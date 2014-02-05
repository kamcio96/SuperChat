package pl.kastir.SuperChat;

import java.io.IOException;

import lombok.Getter;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import pl.kastir.SuperChat.achievements.Achievements;
import pl.kastir.SuperChat.configuration.Config;
import pl.kastir.SuperChat.configuration.ItemNames;
import pl.kastir.SuperChat.deaths.DeathInfo;
import pl.kastir.SuperChat.hooks.FactionsHook;
import pl.kastir.SuperChat.hooks.Hooks;
import pl.kastir.SuperChat.hooks.MultiverseHook;
import pl.kastir.SuperChat.hooks.PlayerHook;
import pl.kastir.SuperChat.hooks.SimpleClansHook;
import pl.kastir.SuperChat.hooks.SimplePrefixPrefixHook;
import pl.kastir.SuperChat.hooks.SimplePrefixSuffixHook;
import pl.kastir.SuperChat.hooks.TimeHook;
import pl.kastir.SuperChat.hooks.VaultPrefixHook;
import pl.kastir.SuperChat.hooks.VaultSuffixHook;
import pl.kastir.SuperChat.listeners.ChatListener;
import pl.kastir.SuperChat.listeners.PlayerListener;
import pl.kastir.SuperChat.utils.Util;

public class SuperChat extends JavaPlugin {
    private static SuperChat    i;

    @Getter
    private static boolean      safeMode        = false;

    private static final String COMPILE_VERSION = "v1_7_R1";

    @Getter
    private Util                util;

    @Override
    public void onEnable() {
        Updater updater = new Updater(this, 56544, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
        i = this;

        Config.init(this);
        Achievements.init(this);
        if (!COMPILE_VERSION.equalsIgnoreCase(Bukkit.getServer().getClass().getPackage().getName().substring(23, 30))) {
            safeMode = true;
            w("This version was compiled using " + COMPILE_VERSION + ", but server version is " + Bukkit.getServer().getClass().getPackage().getName().substring(23, 30));
            w("Plugin may cause errors. Please update plugin as soon as possible.");
        }
        else if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) System.out.println("Update available!");
        Hooks.init(this);
        DeathInfo.init(this);
        ItemNames.init(this);
        if (!getConfig().getBoolean("enable")) {
            w("Plugin has been disabled by configuration file.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            w("Metrics error: " + e.getMessage());
        }

        registerHooks();

        util = new Util(this);

        getCommand("superchat").setExecutor(new SuperChatCommand(this));
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Hooks.refresh();
    }

    @Override
    public void onDisable() {

    }

    private void registerHooks() {
        Hooks.registerHook(new TimeHook());
        Hooks.registerHook(new PlayerHook());
        Hooks.registerHook(new VaultPrefixHook());
        Hooks.registerHook(new VaultSuffixHook());
        Hooks.registerHook(new SimplePrefixPrefixHook());
        Hooks.registerHook(new SimplePrefixSuffixHook());
        Hooks.registerHook(new MultiverseHook());
        Hooks.registerHook(new SimpleClansHook());
        Hooks.registerHook(new FactionsHook());
    }

    private static void w(String message) {
        getI().getLogger().warning(message);
    }

    public static SuperChat getI() {
        return i;
    }

}