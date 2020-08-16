package net.kyori.adventure.text.serializer.console;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.fusesource.jansi.Ansi;

import java.util.HashMap;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 16/08/2020.
 */
public enum Formats {
  BLACK(NamedTextColor.BLACK,Ansi.ansi().fg(Ansi.Color.BLACK).boldOff(), false, "black"),
  DARK_BLUE(NamedTextColor.DARK_BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).boldOff(), false, "darkblue"),
  DARK_GREEN(NamedTextColor.DARK_GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).boldOff(), false, "darkgreen"),
  DARK_AQUA(NamedTextColor.DARK_AQUA, Ansi.ansi().fg(Ansi.Color.CYAN).boldOff(), false, "darkaqua", "darkcyan"),
  DARK_RED(NamedTextColor.DARK_RED, Ansi.ansi().fg(Ansi.Color.RED).boldOff(), false, "darkred"),
  DARK_PURPLE(NamedTextColor.DARK_PURPLE, Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff(), false, "darkpurple", "darkmagenta"),
  GOLD(NamedTextColor.GOLD, Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff(), false, "gold", "orange", "darkyellow"),
  GRAY(NamedTextColor.GRAY,Ansi.ansi().fg(Ansi.Color.WHITE).boldOff(), false, "gray", "grey", "silver"),
  DARK_GRAY(NamedTextColor.DARK_GRAY, Ansi.ansi().fg(Ansi.Color.BLACK).bold(), false, "darkgray", "darkgrey"),
  BLUE(NamedTextColor.BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).bold(), false, "blue"),
  GREEN(NamedTextColor.GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).bold(), false, "green", "lime"),
  AQUA(NamedTextColor.AQUA, Ansi.ansi().fg(Ansi.Color.CYAN).bold(), false, "aqua", "cyan"),
  RED(NamedTextColor.RED, Ansi.ansi().fg(Ansi.Color.RED).bold(), false, "red"),
  LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE, Ansi.ansi().fg(Ansi.Color.MAGENTA).bold(), false, "purple", "magenta"),
  YELLOW(NamedTextColor.YELLOW, Ansi.ansi().fg(Ansi.Color.YELLOW).bold(), false, "yellow"),
  WHITE(NamedTextColor.WHITE, Ansi.ansi().fg(Ansi.Color.WHITE).bold(), false, "white"),

  MAGIC(TextDecoration.OBFUSCATED, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW), true, "magic"),
  BOLD(TextDecoration.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE), true, "bold"),
  STRIKETHROUGH(TextDecoration.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON), true, "strikethrough", "strike"),
  UNDERLINE(TextDecoration.UNDERLINED, Ansi.ansi().a(Ansi.Attribute.UNDERLINE), true, "underline"),
  ITALIC(TextDecoration.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC), true, "italic"),
  RESET(ConsoleComponentSerializerImpl.Reset.INSTANCE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT), false, "reset");

  static final Ansi SOFT_RESET = Ansi.ansi().a(Ansi.Attribute.RESET);

  private static final HashMap<TextFormat, Formats> mMap = new HashMap<>();
  private static final HashMap<String, Formats> mNameMap = new HashMap<>();

  private final TextFormat format;
  private final Ansi escapeString;
  private final String[] mNames;
  private final boolean mIsFormat;

  static {
    for (Formats format : values()) {
      mMap.put(format.getTextFormat(), format);
      for (String name : format.mNames) {
        mNameMap.put(name, format);
      }
    }
  }

  Formats(TextFormat c, Ansi ansi, boolean isFormat, String... names) {
    format = c;
    escapeString = ansi;
    mNames = names;
    mIsFormat = isFormat;
  }
  public TextFormat getTextFormat() {
    return format;
  }

  public Ansi getAnsi() {
    return escapeString;
  }

  public boolean isFormat() {
    return mIsFormat;
  }

  @Override
  public String toString() {
    return escapeString.toString();
  }

  public static Formats getByFormat(TextFormat c) {
    return mMap.get(c);
  }

  public static Formats getByName(String name) {
    return mNameMap.get(name.toLowerCase());
  }

}
