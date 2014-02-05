package pl.kastir.SuperChat.json;

import java.util.ArrayList;

import pl.kastir.SuperChat.json.node.Node;

public abstract class Mapper {
    
    ArrayList<Node> elements;
    int             index;
    
    public Mapper() {
        elements = new ArrayList<Node>();
    }
    
    public ArrayList<Node> getElements() {
        return this.elements;
    }
    
}