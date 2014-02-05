package pl.kastir.SuperChat.deaths;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R1.ChatMessage;
import net.minecraft.server.v1_7_R1.CombatEntry;
import net.minecraft.server.v1_7_R1.CombatTracker;
import net.minecraft.server.v1_7_R1.DamageSource;
import net.minecraft.server.v1_7_R1.Entity;
import net.minecraft.server.v1_7_R1.EntityInsentient;
import net.minecraft.server.v1_7_R1.EntityLiving;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.ItemStack;

public class MyCombatTracker {
    
    public static DeathInfo getSafeDeathInfo(Player player) {
        return new DeathInfo("death.attack.generic").add("player", player.getName());
    }
    
    @SuppressWarnings("unchecked")
    public static DeathInfo getDeathInfo(Player player) {
        try {
            EntityPlayer p = ((CraftPlayer) player).getHandle();
            Field f = CombatTracker.class.getDeclaredField("a");
            f.setAccessible(true);
            List<CombatEntry> a = (List<CombatEntry>) f.get(p.combatTracker);
            String ii = ((ChatMessage) p.combatTracker.b()).i();
            if (!ii.equalsIgnoreCase("death.attack.player")) {
                ii = ii.replace(".player", "-damager").replace(".item", "-item");
            }
            else ii = ii.replace(".item", "-item");
            DeathInfo i = new DeathInfo("temp").add("player", player.getName());
            
            CombatEntry entry1 = f(a);
            CombatEntry entry2 = (CombatEntry) a.get(a.size() - 1);
            
            Entity damager1 = entry2.a().getEntity();
            if ((entry1 != null) && (entry2.a() == DamageSource.FALL)) {
                Entity damager2 = entry1.a().getEntity();
                
                if ((entry1.a() != DamageSource.FALL) && (entry1.a() != DamageSource.OUT_OF_WORLD)) {
                    if ((damager2 != null) && ((damager1 == null) || (!damager2.equals(damager1)))) {
                        ItemStack item = (damager2 instanceof EntityLiving) ? ((EntityLiving) damager2).be() : null;
                        
                        if (item != null) i.add("damager", damager2.getName()).add("item", CraftItemStack.asCraftMirror(item));
                        else i.add("damager", damager2.getName());
                        if (damager2 instanceof EntityInsentient && ((EntityInsentient) damager2).hasCustomName()) i.add("custom", ((EntityInsentient) damager2).getCustomName());
                    }
                    else if (damager1 != null) {
                        ItemStack item = (damager1 instanceof EntityLiving) ? ((EntityLiving) damager1).be() : null;
                        if (item != null) i.add("damager", damager1.getName()).add("item", CraftItemStack.asCraftMirror(item));
                        else i.add("damager", damager1.getName());
                        if (damager1 instanceof EntityInsentient && ((EntityInsentient) damager1).hasCustomName()) i.add("custom", ((EntityInsentient) damager1).getCustomName());
                    }
                }
            }
            if (!i.getParams().containsKey("damager") && p.aX() != null) i.add("damager", p.aX().getName());
            if (!i.getParams().containsKey("item") && i.getParams().containsKey("damager") && p.aX() != null && p.aX().be() != null) i.add("item", CraftItemStack.asCraftMirror(p.aX().be()));
            if (p.aX() != null && p.aX() instanceof EntityInsentient && ((EntityInsentient) p.aX()).hasCustomName()) i.add("custom", ((EntityInsentient) p.aX()).getCustomName());
            if (i.getParams().containsKey("item")) ii = ii.replace("-damager", "-item");
            if (i.getParams().containsKey("item") && !ii.contains("-item")) ii = ii + "-item";
            DeathInfo iii = new DeathInfo(ii);
            iii.setParams(i.getParams());
            return iii;
        }
        catch (Throwable t) {
            return new DeathInfo("death.attack.generic").add("player", player.getName());
        }
    }
    
    private static CombatEntry f(List<CombatEntry> a) {
        CombatEntry localObject1 = null;
        CombatEntry localObject2 = null;
        int i = 0;
        float f1 = 0.0F;
        
        for (int j = 0; j < a.size(); j++) {
            CombatEntry localCombatEntry = (CombatEntry) a.get(j);
            CombatEntry localObject3 = j > 0 ? (CombatEntry) a.get(j - 1) : null;
            
            if (((localCombatEntry.a() == DamageSource.FALL) || (localCombatEntry.a() == DamageSource.OUT_OF_WORLD)) && (localCombatEntry.i() > 0.0F) && ((localObject1 == null) || (localCombatEntry.i() > f1))) {
                if (j > 0) localObject1 = localObject3;
                else {
                    localObject1 = localCombatEntry;
                }
                f1 = localCombatEntry.i();
            }
            
            if ((localCombatEntry.g() != null) && ((localObject2 == null) || (localCombatEntry.c() > i))) {
                localObject2 = localCombatEntry;
            }
        }
        
        if ((f1 > 5.0F) && (localObject1 != null)) return localObject1;
        if ((i > 5) && (localObject2 != null)) { return localObject2; }
        return null;
    }
    
}
