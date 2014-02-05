package pl.kastir.SuperChat.json;

import java.util.ArrayList;

import pl.kastir.SuperChat.json.node.ElementNode;
import pl.kastir.SuperChat.json.node.StringNode;

/**
 * A simpler implementation of the element mapper, primarily used
 * by players to have nice chat messages.
 *
 * WARNING: This is far less reliable and functional than HypertextMapper.
 *
 * @author afistofirony
 */
public class MarkdownMapper extends Mapper {

    public MarkdownMapper (String map) {
        this(map, false);
    }

    public MarkdownMapper (String map, boolean colours) {

        String current = "";

        try {
            boolean tagAvailable = true;
            loop: for (int i = 0; i < map.length(); ++i) {
                switch (map.charAt(i)) {
                    case '*': // Italic / bold
                        if (tagAvailable) { // Element opener?
                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            /* Justification for map.length() - i > 1;
                             * Say map is 7 characters and i is 6 (last char)
                             * map.length() = 7
                             * 7 - 6 = 1, therefore no subsequent character exists.
                             */
                            boolean bold = (map.length() - i > 1) && (map.charAt(i + 1) == '*');
                            elements.add(new ElementNode(bold ? Tag.STRONG : Tag.EMPHASIS, null, index++));

                            if (bold) // Skip next character if it's bold.
                                ++i;
                      } else {
                            Tag closing = null;
                            switch (map.length() - i) {
                                case 1: // '*' only
                                    closing = Tag.EMPHASIS;
                                    break;

                                case 2: // '**' or '* '
                                    if (map.charAt(i + 1) == '*' ||
                                            Character.toString(map.charAt(i + 1)).matches("[*-_ ]"))
                                        closing = map.charAt(i + 1) == '*' ? Tag.STRONG : Tag.EMPHASIS;
                                    break;

                                default: // anything else
                                    if ((map.charAt(i + 1) == '*' &&
                                            Character.toString(map.charAt(i + 2)).matches("[*-_ ]"))
                                            || Character.toString(map.charAt(i + 1)).matches("[*-_ ]"))
                                        closing = map.charAt(i + 1) == '*' ? Tag.STRONG : Tag.EMPHASIS;
                            }

                            if (closing == null)
                                continue;

                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            ElementNode result = findMatch(Tag.STRIKE);

                            if (result != null) {
                                elements.add(new ElementNode(closing, null, result.getID(), true));
                            }

                            if (closing == Tag.STRONG)
                                ++i;
                        }

                        break;

                    case '-': // Strike-through
                        if (tagAvailable) {
                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            elements.add(new ElementNode(Tag.STRIKE, null, index++));
                      } else if ((map.length() - i == 1) ||
                                Character.toString(map.charAt(i + 1)).matches("[*-_ ]")) {
                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            ElementNode result = findMatch(Tag.STRIKE);

                            if (result != null) {
                                elements.add(new ElementNode(Tag.STRIKE, null, result.getID(), true));
                            }
                        }

                        break;

                    case '_': // Underline
                        if (tagAvailable) {
                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            elements.add(new ElementNode(Tag.UNDERLINE, null, index++));
                        } else if ((map.length() - i == 1) ||
                                Character.toString(map.charAt(i + 1)).matches("[*-_ ]")) {
                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            ElementNode result = findMatch(Tag.UNDERLINE);

                            if (result != null) {
                                elements.add(new ElementNode(Tag.UNDERLINE, null, result.getID(), true));
                            }
                        }

                        break;

                    case '&': // Colour codes
                        if (map.length() - i > 1) { // Is there even another character?
                            char code = map.charAt(i + 1);
                            if (!Character.toString(code).matches("(i?)[0-9a-fk-or]")) {
                                current += "&";
                                continue;
                            }

                            if (colours) {
                                if (!current.isEmpty())
                                    elements.add(new StringNode(current, index++));

                                current = "";

                                Tag used = Tag.COLOUR;
                                if (Character.toString(code).matches("[k-orK-OR]")) {
                                    switch (map.toLowerCase().charAt(i + 1)) {
                                        case 'k': used = Tag.MAGIC;
                                            break;

                                        case 'l': used = Tag.STRONG;
                                            break;

                                        case 'm': used = Tag.STRIKE;
                                            break;

                                        case 'n': used = Tag.UNDERLINE;
                                            break;

                                        case 'o': used = Tag.EMPHASIS;
                                            break;

                                        case 'r': used = Tag.RESET;
                                            break;
                                    }
                                }

                                switch (used) { // Thank god for different elements.
                                    case COLOUR:
                                        elements.add(new ElementNode(used, Character.toString(code), index++));
                                        break;

                                    case MAGIC:
                                    case STRONG:
                                    case STRIKE:
                                    case UNDERLINE:
                                    case EMPHASIS:
                                    case RESET:
                                        elements.add(new ElementNode(used, null, index++));
                                    default:
                                        break;
                                }

                                ++i;
                            }
                        }

                        break;

                    case '[': // Hyperlink
                        if (tagAvailable) { // This is essentially the only time we want to allow this
                            if (!current.isEmpty())
                                elements.add(new StringNode(current, index++));

                            current = "";

                            int j;
                            String reference = "", value = "";
                            loop2: for (j = i + 1; j < map.length(); ++j) {
                                if (map.charAt(j) == ']' && map.length() - j > 1) {
                                    if (map.charAt(j + 1) != '(') { // Not a link
                                        current += '[';
                                        ++i;
                                        continue loop;
                                  } else { // A link
                                        value = map.substring(i + 1, j);
                                        for (int k = j + 1; k < map.length(); ++k) {
                                            switch (map.charAt(k)) {
                                                case ')':
                                                    j = k;
                                                    break loop2;

                                                default:
                                                    reference += map.charAt(k);
                                            }
                                        }
                                    }
                                }
                            }

                            /* At this point, we still need to parse the text
                             * INSIDE the brackets, so we're going to do some
                             * nasty recursion (I guess?) :D <3
                             */

                            MarkdownMapper submap = new MarkdownMapper(value, colours);
                            int index = this.index++, index2 = this.index++; // this is so stupid
                            elements.add(new ElementNode(Tag.HREF, reference, index));
                            elements.add(new ElementNode(Tag.TIP, "\u00A76\u00A7o" + reference, index2));
                            elements.addAll(submap.getElements());
                            elements.add(new ElementNode(Tag.TIP, null, index2, true));
                            elements.add(new ElementNode(Tag.HREF, null, index, true));

                            i = j;
                        }

                        break;

                    case '\\': // Escape character
                        if (map.length() - i > 1) {
                            current += map.charAt(i + 1);
                            tagAvailable = map.charAt(i + 1) == ' ';
                        }

                        break;

                    case ' ': // Specifies the availability of a new
                        current += " ";
                        tagAvailable = true;
                        break;

                    default: // In a word
                        current += map.charAt(i);
                        if (tagAvailable)
                            tagAvailable = false;
                }
            }
      } catch (final ArrayIndexOutOfBoundsException ignored) {}

        if (!current.isEmpty())
            elements.add(new StringNode(current, index++));

    }

    private ElementNode findMatch (Tag closing) {
        ArrayList<Integer> blocked = new ArrayList<Integer>();
        for (int m = elements.size() - 1; m >= 0; --m) { // Find opening node.
            if (elements.get(m) instanceof ElementNode) {
                ElementNode node = (ElementNode) elements.get(m);
                if (node.getType() == closing && !blocked.contains(node.getID())) {
                    if (node.isOpening()) {
                        return node;
                    } else {
                        blocked.add(node.getID());
                    }
                }
            }
        }

        return null;
    }


}