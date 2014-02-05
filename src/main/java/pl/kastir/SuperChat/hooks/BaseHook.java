package pl.kastir.SuperChat.hooks;

import org.bukkit.entity.Player;

public interface BaseHook {
    public boolean isAvailable();
    public String getBaseName();
    public void init(HookConfiguration c);
    public String getJson(Player p, Player to);
    public void refresh();
}
