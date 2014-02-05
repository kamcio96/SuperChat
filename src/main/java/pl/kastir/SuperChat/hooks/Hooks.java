package pl.kastir.SuperChat.hooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import pl.kastir.SuperChat.SuperChat;
import pl.kastir.SuperChat.listeners.ChatListener;
import pl.kastir.SuperChat.utils.ChatSender;

public class Hooks {

    private static final HashMap<String, BaseHook> registeredHooks = new HashMap<String, BaseHook>();
    @Getter
    private static FileConfiguration               config;
    private static File h;

    private static SuperChat plugin;

    public static void init(SuperChat p) {
        Hooks.plugin = p;
        try {
            h = new File(p.getDataFolder(), "hooks.yml");
            if (!h.exists()) {
                h.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(h);
            config.options().copyDefaults(true);
            config.addDefaults(YamlConfiguration.loadConfiguration(p.getResource("hooks.yml")));
            config.save(h);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        new BungeeHooks();
    }

    public static void registerHook(BaseHook h) {
        if (registeredHooks.containsKey(h.getBaseName())) throw new RuntimeException("Hook " + h.getBaseName() + " already registered!");
        h.init(new HookConfiguration(h.getBaseName()));
        if (!h.isAvailable()) {
        }
        else {
            registeredHooks.put(h.getBaseName(), h);
            plugin.getLogger().log(Level.INFO, "Hook " + h.getBaseName() + " has been registered.");
            if (ChatListener.initialized) ChatListener.init();
        }
    }

    public static String addRawTags(String s) {
        for (Entry<String, BaseHook> e : registeredHooks.entrySet()) {
            s = s.replace("%" + e.getKey(), "<raw>%" + e.getValue().getBaseName() + "</raw>");
        }
        return s;
    }

    public static void send(String format, String message, Player p, List<Player> rec) {
        HashMap<Player, String> formats = new HashMap<Player, String>();
        HashMap<String, List<Player>> grouped = new HashMap<String, List<Player>>();
        for (Player to : rec) {
            String format1 = format;
            for (Entry<String, BaseHook> e : registeredHooks.entrySet()) {
                if (e.getValue().isAvailable()) format1 = format1.replace("%" + e.getKey(), e.getValue().getJson(p, to));
            }
            format1 = format1.replaceAll("%message", getColors(message.replace("$", "\\$").replace("'", "\\\\'"), p));
            formats.put(to, format1);
        }
        for (Entry<Player, String> e : formats.entrySet()) {
            if (!grouped.containsKey(e.getValue())) grouped.put(e.getValue(), new ArrayList<Player>());
            if (grouped.containsKey(e.getValue())) grouped.get(e.getValue()).add(e.getKey());
        }
        for (Entry<String, List<Player>> e : grouped.entrySet()) {
            ChatSender.sendToPlayers(e.getValue(), e.getKey());
        }
    }

    public static String getColors(String string, Player player) {
        for (ChatColor color : ChatColor.values()) {
            if (player.hasPermission("superchat.colors." + color.name().toLowerCase())) {
                string = string.replaceAll("&" + color.getChar(), color.toString());
            }
        }
        return string;
    }

    public static void refresh() {
        for (Entry<String, BaseHook> e : registeredHooks.entrySet()) {
            e.getValue().refresh();
        }
    }

    public static String getEmptyJson() {
        return "{text:''}";
    }

    public static void reload() {
        try {
            config.load(h);
            refresh();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
