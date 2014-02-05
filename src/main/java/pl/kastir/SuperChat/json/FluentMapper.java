package pl.kastir.SuperChat.json;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;

import pl.kastir.SuperChat.json.node.ElementNode;
import pl.kastir.SuperChat.json.node.Node;
import pl.kastir.SuperChat.json.node.StringNode;

/**
 * A fluent element mapper that relies on code, not text.
 *
 * @author afistofirony
 */
public class FluentMapper extends Mapper {

    private Node           latest;
    ArrayList<ElementNode> latestModifiers;
    ArrayList<Tag>         persist;

    public FluentMapper(String start) {
        latest = new StringNode(start, index++);
        latestModifiers = new ArrayList<ElementNode>();
        persist = new ArrayList<Tag>();
    }

    public FluentMapper then(String next) {
        elements.addAll(latestModifiers);
        elements.add(latest);
        elements.addAll(calculateClosers());

        removeSelectively();
        persist.clear();

        latest = new StringNode(next, index++);
        return this;
    }

    private ArrayList<ElementNode> calculateClosers() {
        ArrayList<ElementNode> result = new ArrayList<ElementNode>();
        for (ElementNode node : this.latestModifiers) {
            if (node.isOpening() && !persist.contains(node.getType())) result.add(new ElementNode(node.getType(), null, node.getID(), true));
        }

        return result;
    }

    private void removeSelectively() {
        for (ElementNode node : this.latestModifiers)
            if (!persist.contains(node.getType())) latestModifiers.remove(node);
    }

    private FluentMapper replace(ElementNode value) {
        for (ElementNode node : this.latestModifiers) {
            if (node.getType() == value.getType()) {
                latestModifiers.remove(node);
                latestModifiers.add(value.setID(node.getID()));
                return this;
            }
        }

        latestModifiers.add(value);
        return this;
    }

    private boolean contains(Tag type) {
        return this.get(type) != null;
    }

    private ElementNode get(Tag type) {
        for (ElementNode node : this.latestModifiers)
            if (node.getType() == type) return node;

        return null;
    }

    // US English alias for colour.
    public FluentMapper color(ChatColor... colours) {
        return this.colour(colours);
    }

    public FluentMapper colour(ChatColor... colours) {
        String value = "";
        for (ChatColor colour : colours)
            value += colour.getChar();
        return this.replace(new ElementNode(Tag.COLOUR, value, index++));
    }

    public FluentMapper style(ChatColor... styles) {
        ArrayList<ChatColor> invalidTypes = new ArrayList<ChatColor>();
        for (ChatColor colour : styles) {
            if (colour.isFormat()) {
                Tag type = Tag.RESET;
                switch (colour) {
                    case MAGIC:
                        type = Tag.MAGIC;
                        break;

                    case BOLD:
                        type = Tag.STRONG;
                        break;

                    case STRIKETHROUGH:
                        type = Tag.STRIKE;
                        break;

                    case UNDERLINE:
                        type = Tag.UNDERLINE;
                        break;

                    case ITALIC:
                        type = Tag.EMPHASIS;
                        break;
                    default:
                        break;
                }

                if (!this.contains(type)) this.latestModifiers.add(new ElementNode(type, null, index++));
            }
            else {
                invalidTypes.add(colour);
            }
        }

        if (!invalidTypes.isEmpty()) this.colour(invalidTypes.toArray(new ChatColor[invalidTypes.size()]));

        return this;
    }

    public FluentMapper persist(Tag... types) {
        persist.addAll(Arrays.asList(types));
        return this;
    }

    public FluentMapper tooltip(String... values) {
        if (values.length == 0) return this;

        String result = values[0];
        for (int i = 1; i < values.length; ++i)
            result += "\n" + values[i];

        return this.replace(new ElementNode(Tag.TIP, result, index++));
    }

    public FluentMapper prompt(String text) {
        return this.replace(new ElementNode(Tag.PROMPT, text, index++));
    }

    public FluentMapper command(String command) {
        return this.replace(new ElementNode(Tag.REF, command, index++));
    }

    public FluentMapper link(String link) {
        return this.replace(new ElementNode(Tag.HREF, link, index++));
    }

}