package pl.kastir.SuperChat.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import pl.kastir.SuperChat.SuperChat;

public class Util {

    private final SuperChat plugin;

    @Getter
    private boolean         chatEnabled, antySpam;

    @Getter
    private int             antySpamSec;

    private final List<String> spyEnabled = new ArrayList<String>();

    public Util(SuperChat plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        chatEnabled = plugin.getConfig().getBoolean("chatenabled");
        antySpam = plugin.getConfig().getBoolean("antyspam.enable");
        antySpamSec = plugin.getConfig().getInt("antyspam.minsec");
    }

    public void setChatEnabled(boolean enable) {
        chatEnabled = enable;
        plugin.getConfig().set("chatenabled", enable);
    }

    public static String getColors(String string) {
        for (ChatColor color : ChatColor.values()) {
            string = string.replaceAll("\\$" + color.getChar(), color.toString());
        }
        return string;
    }

    @Deprecated
    public boolean hasSpy(String player){
        return spyEnabled.contains(player.toLowerCase());
    }

    public boolean hasSpy(Player player){
        return player.hasPermission("superchat.spy") && hasSpy(player.getName());
    }

    public void setSpy(Player player, boolean has){
        setSpy(player.getName(), has);
    }

    public void setSpy(String name, boolean has) {
        if(hasSpy(name)){
            spyEnabled.remove(name.toLowerCase());
        } else {
            spyEnabled.add(name.toLowerCase());
        }
    }

}
