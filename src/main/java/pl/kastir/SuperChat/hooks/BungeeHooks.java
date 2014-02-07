package pl.kastir.SuperChat.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import pl.kastir.SuperChat.SuperChat;

public class BungeeHooks implements PluginMessageListener {
    private static BungeeHooks instance;

    public BungeeHooks() {
        instance = this;
        Bukkit.getMessenger().registerIncomingPluginChannel(SuperChat.getI(), "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(SuperChat.getI(), "BungeeCord");
    }

    /*public static void sendMessage(Player player, String message) {
        instance.sendBungeeMessage(player, message);
    }

    public void sendBungeeMessage(Player player, String message) {
        if (Config.getBoolean("bungeecord")) {
            try {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);

                out.writeUTF("Forward");
                out.writeUTF("ALL");

                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);

                msgout.writeUTF("SuperChat");
                msgout.writeUTF("message");
                msgout.writeUTF(message);

                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());
                System.out.println("message send!");

                player.sendPluginMessage(SuperChat.getI(), "BungeeCord", b.toByteArray());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        /*System.out.println("message: " + channel);
        try {
            ByteArrayInputStream b = new ByteArrayInputStream(message);

            DataInputStream in = new DataInputStream(b);
            String pluginChannel = in.readUTF();
            if (pluginChannel.equals("SuperChat")) {
                String subchannel = in.readUTF();
                if ("message".equals(subchannel)) {
                    String mes = in.readUTF();
                    ChatListener.sendChatMessage(player, mes);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}