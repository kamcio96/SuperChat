package pl.kastir.SuperChat.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class MultiverseHook implements BaseHook {
    
    private HookConfiguration c;
    private MultiverseCore    mv;
    
    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null && c.isEnabled();
    }
    
    public String getBaseName() {
        return "multiverse";
    }
    
    public void init(HookConfiguration c) {
        this.c = c;
        if (isAvailable()) mv = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
    }
    
    public String getJson(Player p, Player to) {
        String format = c.getPrefferedFormat(p, to);
        if (mv != null) { return format.replaceAll("%name", mv.getMVWorldManager().getMVWorld(p.getWorld()).getColoredWorldString()); }
        
        return format.replaceAll("%name", p.getWorld().getName());
    }
    
    public void refresh() {
        c.refresh();
    }
    
}
