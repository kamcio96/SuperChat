package pl.kastir.SuperChat.json;

import java.util.ArrayList;

import pl.kastir.SuperChat.json.node.ElementNode;
import pl.kastir.SuperChat.json.node.StringNode;

/**
 * Okay, so this is an essential part of the SpecialMessage stuff.
 *
 * This class will lex the String given into a list of Nodes.
 * That can then be used to construct Elements.
 *
 * Here's a basic example:
 * Let's <b> make an <i>example</b> by creating stacked tags</i>.
 *
 * This class will convert that to something like this:
 * STRING-0: "Let's "
 * STRONG-0: OPEN, null  // It has no value
 * STRING-1: "make an "
 * EMPHASIS-0: OPEN, null
 * STRING-2: "example "
 * STRONG-0: CLOSE
 * STRING-3: "by creating stacked tags"
 * EMPHASIS-0: CLOSE
 * STRING-4: "."
 *
 * That is exponentially easier to process than a bunch of characters.
 */
public class HypertextMapper extends Mapper {

    public HypertextMapper(String map) {

        String current = "";

        try {
            for (int i = 0; i < map.length(); ++i) {
                switch (map.charAt(i)) {
                    case '<': // Oh dear, a new tag

                        // First things first, wrap this element up.
                        if (!current.isEmpty())
                            elements.add(new StringNode(current, index++));

                        current = "";
                        // The one time postfix-increments are actually useful!

                        int j, k;
                        for (j = i + 1; j < map.length(); ++j) {
                            if (map.charAt(j) == '=' || map.charAt(j) == '>')
                                break;
                        }

                        String value = null;

                        if (map.charAt(j) == '=') { // Tag has value? Nice!
                            char closer = map.charAt(j + 1);
                            for (k = j + 2; k < map.length() &&
                                    !(map.charAt(k) == closer && (map.charAt(k - 1) == '\\'
                                            || (map.length() - k > 1 && map.charAt(k + 1) == '>'))); ++k)
                                if (map.charAt(k) == '\\')
                                    ++k;
                            value = map.substring(j + 2, k++);
                      } else k = j;



                        boolean close = map.substring(i + 1).charAt(0) == '/';

                        Tag tag = Tag.lookup(map.substring(i + (close ? 2 : 1), j));

                        if (tag == null) {
                            i = j;
                            continue;
                        }
//
//                        if (tag == Tag.PROMPT)
//                            value = ChatColor.translateAlternateColorCodes('$', value)
//                                    .replaceAll("\u00A7", "\\\\u00A7");

                        if (close) {
                            int m;
                            ElementNode result = null;
                            ArrayList<Integer> blocked = new ArrayList<Integer>();
                            for (m = elements.size() - 1; m >= 0; --m) { // Find the opening node.
                                if (elements.get(m) instanceof ElementNode) {
                                    ElementNode node = (ElementNode) elements.get(m);
                                    if (node.getType() == tag && !blocked.contains(node.getID())) {
                                        if (node.isOpening()) { // We only want opening nodes!
                                            result = node;
                                            break;
                                      } else { // Do not allow this node's partner to be considered.
                                            blocked.add(node.getID());
                                        }
                                    }
                                }
                            }

                            if (result != null) {
                                elements.add(new ElementNode(tag, value, result.getID(), true));
                            }
                      } else {
                            elements.add(new ElementNode(tag, value, index++));
                        }

                        i = k;

                        break;

                    case '$': // Colour code needs its own element so we can refer back to it.
                        if (map.length() - i > 1) {
                            char code = map.charAt(i + 1);

                            if (code == ' ')
                                continue;

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

                        break;

                    case '\\':
                        if (map.length() - i > 1)
                            current += map.charAt(++i);
                        break;

                    default:
                        current += map.charAt(i);
                }
            }
      } catch (final ArrayIndexOutOfBoundsException ignored) {}

        if (!current.isEmpty())
            elements.add(new StringNode(current, index++));

    }

}