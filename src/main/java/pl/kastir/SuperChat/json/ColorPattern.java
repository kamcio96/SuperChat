package pl.kastir.SuperChat.json;

import org.bukkit.ChatColor;

public class ColorPattern {

    ChatColor[] pattern;
    int index;

    public ColorPattern (String colours) {
        pattern = new ChatColor[colours.length()];
        for (int i = 0; i < colours.length(); ++i) {
            pattern[i] = ChatColor.getByChar(colours.charAt(i));
        }
    }

    public ColorPattern (ChatColor... colours) {
        this.pattern = colours;
    }

    public String patterned (String value) {
        if (this.isMonochrome())
            return pattern[0] + value;

        String result = "";
        for (int i = 0; i < value.length(); ++i) {
            result += "\\\\u00A7" + this.next().getChar() + value.charAt(i);
        }

        return result;
    }

    public boolean isMonochrome () { return pattern.length == 1; }

    public ChatColor next () {
        if (pattern.length - index == 1) // Final element? Back to the beginning with you!
            index = -1;
        return pattern[++index];
    }

    public static ColorPattern fromString (String value) {
        char[] values = value.toLowerCase().toCharArray();
        ChatColor[] result = new ChatColor[values.length];

        for (int i = 0; i < values.length; ++i) {
            result[i] = Character.toString(values[i]).matches("[0-9a-fk-or]") ?
                    ChatColor.getByChar(values[i]) : ChatColor.WHITE;
        }

        return new ColorPattern(result);
    }


}