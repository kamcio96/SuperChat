package pl.kastir.SuperChat.hooks;

import org.bukkit.entity.Player;

public class PlayerHook implements BaseHook {
    
    private HookConfiguration c;
    
    public boolean isAvailable() {
        return true;
    }
    
    public String getBaseName() {
        return "player";
    }
    
    public void init(HookConfiguration c) {
        this.c = c;
    }
    
    public String getJson(Player p, Player to) {
        return c.getPrefferedFormat(p, to).replaceAll("%name", p.getDisplayName()).replaceAll("%realname", p.getName());
    }
    
    public void refresh() {
        c.refresh();
    }
}
