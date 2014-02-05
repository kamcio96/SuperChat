package pl.kastir.SuperChat.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import pl.kastir.SuperChat.utils.reflection.ReflectionUtil;
import pl.kastir.SuperChat.utils.reflection.ReflectionUtil.DynamicPackage;

public class ChatSender {
    
    public static Object getPacket(String jsonMessage) {
        try {
            return ReflectionUtil.newInstance(ReflectionUtil.getClass("PacketPlayOutChat", DynamicPackage.MINECRAFT_SERVER), ReflectionUtil.getClass("ChatSerializer", DynamicPackage.MINECRAFT_SERVER).getMethod("a", String.class).invoke(null, jsonMessage));
        }
        catch (Throwable t) {
            System.out.println("Json message: " + jsonMessage);
            new RuntimeException("Oops, something went wrong while creating packet!", t).printStackTrace();
        }
        return null;
    }
    
    public static void sendToPlayer(Player player, String jsonMessage) {
        Object p = getPacket(jsonMessage);
        if (p == null) return;
        try {
            Object pl = player.getClass().getMethod("getHandle").invoke(player);
            Object pc = pl.getClass().getField("playerConnection").get(pl);
            pc.getClass().getMethod("sendPacket", ReflectionUtil.getClass("Packet", DynamicPackage.MINECRAFT_SERVER)).invoke(pc, p);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public static void sendToPlayers(Iterable<Player> players, String jsonMessage) {
        Object p = getPacket(jsonMessage);
        if (p == null) return;
        for (Player player : players) {
            try {
                Object pl = player.getClass().getMethod("getHandle").invoke(player);
                Object pc = pl.getClass().getField("playerConnection").get(pl);
                pc.getClass().getMethod("sendPacket", ReflectionUtil.getClass("Packet", DynamicPackage.MINECRAFT_SERVER)).invoke(pc, p);
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    public static String replace(String string) {
        return ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('$', string));
    }
}
