package pl.kastir.SuperChat.achievements;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.ServerStatisticManager;
import net.minecraft.server.v1_7_R1.Statistic;

public class StatsManager extends ServerStatisticManager {
    
    public MinecraftServer c;
    @SuppressWarnings("rawtypes")
    public Set             e;
    public Field           gf;
    
    @SuppressWarnings("rawtypes")
    public StatsManager(MinecraftServer arg0, File arg1) {
        super(arg0, arg1);
        c = arg0;
        try {
            Field ef = ServerStatisticManager.class.getDeclaredField("e");
            ef.setAccessible(true);
            e = (Set) ef.get(this);
            gf = ServerStatisticManager.class.getDeclaredField("g");
            gf.setAccessible(true);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public void a(EntityHuman paramEntityHuman, Statistic statistic, int paramInt) {
        int i = a(statistic);
        super.a(paramEntityHuman, statistic, paramInt);
        if ((statistic.d()) && (i == 0) && (paramInt > 0)) {
            Achievements.sendMessage((Player) paramEntityHuman.getBukkitEntity(), statistic.e);
        }
    }
}
