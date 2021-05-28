package net.kyori.adventure.text.minimessage.parser.node;

public final class RootNode extends ElementNode {

  /**
   * Creates a new root node.
   *
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  public RootNode(final String sourceMessage) {
    super(null, null, sourceMessage);
  }
}
