package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;

public interface Modifying {

  void visit(ElementNode curr);

  Component apply(Component curr, Component parent);
}
