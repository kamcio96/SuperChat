package pl.kastir.SuperChat.json;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * Represents one of the elements in a compound JSON message.
 * 
 * BASE JSON ELEMENT (class based from this) { text: "Text", color: "Value"
 * clickEvent: { action: suggest_command, value: "You should say this." },
 * 
 * hoverEvent: { action: show_text, value:
 * "Click me to insert text into the chat." }
 * 
 * }
 * 
 * 
 * @author afistofirony
 */
public class Element {
    
    Action          click, hover;
    String          clickValue, hoverValue;
    String          text;
    ColorPattern    colour;
    
    boolean         bold, strike, underline, italic, magic;
    private boolean isRaw = false;
    private String  rawValue;
    
    public Element setClickAction(Action click, String value) {
        this.click = click;
        this.clickValue = StringEscapeUtils.unescapeHtml(value);
        return this;
    }
    
    public Element setHoverValue(String value) {
        this.hoverValue = StringEscapeUtils.unescapeHtml(value);
        hover = Action.SHOW_TEXT;
        hoverValue = colorIt(hoverValue);
        return this;
    }
    
    public Element setHoverValue(Action hover, String value) {
        this.hover = hover;
        this.hoverValue = StringEscapeUtils.unescapeHtml(value);
        if (hover == Action.SHOW_TEXT) hoverValue = colorIt(hoverValue);
        return this;
    }
    
    public Element setValue(String text) {
        this.text = StringEscapeUtils.unescapeHtml(text);
        return this;
    }
    
    public Element setColour(ChatColor... colours) {
        return this.setColor(colours);
    }
    
    public Element setColor(ChatColor... colours) {
        this.colour = new ColorPattern(colours);
        return this;
    }
    
    public Element setBold(boolean bold) {
        this.bold = bold;
        return this;
    }
    
    public Element setStrike(boolean strike) {
        this.strike = strike;
        return this;
    }
    
    public Element setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }
    
    public Element setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }
    
    public Element setMagic(boolean magic) {
        this.magic = magic;
        return this;
    }
    
    public String toString(boolean brackets) {
        if (isRaw) return rawValue;
        String result = brackets ? "{" : "";
        ArrayList<String> elements = new ArrayList<String>();
        if (text != null && !text.isEmpty()) {
            if (colour != null) {
                if (colour.pattern.length < 2) {
                    elements.add("text: '" + text.replaceAll("'", "\\\\'") + "'");
                    elements.add("color: " + colour.pattern[0].name().toLowerCase());
                }
                else {
                    elements.add("text: '" + colour.patterned(text.replaceAll("'", "\\\\'")) + "'");
                }
            }
            else {
                elements.add("text: '" + text.replaceAll("'", "\\\\'") + "'");
            }
            
            if (bold) elements.add("bold: true");
            
            if (italic) elements.add("italic: true");
            
            if (underline) elements.add("underlined: true");
            
            if (strike) elements.add("strikethrough: true");
            
            if (magic) elements.add("obfuscated: true");
        }
        
        if (click != null) elements.add("clickEvent:{action:" + click + "," + " value:'" + clickValue.replaceAll("'", "\\\\'") + "'}");
        
        if (hoverValue != null && !hoverValue.isEmpty()) {
            elements.add("hoverEvent:{action:" + this.hover + ", value:'" + this.hoverValue.replaceAll("'", "\\\\'") + "'}");
        }
        boolean first = true;
        StringBuilder builder = new StringBuilder(result);
        for (String element : elements) {
            if (!first) builder.append(", ");
            builder.append(element);
            first = false;
        }
        
        return (builder + (brackets ? "}" : "")).replaceAll("\u00A7", "\\\\u00A7");
    }
    
    public void setRaw(String value) {
        isRaw = true;
        rawValue = StringEscapeUtils.unescapeHtml(value);
    }
    
    private String colorIt(String string) {
        for (ChatColor color : ChatColor.values()) {
            string = string.replaceAll("\\$" + color.getChar(), color.toString());
        }
        return string;
    }
}