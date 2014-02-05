package pl.kastir.SuperChat.json.node;

import pl.kastir.SuperChat.json.Tag;

public class ElementNode extends Node {
    
    Tag     tag;
    boolean close;
    
    public ElementNode(Tag tag, String value, int ID) {
        this(tag, value, ID, false);
    }
    
    public ElementNode(Tag tag, String value, int ID, boolean closing) {
        super(value, ID);
        this.tag = tag;
        this.close = closing;
    }
    
    public boolean isOpening() {
        return !this.close;
    }
    
    public Tag getType() {
        return this.tag;
    }
    
    public ElementNode setID(int id) {
        return (ElementNode) super.setID(id);
    }
    
    public String toString() {
        switch (tag) {
            case STRONG:
                return "\u00A7l";
            case EMPHASIS:
                return "\u00A7o";
            case UNDERLINE:
                return "\u00A7n";
            case STRIKE:
                return "\u00A7m";
            case COLOUR:
                return "\u00A7" + value.charAt(0);
            case MAGIC:
                return "\u00A7k";
            case RESET:
                return "\u00A7r";
                
            default:
                return null;
        }
    }
    
}