package pl.kastir.SuperChat.hooks;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SimpleClansHook implements BaseHook {
    
    private HookConfiguration c;
    private SimpleClans       sclans;
    
    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("SimpleClans") != null && c.isEnabled();
    }
    
    public String getBaseName() {
        return "simpleclans";
    }
    
    public void init(HookConfiguration c) {
        this.c = c;
        if (isAvailable()) {
            if ((sclans == null) && (Bukkit.getPluginManager().isPluginEnabled("SimpleClans"))) {
                sclans = (SimpleClans) Bukkit.getPluginManager().getPlugin("SimpleClans");
            }
        }
    }
    
    public String getJson(Player p, Player to) {
        String format = null;
        if (c.getFormats().containsKey("ally") && sclans.getClanManager().getClanPlayer(p) != null && sclans.getClanManager().getClanPlayer(to) != null && sclans.getClanManager().getClanPlayer(p).getClan().getAllAllyMembers().contains(sclans.getClanManager().getClanPlayer(to).getClan())) format = c.getFormats().get("ally");
        else if (c.getFormats().containsKey("enemy") && sclans.getClanManager().getClanPlayer(p) != null && sclans.getClanManager().getClanPlayer(to) != null && !sclans.getClanManager().getClanPlayer(p).getClan().getAllAllyMembers().contains(sclans.getClanManager().getClanPlayer(to).getClan())) format = c.getFormats().get("enemy");
        else if (c.getFormats().containsKey("no-clan") && sclans.getClanManager().getClanPlayer(p) != null && sclans.getClanManager().getClanPlayer(to) == null) format = c.getFormats().get("enemy");
        if (format == null) format = c.getPrefferedFormat(p, to);
        
        if (sclans.getClanManager().getClanPlayer(p) != null) {
            Clan clan = sclans.getClanManager().getClanPlayer(p).getClan();
            format = format.replaceAll("%tag", clan.getTag());
            format = format.replaceAll("%name", clan.getName());
        }
        else {
            if (c.isVisibleWhenThereIsNoTag()) {
                format = format.replaceAll("%tag", c.getConfigSection().getString("no-clan-tag"));
                format = format.replaceAll("%name", c.getConfigSection().getString("no-clan-name"));
            }
            else return Hooks.getEmptyJson();
        }
        return format;
    }
    
    public void refresh() {
        c.refresh();
    }
    
}
