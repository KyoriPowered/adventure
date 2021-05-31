package net.kyori.adventure.text.minimessage.parser.node;

import net.kyori.adventure.text.minimessage.parser.Token;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a template replacement in a string.
 *
 * @since 4.2.0
 */
public class TemplateNode extends ValueNode {

  /**
   * Creates a new element node.
   *
   * @param parent        the parent of this node
   * @param token         the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  public TemplateNode(
    final @Nullable ElementNode parent,
    final @NonNull Token token,
    final @NonNull String sourceMessage,
    final @NonNull String actualValue
  ) {
    super(parent, token, sourceMessage, actualValue);
  }

  @Override
  String valueName() {
    return "TemplateNode";
  }
}
