package net.kyori.adventure.text.minimessage.parser.node;

import java.util.Objects;
import net.kyori.adventure.text.minimessage.parser.Token;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a template replacement in a string.
 *
 * @since 4.2.0
 */
public class TemplateNode extends ElementNode {

  private final @NonNull String value;

  /**
   * Creates a new element node.
   *
   * @param parent        the parent of this node
   * @param token         the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  public TemplateNode(final @Nullable ElementNode parent, final @NonNull Token token, final @NonNull String sourceMessage, final @NonNull String actualValue) {
    super(parent, token, sourceMessage);
    this.value = actualValue;
  }

  /**
   * Returns the value of this template node.
   *
   * @return the value
   * @since 4.2.0
   */
  public @NonNull String value() {
    return this.value;
  }

  @Override
  public @NonNull Token token() {
    return Objects.requireNonNull(super.token(), "token is not set");
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
    sb.append(in).append("TemplateNode('").append(this.value).append("')\n");
    return sb;
  }
}
