package pl.kastir.SuperChat.hooks;

import java.util.Calendar;

import org.bukkit.entity.Player;

public class TimeHook implements BaseHook {

    private HookConfiguration c;

    public boolean isAvailable() {
        return c.isEnabled();
    }

    public String getBaseName() {
        return "time";
    }

    public void init(HookConfiguration c) {
        this.c = c;
    }

    public String getJson(Player p, Player to) {
        return getTime(c.getPrefferedFormat(p, to));
    }

    private String getTime(String string) {
        Calendar calendar = Calendar.getInstance();

        string = string.replaceAll("%h", String.format("%02d", calendar.get(Calendar.HOUR)))
            .replaceAll("%H", String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)))
            .replaceAll("%g", Integer.toString(calendar.get(Calendar.HOUR)))
            .replaceAll("%G", Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)))
            .replaceAll("%i", String.format("%02d", calendar.get(Calendar.MINUTE)))
            .replaceAll("%s", String.format("%02d", calendar.get(Calendar.SECOND)))
            .replaceAll("%a", (calendar.get(Calendar.AM_PM) == 0) ? "am" : "pm")
            .replaceAll("%A", (calendar.get(Calendar.AM_PM) == 0) ? "AM" : "PM");

        return string;
    }

    public void refresh() {
        c.refresh();
    }

}
