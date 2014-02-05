package pl.kastir.SuperChat.automessage;

import java.util.List;
import java.util.Random;

import net.minecraft.util.com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import pl.kastir.SuperChat.utils.ChatSender;

public class MessageScheduler extends BukkitRunnable {

    private final List<String> messages;
    private final boolean rand;
    private int i=0;
    private final Random random = new Random();

    public MessageScheduler(List<String> messages, boolean rand){
        this.messages = messages;
        this.rand = rand;
    }

    public void run() {
        if(rand){
            int r = random.nextInt(messages.size());
            broadcast(messages.get(r));
        } else {
            if(i >= messages.size()) i = 0;
            broadcast(messages.get(i));
            i++;
        }

    }


    private void broadcast(String message){
        ChatSender.sendToPlayers(Lists.newArrayList(Bukkit.getOnlinePlayers()), message);
    }

}
