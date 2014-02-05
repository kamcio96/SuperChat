package pl.kastir.SuperChat.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.kastir.SuperChat.SuperChat;
import pl.kastir.SuperChat.configuration.Config;
import pl.kastir.SuperChat.hooks.Hooks;
import pl.kastir.SuperChat.json.SpecialMessage;
import pl.kastir.SuperChat.utils.Util;

public class ChatListener implements Listener {

    public HashMap<String, Long> antispam    = new HashMap<String, Long>(); private String key(Player p){ return p.getName().toLowerCase(); }
    private static String        localFormatted;
    private static String        globalFormatted;
    public static boolean        initialized = false;

    private final SuperChat plugin;

    public ChatListener(SuperChat plugin) {
        this.plugin = plugin;
        init();
        initialized = true;
    }

    public static void init() {
        String local = Hooks.addRawTags(Config.getString("localchat.format"));
        String global = Hooks.addRawTags(Config.getString("global-chat-format"));
        localFormatted = new SpecialMessage(local).toString();
        globalFormatted = new SpecialMessage(global).toString();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChatHightest(AsyncPlayerChatEvent e) {
        Util u = plugin.getUtil();

        if (!u.isChatEnabled() && !e.getPlayer().hasPermission("superchat.toggle.chat")) {
            e.setCancelled(true);
            return;
        }

        Player p = e.getPlayer();
        String message = e.getMessage();

        if (u.isAntySpam() && !p.hasPermission("superchat.bypass")) {
            if (!antispam.containsKey(key(p))) antispam.put(key(p), 0L);
            if (System.currentTimeMillis() - antispam.get(key(p)) <= u.getAntySpamSec()*1000L) {
                p.sendMessage(Config.getMessage("spam"));
                e.setCancelled(true);
                return;
            }
            antispam.put(e.getPlayer().getName().toLowerCase(), System.currentTimeMillis());
        }
        if (Config.getBoolean("log-message")) {
            System.out.println(e.getPlayer().getName() + ": " + message);
        }
        sendChatMessage(p, message);
        e.setCancelled(true);
    }

    public static void sendChatMessage(Player player, String message) {

        boolean local = Config.getBoolean("local-chat");

        if (local && (Config.getBoolean("global-chat-auto")) && (player.hasPermission("superchat.globalchat.all"))) {
            local = false;
        }
        if (local && message.startsWith("!") && (player.hasPermission("superchat.globalchat"))) {
            message = message.replaceFirst("!", "");
            local = false;
        }

        List<Player> rec = new ArrayList<Player>();
        String format;
        if (local) {
            rec.addAll(getLocalPlayers(player));
            format = localFormatted;
        }
        else {
            rec.addAll(Arrays.asList(Bukkit.getOnlinePlayers()));
            format = globalFormatted;
        }

        Hooks.send(format, message, player, rec);
    }

    private static List<Player> getLocalPlayers(Player player) {
        Player[] bukkitplayer = Bukkit.getOnlinePlayers();
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        for (Player top : bukkitplayer) {
            if (((top.getWorld() == player.getWorld()) && (top.getLocation().distance(player.getLocation()) <= Config.getDouble("local-chat-radius"))) || (top.hasPermission("superchat.spy"))) {
                players.add(top);
            }

        }

        return players;
    }
}