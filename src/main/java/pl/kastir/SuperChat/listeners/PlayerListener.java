package pl.kastir.SuperChat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.kastir.SuperChat.SuperChat;
import pl.kastir.SuperChat.configuration.Config;
import pl.kastir.SuperChat.deaths.MyCombatTracker;
import pl.kastir.SuperChat.utils.ChatSender;
import pl.kastir.SuperChat.utils.Util;

import com.google.common.collect.Lists;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(Util.getColors(Config.getMessage("join").replaceAll("%player", event.getPlayer().getDisplayName()).replaceAll("%realname", event.getPlayer().getName())));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Util.getColors(Config.getMessage("quit").replaceAll("%player", event.getPlayer().getDisplayName()).replaceAll("%realname", event.getPlayer().getName())));
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(Util.getColors(Config.getMessage("kick").replaceAll("%player", event.getPlayer().getDisplayName()).replaceAll("%realname", event.getPlayer().getName()).replaceAll("%reason", event.getReason())));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage("");
        if (!SuperChat.isSafeMode()) ChatSender.sendToPlayers(Lists.newArrayList(Bukkit.getOnlinePlayers()), MyCombatTracker.getDeathInfo(player).getJson());
        else ChatSender.sendToPlayers(Lists.newArrayList(Bukkit.getOnlinePlayers()), MyCombatTracker.getSafeDeathInfo(player).getJson());
    }
}