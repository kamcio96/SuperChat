package pl.kastir.SuperChat.deaths;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import pl.kastir.SuperChat.configuration.Config;
import pl.kastir.SuperChat.configuration.ItemNames;
import pl.kastir.SuperChat.json.SpecialMessage;
import pl.kastir.SuperChat.utils.ItemSerializer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class DeathInfo {
    private static File                    d;
    private static FileConfiguration       config;
    private static String                  itemTemplate;
    private static HashMap<String, String> templates = new HashMap<String, String>();
    @Getter
    @Setter
    public HashMap<String, Object>         params    = new HashMap<String, Object>();
    @Getter
    public final String                    deathMessage;
    
    public DeathInfo add(String key, Object value) {
        params.put(key, value);
        return this;
    }
    
    public String getJson() {
        String json = templates.get(deathMessage);
        if (json == null) {
            System.out.println("Missing death translation: " + deathMessage);
            json = templates.get("death.attack.generic");
            if (json == null) json = "{text: '%player has died'}";
        }
        for (Entry<String, Object> e : params.entrySet()) {
            if (e.getKey().equals("item") || e.getKey().equals("damager") || e.getKey().equals("custom")) continue;
            else json = json.replace("%" + e.getKey(), e.getValue().toString());
        }
        if (params.containsKey("item")) {
            ItemStack item = (ItemStack) params.get("item");
            String itemjson = ItemSerializer.getJson(item);
            String name = "";
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) name = item.getItemMeta().getDisplayName();
            else {
                name = ItemNames.getItemName(item);
            }
            String temp = itemTemplate.replace("%json", itemjson).replace("%name", name);
            json = json.replace("%item", temp);
        }
        if (params.containsKey("damager") && !params.containsKey("custom")) {
            if (Config.has("messages.entities." + ((String) params.get("damager")).toUpperCase())) {
                json = json.replace("%damager", Config.getString("messages.entities." + ((String) params.get("damager")).toUpperCase()));
            }
            else json = json.replace("%damager", params.get("damager").toString());
        }
        else if (params.containsKey("custom")) {
            json = json.replace("%damager", params.get("custom").toString());
        }
        
        return json;
    }
    
    public static void init(JavaPlugin p) {
        try {
            d = new File(p.getDataFolder(), "deaths.yml");
            if (!d.exists()) {
                d.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(d);
            config.options().copyDefaults(true);
            config.addDefaults(YamlConfiguration.loadConfiguration(p.getResource("deaths.yml")));
            config.save(d);
            itemTemplate = new SpecialMessage("<itemtip='%json'>%name</itemtip>").toString2();
            Set<String> set = config.getKeys(true);
            for (String s : set) {
                if (s.startsWith(".")) s = s.substring(1);
                if (config.get(s) instanceof String) {
                    String s1 = (String) config.get(s);
                    s1 = s1.replace("%item", "<raw>%item</raw>");
                    templates.put(s, new SpecialMessage(s1).toString());
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public static void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(d);
            config.options().copyDefaults(true);
            config.save(d);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        itemTemplate = new SpecialMessage("<itemtip='%json'>%name</itemtip>").toString2();
        Set<String> set = config.getKeys(true);
        for (String s : set) {
            if (s.startsWith(".")) s = s.substring(1);
            if (config.get(s) instanceof String) {
                String s1 = (String) config.get(s);
                s1 = s1.replace("%item", "<raw>%item</raw>");
                templates.put(s, new SpecialMessage(s1).toString());
            }
        }
    }
    
}
