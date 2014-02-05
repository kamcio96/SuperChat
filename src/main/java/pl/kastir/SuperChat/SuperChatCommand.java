package pl.kastir.SuperChat;

import lombok.AllArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import pl.kastir.SuperChat.achievements.Achievements;
import pl.kastir.SuperChat.configuration.Config;
import pl.kastir.SuperChat.deaths.DeathInfo;
import pl.kastir.SuperChat.hooks.Hooks;
import pl.kastir.SuperChat.listeners.ChatListener;

@AllArgsConstructor
public class SuperChatCommand implements CommandExecutor {

    private final SuperChat plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
            if ("clear".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("superchat.clear")) {
                    for (int i = 0; i < 58; i++)
                        sender.sendMessage(" ");
                    sender.sendMessage(Config.getMessage("cleared"));
                    return true;
                }
                sender.sendMessage(Config.getMessage("nopermissions"));
                return true;
            }
            if ("clearall".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("superchat.clearall")) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        for (int i = 0; i < 58; i++)
                            p.sendMessage(" ");
                    sender.sendMessage(Config.getMessage("cleared"));
                    return true;
                }
                sender.sendMessage(Config.getMessage("nopermissions"));
                return true;
            }
            if ("reload".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("superchat.reload")) {
                    Config.reloadConfig();
                    Hooks.reload();
                    ChatListener.init();
                    DeathInfo.reload();
                    Achievements.reload();
                    sender.sendMessage(Config.getMessage("reloaded"));
                    return true;
                }
                sender.sendMessage(Config.getMessage("nopermissions"));
                return true;
            }
            if ("toggle".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("superchat.toggle")) {
                    plugin.getUtil().setChatEnabled(!plugin.getUtil().isChatEnabled());
                    String br = Config.getMessage("toggle-" + plugin.getUtil().isChatEnabled());
                    if(!"".equals(br))
                        plugin.getServer().broadcastMessage(br);
                    return true;
                }
                sender.sendMessage(Config.getMessage("nopermissions"));
                return true;
            }
        }
        sender.sendMessage(Config.getMessage("help"));
        return true;
    }




}
