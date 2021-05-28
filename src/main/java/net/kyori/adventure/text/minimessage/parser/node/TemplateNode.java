package net.kyori.adventure.text.minimessage.parser.node;

import net.kyori.adventure.text.minimessage.parser.Token;

public final class TemplateNode extends ComponentNode {

  /**
   * Creates a new element node.
   *
   * @param parent        the parent of this node
   * @param token         the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  public TemplateNode(final ElementNode parent, final Token token, final String sourceMessage) {
    super(parent, token, sourceMessage);
  }

  @Override
  String componentName() {
    return "TemplateNode";
  }
}
