package pl.kastir.SuperChat.achievements;

import java.io.File;

import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.ServerStatisticManager;
import net.minecraft.server.v1_7_R1.Statistic;

import org.bukkit.entity.Player;

public class StatsManager extends ServerStatisticManager {
    
    public StatsManager(MinecraftServer arg0, File arg1) {
        super(arg0, arg1);
        a();
    }
    
    @Override
    public void a(EntityHuman paramEntityHuman, Statistic statistic, int paramInt) {
        int i = a(statistic);
        super.a(paramEntityHuman, statistic, paramInt);
        if ((statistic.d()) && (i == 0) && (paramInt > 0)) {
            Achievements.sendMessage((Player) paramEntityHuman.getBukkitEntity(), statistic.e);
        }
    }
}
