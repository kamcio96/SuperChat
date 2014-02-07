package pl.kastir.SuperChat.automessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.kastir.SuperChat.SuperChat;
import pl.kastir.SuperChat.json.SpecialMessage;

public class AutoMessage {

    private final SuperChat plugin;

    private MessageScheduler task;

    private FileConfiguration config;

    public AutoMessage(SuperChat plugin){
        this.plugin = plugin;
        reload();

    }

    public void reload(){
        File f = new File(plugin.getDataFolder(), "automsg.yml");
        if(!f.exists()){
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(f);
        config.options().copyDefaults(true);
        config.addDefaults(YamlConfiguration.loadConfiguration(plugin.getResource("automsg.yml")));

        try {
            config.save(f);
            System.out.println("save");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!config.getBoolean("enabled")){
            return;
        }

        List<String> messages = new ArrayList<String>();
        for(String list : config.getStringList("messages")){
            messages.add(new SpecialMessage(list).toString());
        }


        if(task != null){
            task.cancel();
            task = null;
        }


        task = new MessageScheduler(messages, config.getBoolean("random"));
        task.runTaskTimer(plugin, 10, config.getLong("time")*20);
    }

}
