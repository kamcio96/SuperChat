package pl.kastir.SuperChat.json;

public enum Tag {
    REF ("ref", "command", "run_command"),
    HREF ("href", "link", "open_url"),
    TIP ("tip", "tooltip", "show_text"),
    PROMPT ("prompt", "insert", "suggest_command"),
    STRONG ("strong", "b", "bold"),
    EMPHASIS ("em", "i", "emphasis", "italic"),
    UNDERLINE ("u", "underline"),
    STRIKE ("s", "strike", "strikethrough"),
    MAGIC ("m", "magic", "random", "?"),
    RESET ("r", "reset"),
    ITEMTIP("itemtip", "show_item"), 
    ACHIEVEMENTTIP("achievementtip", "show_achievement"), 
    RAW("raw"), 
    COLOUR ("colour", "color", "pattern");


    final String[] aliases;

    Tag(String... aliases) {
        this.aliases = aliases;
    }

    /**
     * Looks for the given value.
     * @param value The value to match.
     * @return The matching value, or null if not found
     */
    static Tag lookup (String value) {
        for (Tag tag : values())
            for (String name : tag.aliases)
                if (tag.name().equalsIgnoreCase(value) || name.equalsIgnoreCase(value))
                    return tag;

        return null;
    }

    /**
     * @return Whether or not this tag has a value.
     */
    boolean sansValue () {
        switch (this) {
            case STRONG:
            case EMPHASIS:
            case STRIKE:
                return true;

            default:
                return false;
        }
    }

}