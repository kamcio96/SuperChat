package pl.kastir.SuperChat.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
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

        UPlayer factionPlayer = UPlayer.get(p);
        UPlayer factionTo = UPlayer.get(to);

        if(factionPlayer == null || !factionPlayer.hasFaction()){
            format = c.getFormats().get("no-faction");
        } else {
            if(factionTo == null || !factionTo.hasFaction()){
                format = c.getFormats().get("enemy");
            } else if(factionTo.getFactionId().equals(factionPlayer.getFactionId())){
                format = c.getFormats().get("self");
            } else if(getRelation(factionTo.getFaction(), factionPlayer.getFaction()) == Rel.ENEMY){
                format = c.getFormats().get("enemy");
            } else if(getRelation(factionTo.getFaction(), factionPlayer.getFaction()) == Rel.ALLY){
                format = c.getFormats().get("ally");
            } else if(getRelation(factionTo.getFaction(), factionPlayer.getFaction()) == Rel.NEUTRAL){
                format = c.getFormats().get("neutral");
            } else {
                format = c.getFormats().get("enemy");
            }
        }

        if(factionPlayer != null && factionPlayer.hasFaction()){
            Faction f = factionPlayer.getFaction();
            format = format.replaceAll("%tag", f.getName());
            format = format.replaceAll("%name", f.getDescription());
            format = format.replaceAll("%title", factionPlayer.getTitle());
        }

        /*if ((factionPlayer == null || !factionPlayer.hasFaction()) && (UPlayer.get(p) != null || UPlayer.get(p).hasFaction()) && c.getFormats().containsKey("no-faction")) format = c.getFormats().get("no-faction");
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
        }*/
        return format;
    }

    public void refresh() {
        c.refresh();
    }


    private Rel getRelation(Faction f1, Faction f2){
        if(f1.getRelationWish(f2) == Rel.ENEMY && f2.getRelationWish(f1) == Rel.ENEMY){
            return Rel.ENEMY;
        } else if(f1.getRelationWish(f2) == Rel.ALLY && f2.getRelationWish(f1) == Rel.ALLY){
            return Rel.ALLY;
        } else if(f1.getRelationWish(f2) == Rel.NEUTRAL && f2.getRelationWish(f1) == Rel.NEUTRAL){
            return Rel.NEUTRAL;
        }
        return null;
    }

}
