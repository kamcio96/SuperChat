package pl.kastir.SuperChat.utils;

import org.bukkit.ChatColor;

import pl.kastir.SuperChat.SuperChat;

import lombok.Getter;

public class Util {
    
    private final SuperChat plugin;
    
    @Getter
    private boolean         chatEnabled, antySpam;
    
    @Getter
    private int             antySpamSec;
    
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
}
