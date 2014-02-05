package pl.kastir.SuperChat.hooks;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import pl.kastir.SuperChat.utils.ChatSender;

public class VaultSuffixHook implements BaseHook {

    private HookConfiguration c;
    private Chat              chat;

    public boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault") && c.isEnabled() && Bukkit.getServer().getServicesManager().isProvidedFor(Chat.class);
    }

    public String getBaseName() {
        return "suffix";
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
        if (chat.getPlayerPrefix(p) != null && !chat.getPlayerPrefix(p).isEmpty()) return format.replaceAll("%tag", ChatSender.replace(chat.getPlayerSuffix(p)));
        else if (c.isVisibleWhenThereIsNoTag()) return format.replaceAll("%tag", "");
        else return Hooks.getEmptyJson();
    }

    public void refresh() {
        c.refresh();
    }

}