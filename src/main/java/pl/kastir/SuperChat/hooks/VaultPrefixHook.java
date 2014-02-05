package pl.kastir.SuperChat.hooks;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import pl.kastir.SuperChat.utils.ChatSender;

public class VaultPrefixHook implements BaseHook {
    public HookConfiguration c;
    public Chat              chat;

    public boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault") && c.isEnabled() && Bukkit.getServer().getServicesManager().isProvidedFor(Chat.class);
    }

    public String getBaseName() {
        return "prefix";
    }

    public void init(HookConfiguration c) {
        this.c = c;
        if (isAvailable()) {
            RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
            if (chatProvider != null) {
                chat = chatProvider.getProvider();
            }
        }
    }

    public String getJson(Player p, Player to) {
        String format = c.getPrefferedFormat(p, to);
        if (chat.getPlayerPrefix(p) != null && !chat.getPlayerPrefix(p).isEmpty()) return format.replace("%tag", ChatSender.replace(chat.getPlayerPrefix(p)));
        else if (c.isVisibleWhenThereIsNoTag()) return format.replace("%tag", "");
        else return Hooks.getEmptyJson();
    }

    public void refresh() {
        c.refresh();
    }
}