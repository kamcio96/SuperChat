package pl.kastir.SuperChat.json.node;

public class Node {

    String value;
    int index;

    public Node (String value, int ID) {
        this.value = value;
        this.index = ID;
    }

    public int getID () { return index; }

    public String getValue () { return value; }

    public Node setID (int id) {
        this.index = id;
        return this;
    }

}