package pl.kastir.SuperChat.json;

import org.bukkit.ChatColor;

import pl.kastir.SuperChat.json.node.ElementNode;
import pl.kastir.SuperChat.json.node.Node;
import pl.kastir.SuperChat.json.node.StringNode;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialMessage {
    
    ArrayList<Element> result;
    String             output;
    Mapper             mapper;
    
    public SpecialMessage(String input) {
        this(new HypertextMapper(input));
    }
    
    public SpecialMessage(Mapper mapper) {
        
        this.result = new ArrayList<Element>();
        this.output = "";
        this.mapper = mapper;
        
        ArrayList<Node> nodes = mapper.getElements();
        
        for (int i = 0; i < nodes.size(); ++i) {
            
            Node node = nodes.get(i);
            
            if (node instanceof StringNode) { // Is it a String node?
                Element add = new Element();
                
                HashMap<Tag, Node> tags = getOpenElements(nodes, i);
                add.setValue(node.getValue());
                
                if (tags.containsKey(Tag.REF)) add.setClickAction(Action.RUN_COMMAND, tags.get(Tag.REF).getValue());
                
                if (tags.containsKey(Tag.HREF)) add.setClickAction(Action.OPEN_URL, tags.get(Tag.HREF).getValue());
                
                if (tags.containsKey(Tag.TIP)) add.setHoverValue(tags.get(Tag.TIP).getValue());
                
                if (tags.containsKey(Tag.RAW)) add.setRaw(node.getValue());
                
                if (tags.containsKey(Tag.ITEMTIP)) add.setHoverValue(Action.SHOW_ITEM, ((Node) tags.get(Tag.ITEMTIP)).getValue().replaceAll("&quot", "\""));
                
                if (tags.containsKey(Tag.ACHIEVEMENTTIP)) add.setHoverValue(Action.SHOW_ACHIEVEMENT, ((Node) tags.get(Tag.ACHIEVEMENTTIP)).getValue());
                
                if (tags.containsKey(Tag.PROMPT)) add.setClickAction(Action.SUGGEST_COMMAND, tags.get(Tag.PROMPT).getValue());
                
                if (tags.containsKey(Tag.STRONG)) add.setBold(true);
                
                if (tags.containsKey(Tag.EMPHASIS)) add.setItalic(true);
                
                if (tags.containsKey(Tag.UNDERLINE)) add.setUnderline(true);
                
                if (tags.containsKey(Tag.STRIKE)) add.setStrike(true);
                
                if (tags.containsKey(Tag.COLOUR)) add.setColour(ColorPattern.fromString(tags.get(Tag.COLOUR).getValue()).pattern);
                
                if (tags.containsKey(Tag.RESET)) {
                    
                    if (tags.containsKey(Tag.STRONG)) add.setBold(tags.get(Tag.RESET).getID() < tags.get(Tag.STRONG).getID());
                    
                    if (tags.containsKey(Tag.EMPHASIS)) add.setItalic(tags.get(Tag.RESET).getID() < tags.get(Tag.EMPHASIS).getID());
                    
                    if (tags.containsKey(Tag.UNDERLINE)) add.setUnderline(tags.get(Tag.RESET).getID() < tags.get(Tag.UNDERLINE).getID());
                    
                    if (tags.containsKey(Tag.STRIKE)) add.setStrike(tags.get(Tag.RESET).getID() < tags.get(Tag.STRIKE).getID());
                    
                    if (tags.containsKey(Tag.MAGIC)) add.setMagic(tags.get(Tag.RESET).getID() < tags.get(Tag.MAGIC).getID());
                    
                    if (tags.containsKey(Tag.COLOUR)) add.setColour(tags.get(Tag.RESET).getID() < tags.get(Tag.COLOUR).getID() ? ColorPattern.fromString(tags.get(Tag.COLOUR).getValue()).pattern : new ChatColor[] { ChatColor.WHITE });
                }
                
                result.add(add);
            }
            
        }
        
    }
    
    public String toString() {
        String result = "";
        
        if (this.result.size() > 0) {
            result = ", extra:[";
            for (int i = 0; i < this.result.size(); ++i) {
                Element element = this.result.get(i);
                result += (i > 0 ? ", " : "") + element.toString(true);
            }
            result += "]";
        }
        
        if (this.result.size() < 1) return "{}";
        
        return "{" + "text: ''" + result + "}";
    }
    
    public String toString2() {
        String result = "";
        
            for (int i = 0; i < this.result.size(); ++i) {
                Element element = this.result.get(i);
                result += (i > 0 ? ", " : "") + element.toString(true);
            }
        
        if (this.result.size() < 1) return "{ text: '' }";
        
        return result;
    }
    
    public String humanReadable(boolean format) {
        StringBuilder result = new StringBuilder();
        for (Element element : this.result) {
            String parsed = element.text;
            if (format && element.colour != null && !element.colour.isMonochrome()) parsed = element.colour.patterned(parsed);
            
            if (format && element.bold) parsed = "\\\\u00A7l" + parsed;
            
            if (format && element.strike) parsed = "\\\\u00A7l" + parsed;
            
            if (format && element.italic) parsed = "\\\\u00A7l" + parsed;
            
            if (format && element.underline) parsed = "\\\\u00A7n" + parsed;
            
            if (format && element.magic) parsed = "\\\\u00A7k" + parsed;
            
            if (format && element.colour != null && element.colour.isMonochrome()) parsed = "\\\\u00A7" + element.colour.pattern[0].getChar() + parsed;
            
            result.append(parsed);
        }
        
        return result.toString();
    }
    
    private HashMap<Tag, Node> getOpenElements(ArrayList<Node> nodes, int backFrom) {
        ArrayList<Integer> closedTags = new ArrayList<Integer>();
        HashMap<Tag, Node> result = new HashMap<Tag, Node>();
        
        if (backFrom >= nodes.size()) backFrom = nodes.size() - 1;
        
        for (int i = backFrom; i >= 0; --i) {
            if (nodes.get(i) instanceof ElementNode) {
                ElementNode node = (ElementNode) nodes.get(i);
                if (result.containsKey(node.getType())) continue;
                
                if (!closedTags.contains(node.getID())) {
                    if (node.isOpening()) {
                        result.put(node.getType(), node);
                    }
                    else {
                        closedTags.add(node.getID());
                    }
                }
            }
        }
        
        return result;
    }
    
}