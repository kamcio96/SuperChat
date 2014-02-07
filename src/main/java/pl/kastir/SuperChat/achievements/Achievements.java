package pl.kastir.SuperChat.achievements;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.v1_7_R1.Achievement;
import net.minecraft.server.v1_7_R1.AchievementList;
import net.minecraft.server.v1_7_R1.DedicatedServer;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.ServerStatisticManager;
import net.minecraft.util.com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import pl.kastir.SuperChat.configuration.Config;
import pl.kastir.SuperChat.json.SpecialMessage;
import pl.kastir.SuperChat.utils.ChatSender;
import pl.kastir.SuperChat.utils.Util;
import pl.kastir.SuperChat.utils.reflection.SafeField;

public class Achievements implements Listener {

    private static File                a;
    private static YamlConfiguration   config;
    private static String              achievementTemplate;
    private static String              achievementMessage;
    private static Map<String, String> templates = new HashMap<String, String>();

    @SuppressWarnings("unchecked")
    public static void init(JavaPlugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(new Achievements(), p);
        try {
            ((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.a("announce-player-achievements", false);
            a = new File(p.getDataFolder(), "achievements.yml");
            if (!a.exists()) {
                a.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(a);
            config.options().copyDefaults(true);
            for (Achievement ach : (List<Achievement>) AchievementList.e) {
                if (!config.contains(ach.e)) config.set(ach.e, "achievement");
            }
            config.save(a);
            achievementTemplate = new SpecialMessage("<achievementtip='%id'>%name</achievementtip>").toString2();
            achievementMessage = new SpecialMessage(Config.getMessage("achievement").replace("%achievement", "<raw>%achievement</raw>")).toString();
            Set<String> set = config.getKeys(true);
            for (String s : set) {
                if (s.startsWith(".")) s = s.substring(1);
                if (config.get(s) instanceof String) {
                    templates.put(s, Util.getColors((String) config.get(s)));
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void reload() {
        try {
            ((DedicatedServer) ((CraftServer) Bukkit.getServer()).getServer()).propertyManager.a("announce-player-achievements", false);
            config = YamlConfiguration.loadConfiguration(a);
            config.options().copyDefaults(true);
            for (Achievement ach : (List<Achievement>) AchievementList.e) {
                if (!config.contains(ach.e)) config.set(ach.e, "achievement");
            }
            config.save(a);
            achievementTemplate = new SpecialMessage("<achievementtip='%id'>%name</achievementtip>").toString2();
            achievementMessage = new SpecialMessage(Config.getMessage("achievement").replace("%achievement", "<raw>%achievement</raw>")).toString();
            Set<String> set = config.getKeys(true);
            for (String s : set) {
                if (s.startsWith(".")) s = s.substring(1);
                if (config.get(s) instanceof String) {
                    templates.put(s, Util.getColors((String) config.get(s)));
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ServerStatisticManager o = SafeField.get(EntityPlayer.class, ((CraftPlayer) e.getPlayer()).getHandle(), "bO", ServerStatisticManager.class);
        MinecraftServer s = SafeField.get(ServerStatisticManager.class, o, "c", MinecraftServer.class);
        File f = SafeField.get(ServerStatisticManager.class, o, "d", File.class);
        SafeField.set(EntityPlayer.class, ((CraftPlayer) e.getPlayer()).getHandle(), "bO", new StatsManager(s, f));
    }

    public static void sendMessage(Player p, String ach) {
        String result = achievementMessage;
        result = result.replace("%player", p.getDisplayName());
        result = result.replace("%achievement", achievementTemplate.replace("%id", ach).replace("%name", templates.get(ach)));
        if (Config.getBoolean("achievement-notification")) ChatSender.sendToPlayers(Lists.newArrayList(Bukkit.getOnlinePlayers()), result);
    }

}
