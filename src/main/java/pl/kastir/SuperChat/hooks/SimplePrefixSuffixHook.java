package pl.kastir.SuperChat.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class SimplePrefixSuffixHook implements BaseHook {
    
    private HookConfiguration c;
    
    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("Simple Prefix") != null && c.isEnabled();
    }
    
    public String getBaseName() {
        return "spsuffix";
    }
    
    public void init(HookConfiguration c) {
        this.c = c;
    }
    
    public String getJson(Player p, Player to) {
        String format = c.getPrefferedFormat(p, to);
        
        if (p.hasMetadata("suffix")) {
            return format.replaceAll("%tag", ((MetadataValue) p.getMetadata("suffix").get(0)).asString());
        }
        else if (c.isVisibleWhenThereIsNoTag()) return format.replaceAll("%tag", "");
        else return Hooks.getEmptyJson();
    }
    
    public void refresh() {
        c.refresh();
    }
    
}
