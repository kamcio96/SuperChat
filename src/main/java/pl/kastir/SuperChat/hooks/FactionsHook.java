package pl.kastir.SuperChat.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.UPlayer;

public class FactionsHook implements BaseHook {

    private HookConfiguration c;

    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("Factions") != null && c.isEnabled();
    }

    public String getBaseName() {
        return "factions";
    }

    public void init(HookConfiguration c) {
        this.c = c;
    }

    public String getJson(Player p, Player to) {
        String format = null;
        if ((UPlayer.get(to) == null || !UPlayer.get(to).hasFaction()) && (UPlayer.get(p) != null || UPlayer.get(p).hasFaction()) && c.getFormats().containsKey("no-faction")) format = c.getFormats().get("no-faction");
        if (format == null) format = c.getPrefferedFormat(p, to);
        if (UPlayer.get(p) != null && UPlayer.get(p).hasFaction()) {
            UPlayer f = UPlayer.get(p);
            format = format.replaceAll("%tag", f.getFaction().getName());
            format = format.replaceAll("%name", f.getFaction().getDescription());
            format = format.replaceAll("%title", f.getTitle());
        }
        else {
            if (c.isVisibleWhenThereIsNoTag()) {
                format = format.replaceAll("%tag", c.getConfigSection().getString("no-faction-tag"));
                format = format.replaceAll("%name", c.getConfigSection().getString("no-faction-name"));
                format = format.replaceAll("%title", c.getConfigSection().getString("no-title"));
            }
            else return Hooks.getEmptyJson();
        }
        return format;
    }

    public void refresh() {
        c.refresh();
    }

}
