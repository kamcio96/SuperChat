package pl.kastir.SuperChat.hooks;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import lombok.Getter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.kastir.SuperChat.SuperChat;
import pl.kastir.SuperChat.json.SpecialMessage;

public class HookConfiguration {

    @Getter
    private final HashMap<String, String> formats = new HashMap<String, String>();
    @Getter
    private String                        globalFormat;
    @Getter
    private boolean                       enabled;
    @Getter
    private boolean                       visibleWhenThereIsNoTag;
    @Getter
    private ConfigurationSection          configSection;
    private final String                  baseName;

    public HookConfiguration(String baseName) {
        this.baseName = baseName;
        refresh();
    }

    public void refresh() {
        if (!Hooks.getConfig().contains(baseName)) {
            SuperChat.getI().getLogger().log(Level.WARNING, "Hook " + baseName + " don't have configuration section");
            enabled = false;
        }
        else {
            configSection = Hooks.getConfig().getConfigurationSection(baseName);
            Set<String> s = configSection.getKeys(false);
            for (String k : s) {
                if (k.startsWith("format-")) formats.put(k.replace("format-", ""), new SpecialMessage(configSection.getString(k)).toString2());
            }
            globalFormat = new SpecialMessage(configSection.getString("format")).toString2();
            enabled = configSection.getBoolean("enable", false);
            visibleWhenThereIsNoTag = configSection.getBoolean("display-empty", true);
        }
    }

    public String getPrefferedFormat(Player from, Player to) {
        if (to.equals(from) && getFormats().containsKey("self")) return getFormats().get("self");
        else if (to.isOp() && getFormats().containsKey("op")) return getFormats().get("op");
        else {
            for (Entry<String, String> e : getFormats().entrySet()) {
                if (to.hasPermission(e.getKey().replaceAll("-", "."))) { return e.getValue(); }
            }
        }
        return getGlobalFormat();
    }

}