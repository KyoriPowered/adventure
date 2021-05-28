package net.kyori.adventure.text.minimessage.parser.node;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.minimessage.ParseException;
import net.kyori.adventure.text.minimessage.parser.Token;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class ComponentNode extends ElementNode {

  private final List<TagPart> parts;

  /**
   * Creates a new element node.
   *
   * @param parent        the parent of this node
   * @param token         the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  ComponentNode(final ElementNode parent, final Token token, final String sourceMessage) {
    super(parent, token, sourceMessage);
    this.parts = genParts(token, sourceMessage);
  }

  private static List<TagPart> genParts(final Token token, final String sourceMessage) {
    final ArrayList<TagPart> parts = new ArrayList<>();

    if(token.childTokens() != null) {
      for(final Token childToken : token.childTokens()) {
        parts.add(new TagPart(sourceMessage, childToken));
      }
    }

    return parts;
  }

  abstract String componentName();

  /**
   * Returns the parts of this tag.
   *
   * @return the parts
   * @since 4.2.0
   */
  public List<TagPart> parts() {
    return this.parts;
  }

  /**
   * Returns the name of this tag.
   *
   * @return the name
   * @since 4.2.0
   */
  public String name() {
    if(this.parts.isEmpty()) {
      throw new ParseException("Tag has no parts? " + this);
    }
    return this.parts.get(0).value();
  }

  /**
   * Serializes this node to a string.
   *
   * @param sb the string builder to serialize into
   * @param indent the current indent level
   * @return the passed string builder, for chaining
   * @since 4.2.0
   */
  public @NonNull StringBuilder buildToString(final @NonNull StringBuilder sb, final int indent) {
    final char[] in = this.ident(indent);
    sb.append(in).append(this.componentName()).append("(");

    final int size = this.parts.size();
    for(int i = 0; i < size; i++) {
      final TagPart part = this.parts.get(i);
      sb.append('\'').append(part.value()).append('\'');
      if(i != size - 1) {
        sb.append(", ");
      }
    }

    sb.append(") {\n");

    for(final ElementNode child : this.children()) {
      child.buildToString(sb, indent + 1);
    }
    sb.append(in).append("}\n");
    return sb;
  }
}
