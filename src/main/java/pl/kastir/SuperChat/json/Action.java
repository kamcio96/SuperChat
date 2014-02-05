package pl.kastir.SuperChat.json;

public enum Action {
    OPEN_URL (Type.CLICK),
    RUN_COMMAND (Type.CLICK),
    SHOW_TEXT (Type.HOVER),
    SHOW_ITEM(Type.HOVER), 
    SHOW_ACHIEVEMENT(Type.HOVER), 
    SUGGEST_COMMAND (Type.CLICK);

    final Type type;
    Action (Type type) {
        this.type = type;
    }

    public boolean isClickable () {
        return this.type == Type.CLICK;
    }

    public String toString () {
        return name().toLowerCase();
    }

    private enum Type { CLICK, HOVER }

}
