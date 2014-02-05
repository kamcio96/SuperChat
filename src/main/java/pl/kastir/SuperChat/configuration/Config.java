package pl.kastir.SuperChat.configuration;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.kastir.SuperChat.SuperChat;

public class Config {
    private static SuperChat plugin;

    private static FileConfiguration messages;

    public static void init(SuperChat main) {
        plugin = main;
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();

        try {
            File f = new File(main.getDataFolder(), "messages.yml");
            if (!f.exists()) {
                f.createNewFile();
            }
            messages = YamlConfiguration.loadConfiguration(f);
            messages.options().copyDefaults(true);
            messages.addDefaults(YamlConfiguration.loadConfiguration(main.getResource("messages.yml")));
            messages.save(f);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static String getMessage(String path) {
        if (!messages.contains(path)) return "Not found: messages.yml => " + path;
        String message = messages.getString(path);
        for (ChatColor color : ChatColor.values()) {
            message = message.replaceAll("\\$" + color.getChar(), color.toString());
        }

        message = message.replaceAll("%new_line%", "\n");
        message = message.replaceAll("\\\\n", "\n");
        return message;
    }

    public static String getString(String path) {
        return plugin.getConfig().getString(path);
    }

    public static boolean getBoolean(String path) {
        return plugin.getConfig().getBoolean(path);
    }

    public static double getDouble(String path) {
        return plugin.getConfig().getDouble(path);
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveConfig();

        try {
            File f = new File(plugin.getDataFolder(), "messages.yml");
            if (!f.exists()) {
                f.createNewFile();
            }
            messages = YamlConfiguration.loadConfiguration(f);
            messages.options().copyDefaults(true);
            messages.addDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("messages.yml")));
            messages.save(f);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public static Long getLong(String path) {
        return plugin.getConfig().getLong(path);
    }

    public static boolean has(String path) {
        return plugin.getConfig().isSet(path);
    }

}