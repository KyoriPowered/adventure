package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.ansi.ANSIComponentRenderer;
import net.kyori.ansi.ColorLevel;
import net.kyori.ansi.StyleOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class ANSIComponentSerializerImpl implements ANSIComponentSerializer {
  static final ANSIComponentSerializer INSTANCE = new ANSIComponentSerializerImpl();

  @Override
  public @NotNull String serialize(@NotNull Component component) {
    ANSIComponentRenderer.ToString<Style> renderer = ANSIComponentRenderer.toString(ComponentStyleOps.INSTANCE);
    ComponentFlattener.basic().flatten(component, new ANSIFlattenerListener(renderer));
    renderer.complete();
    return renderer.asString();
  }

  @Override
  public @NotNull String serialize(@NotNull Component component, @NotNull ColorLevel colorLevel) {
    ANSIComponentRenderer.ToString<Style> renderer = ANSIComponentRenderer.toString(ComponentStyleOps.INSTANCE, colorLevel);
    ComponentFlattener.basic().flatten(component, new ANSIFlattenerListener(renderer));
    renderer.complete();
    return renderer.asString();
  }

  static StyleOps.State mapState(TextDecoration.State state) {
    switch (state) {
      case NOT_SET:
        return StyleOps.State.UNSET;
      case FALSE:
        return StyleOps.State.FALSE;
      case TRUE:
        return StyleOps.State.TRUE;
    }
    throw new IllegalStateException("Decoration state is not valid");
  }

  static class ComponentStyleOps implements StyleOps<Style> {
    static final ComponentStyleOps INSTANCE = new ComponentStyleOps();

    @Override
    public State bold(@NotNull Style style) {
      return mapState(style.decoration(TextDecoration.BOLD));
    }

    @Override
    public State italics(@NotNull Style style) {
      return mapState(style.decoration(TextDecoration.ITALIC));
    }

    @Override
    public State underlined(@NotNull Style style) {
      return mapState(style.decoration(TextDecoration.UNDERLINED));
    }

    @Override
    public State strikethrough(@NotNull Style style) {
      return mapState(style.decoration(TextDecoration.STRIKETHROUGH));
    }

    @Override
    public State obfuscated(@NotNull Style style) {
      return mapState(style.decoration(TextDecoration.OBFUSCATED));
    }

    @Override
    public @Range(from = -1L, to = 16777215L) int color(@NotNull Style style) {
      final TextColor color = style.color();
      return color == null ? -1 : color.value();
    }

    @Override
    public @Nullable String font(@NotNull Style style) {
      final Key font = style.font();
      return font == null ? null : font.asString();
    }
  }

  static class ANSIFlattenerListener implements FlattenerListener {
    private final ANSIComponentRenderer<Style> renderer;

    ANSIFlattenerListener(ANSIComponentRenderer<Style> renderer) {
      this.renderer = renderer;
    }

    @Override
    public void pushStyle(@NotNull Style style) {
      renderer.pushStyle(style);
    }

    @Override
    public void component(@NotNull String text) {
      renderer.text(text);
    }

    @Override
    public void popStyle(@NotNull Style style) {
      renderer.popStyle(style);
    }
  }
}

