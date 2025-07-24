package net.kyori.adventure.dfu.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;

public class ComponentTypes {
  public static final ComponentType<TextComponent> TEXT = TextType.INSTANCE;
  public static final ComponentType<TranslatableComponent> TRANSLATABLE = TranslatableType.INSTANCE;
  public static final ComponentType<KeybindComponent> KEYBIND = KeybindType.INSTANCE;
  public static final ComponentType<ScoreComponent> SCORE = ScoreType.INSTANCE;
  public static final ComponentType<SelectorComponent> SELECTOR = SelectorType.INSTANCE;
  public static final ComponentType<NBTComponent> TYPE = NBTComponentType.INSTANCE;

  public static final ComponentType<?>[] ALL = new ComponentType[]{TEXT, TRANSLATABLE, KEYBIND, SCORE, SELECTOR, TYPE};

  public static ComponentType<?> from(Component component) {
    if (component instanceof TextComponent) {
      return TEXT;
    } else if (component instanceof TranslatableComponent) {
      return TRANSLATABLE;
    } else if (component instanceof KeybindComponent) {
      return KEYBIND;
    } else if (component instanceof ScoreComponent) {
      return SCORE;
    } else if (component instanceof SelectorComponent) {
      return SELECTOR;
    } else if (component instanceof NBTComponent) {
      return TYPE;
    } else {
      return null;
    }
  }
}
