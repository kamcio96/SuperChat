package pl.kastir.SuperChat.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SimplePrefixPrefixHook implements BaseHook {

    private HookConfiguration c;

    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("Simple Prefix") != null && c.isEnabled();
    }

    public String getBaseName() {
        return "spprefix";
    }

    public void init(HookConfiguration c) {
        this.c = c;
    }

    public String getJson(Player p, Player to) {
        String format = c.getPrefferedFormat(p, to);

        if (p.hasMetadata("prefix")) {
            return format.replaceAll("%tag", p.getMetadata("prefix").get(0).asString());
        }
        else if (c.isVisibleWhenThereIsNoTag()) return format.replaceAll("%tag", "");
        else return Hooks.getEmptyJson();
    }

    public void refresh() {
        c.refresh();
    }

}
