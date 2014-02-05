package pl.kastir.SuperChat.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import pl.kastir.SuperChat.json.SpecialMessage;
import pl.kastir.SuperChat.utils.Util;

public class ItemNames {
    private static File                    is;
    private static YamlConfiguration       config;
    private static HashMap<String, String> items = new HashMap<String, String>();
    
    public static void init(JavaPlugin p) {
        try {
            is = new File(p.getDataFolder(), "items.yml");
            if (!is.exists()) {
                is.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(is);
            Set<String> set = config.getKeys(false);
            for (Material m : Material.values()) {
                if (!config.contains(m.name().toLowerCase())) {
                    config.set(m.name().toLowerCase(), m.name().substring(0, 1) + m.name().replaceAll("_", " ").toLowerCase().substring(1));
                }
            }
            config.save(is);
            for (String s : set) {
                if (s.startsWith(".")) s = s.substring(1);
                if (config.get(s) instanceof String) {
                    items.put(s, new SpecialMessage(Util.getColors((String) config.get(s))).toString());
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    public static void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(is);
            Set<String> set = config.getKeys(false);
            for (Material m : Material.values()) {
                if (!config.contains(m.name().toLowerCase())) {
                    config.set(m.name().toLowerCase(), m.name().substring(0, 1) + m.name().replaceAll("_", " ").toLowerCase().substring(1));
                }
            }
            config.save(is);
            for (String s : set) {
                if (s.startsWith(".")) s = s.substring(1);
                if (config.get(s) instanceof String) {
                    items.put(s, new SpecialMessage(Util.getColors((String) config.get(s))).toString());
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public static String getItemName(ItemStack i) {
        return items.get(i.getType().name().toLowerCase());
    }
    
}
