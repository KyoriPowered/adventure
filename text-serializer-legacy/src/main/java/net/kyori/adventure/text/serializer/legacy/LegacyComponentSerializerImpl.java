/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/* package */ class LegacyComponentSerializerImpl implements LegacyComponentSerializer {
  private static final Pattern URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private static final String LEGACY_CHARS = "0123456789abcdefklmnor";
  private static final char LEGACY_BUNGEE_HEX_CHAR = 'x';
  private static final List<TextFormat> FORMATS;

  static {
    final List<TextFormat> formats = new ArrayList<>();
    formats.addAll(NamedTextColor.values());
    Collections.addAll(formats, DECORATIONS);
    formats.add(Reset.INSTANCE);
    FORMATS = Collections.unmodifiableList(formats);

    // assert same length
    if(FORMATS.size() != LEGACY_CHARS.length()) {
      throw new IllegalStateException("FORMATS length differs from LEGACY_CHARS length");
    }
  }

  static final LegacyComponentSerializer SECTION_SERIALIZER = new LegacyComponentSerializerImpl(SECTION_CHAR, HEX_CHAR, null, false, true, false);
  static final LegacyComponentSerializer AMPERSAND_SERIALIZER = new LegacyComponentSerializerImpl(AMPERSAND_CHAR, HEX_CHAR, null, false, true, false);

  private final char character;
  private final char hexCharacter;
  private final Style urlStyle;
  private final boolean urlLink;
  private final boolean colorDownsample;
  private final boolean useTerriblyStupidHexFormat; // (╯°□°)╯︵ ┻━┻

  LegacyComponentSerializerImpl(final char character, final char hexCharacter, final @Nullable Style urlStyle, final boolean urlLink, final boolean colorDownsample, final boolean useTerriblyStupidHexFormat) {
    this.character = character;
    this.hexCharacter = hexCharacter;
    this.urlStyle = urlStyle;
    this.urlLink = urlLink;
    this.colorDownsample = colorDownsample;
    this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
  }

  private @Nullable FormatCodeType determineFormatType(final char legacy, final String input, final int pos) {
    if(pos >= 14) {
      // The BungeeCord RGB color format uses a repeating sequence of RGB values, each character formatted
      // as their own color format string, and to make things interesting, all the colors are also valid
      // Mojang colors. To differentiate this, we do a lookback check for &x (or equivalent) for its position
      // in the string if it is indeed a BungeeCord-style RGB color.
      final int expectedCharacterPosition = pos - 14;
      final int expectedIndicatorPosition = pos - 13;
      if(input.charAt(expectedCharacterPosition) == this.character && input.charAt(expectedIndicatorPosition) == LEGACY_BUNGEE_HEX_CHAR) {
        return FormatCodeType.BUNGEECORD_UNUSUAL_HEX;
      }
    }
    if(legacy == this.hexCharacter) {
      return FormatCodeType.KYORI_HEX;
    } else if(LEGACY_CHARS.indexOf(legacy) != -1) {
      return FormatCodeType.MOJANG_LEGACY;
    }
    return null;
  }

  private @Nullable DecodedFormat decodeTextFormat(final char legacy, final String input, final int pos) {
    final FormatCodeType foundFormat = this.determineFormatType(legacy, input, pos);
    if(foundFormat == null) {
      return null;
    }
    switch(foundFormat) {
      case BUNGEECORD_UNUSUAL_HEX:
        final StringBuilder foundHex = new StringBuilder();
        for(int i = pos - 1; i >= pos - 11; i -= 2) {
          foundHex.append(input.charAt(i));
        }
        foundHex.append('#');
        return new DecodedFormat(foundFormat, TextColor.fromHexString(foundHex.reverse().toString()));
      case KYORI_HEX:
        return new DecodedFormat(foundFormat, TextColor.fromHexString('#' + input.substring(pos, pos + 6)));
      case MOJANG_LEGACY:
        return new DecodedFormat(foundFormat, FORMATS.get(LEGACY_CHARS.indexOf(legacy)));
      default:
        return null;
    }
  }

  private static boolean isHexTextColor(final TextFormat format) {
    return format instanceof TextColor && !(format instanceof NamedTextColor);
  }

  private String toLegacyCode(TextFormat format) {
    if(isHexTextColor(format)) {
      final TextColor color = (TextColor) format;
      if(this.colorDownsample) {
        format = NamedTextColor.nearestTo(color);
      } else {
        final String hex = String.format("%06x", color.value());
        if(this.useTerriblyStupidHexFormat) {
          // ah yes, wonderful. A 14 digit long completely unreadable string.
          final StringBuilder legacy = new StringBuilder(String.valueOf(LEGACY_BUNGEE_HEX_CHAR));
          for(final char c : hex.toCharArray()) {
            legacy.append(this.character).append(c);
          }
          return legacy.toString();
        } else {
          // this is a bit nicer, hey?
          return this.hexCharacter + hex;
        }
      }
    }
    final int index = FORMATS.indexOf(format);
    return Character.toString(LEGACY_CHARS.charAt(index));
  }

  private TextComponent extractUrl(final TextComponent component) {
    return !this.urlLink ? component : component.replace(URL_PATTERN, url ->
      (this.urlStyle == null ? url : url.style(this.urlStyle)).clickEvent(ClickEvent.openUrl(url.content())));
  }

  @Override
  public @NonNull TextComponent deserialize(final @NonNull String input) {
    int next = input.lastIndexOf(this.character, input.length() - 2);
    if(next == -1) {
      return this.extractUrl(TextComponent.of(input));
    }

    final List<TextComponent> parts = new ArrayList<>();

    TextComponent.Builder current = null;
    boolean reset = false;

    int pos = input.length();
    do {
      final DecodedFormat decoded = this.decodeTextFormat(input.charAt(next + 1), input, next + 2);
      if(decoded != null) {
        final int from = next + (decoded.encodedFormat == FormatCodeType.KYORI_HEX ? 8 : 2);
        if(from != pos) {
          if(current != null) {
            if(reset) {
              parts.add(current.build());
              reset = false;
              current = TextComponent.builder();
            } else {
              current = TextComponent.builder().append(current.build());
            }
          } else {
            current = TextComponent.builder();
          }

          current.content(input.substring(from, pos));
        } else if(current == null) {
          current = TextComponent.builder();
        }

        if(!reset) {
          reset = applyFormat(current, decoded.format);
        }
        if(decoded.encodedFormat == FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
          // BungeeCord hex characters are a repeating set of characters, all of which are also valid
          // legacy Mojang chat colors. Subtract the number of characters in the format, and only then
          // skip ahead.
          next -= 12;
        }
        pos = next;
      }

      next = input.lastIndexOf(this.character, next - 1);
    } while(next != -1);

    if(current != null) {
      parts.add(current.build());
    }

    final String remaining = pos > 0 ? input.substring(0, pos) : "";
    if(parts.size() == 1 && remaining.isEmpty()) {
      return this.extractUrl(parts.get(0));
    } else {
      Collections.reverse(parts);
      return this.extractUrl(TextComponent.builder(remaining).append(parts).build());
    }
  }

  @Override
  public @NonNull String serialize(final @NonNull Component component) {
    final Cereal state = new Cereal(this.character);
    state.append(component);
    return state.toString();
  }

  private static boolean applyFormat(final TextComponent.@NonNull Builder builder, final @NonNull TextFormat format) {
    if(format instanceof TextColor) {
      builder.colorIfAbsent((TextColor) format);
      return true;
    } else if(format instanceof TextDecoration) {
      builder.decoration((TextDecoration) format, TextDecoration.State.TRUE);
      return false;
    } else if(format instanceof Reset) {
      return true;
    }
    throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  private enum Reset implements TextFormat {
    INSTANCE
  }

  // Are you hungry?
  private final class Cereal {
    private final StringBuilder sb = new StringBuilder();
    private final Style style = new Style();
    private final char character;

    Cereal(final char character) {
      this.character = character;
    }

    /* package */ void append(final @NonNull Component component) {
      this.append(component, new Style());
    }

    private void append(final @NonNull Component component, final @NonNull Style style) {
      style.apply(component);

      if(component instanceof TextComponent) {
        final String content = ((TextComponent) component).content();
        if(!content.isEmpty()) {
          style.applyFormat();
          this.sb.append(content);
        }
      }

      final List<Component> children = component.children();
      if(!children.isEmpty()) {
        final Style childrenStyle = new Style(style);
        for(final Component child : children) {
          this.append(child, childrenStyle);
          childrenStyle.set(style);
        }
      }
    }

    /* package */ void append(final @NonNull TextFormat format) {
      this.sb.append(this.character).append(LegacyComponentSerializerImpl.this.toLegacyCode(format));
    }

    @Override
    public String toString() {
      return this.sb.toString();
    }

    private final class Style {
      private @Nullable TextColor color;
      private final Set<TextDecoration> decorations;

      Style() {
        this.decorations = EnumSet.noneOf(TextDecoration.class);
      }

      Style(final @NonNull Style that) {
        this.color = that.color;
        this.decorations = EnumSet.copyOf(that.decorations);
      }

      /* package */ void set(final @NonNull Style that) {
        this.color = that.color;
        this.decorations.clear();
        this.decorations.addAll(that.decorations);
      }

      /* package */ void apply(final @NonNull Component component) {
        final TextColor color = component.color();
        if(color != null) {
          this.color = color;
        }

        for(int i = 0, length = DECORATIONS.length; i < length; i++) {
          final TextDecoration decoration = DECORATIONS[i];
          switch(component.decoration(decoration)) {
            case TRUE:
              this.decorations.add(decoration);
              break;
            case FALSE:
              this.decorations.remove(decoration);
              break;
          }
        }
      }

      /* package */ void applyFormat() {
        // If color changes, we need to do a full reset
        if(this.color != Cereal.this.style.color) {
          this.applyFullFormat();
          return;
        }

        // Does current have any decorations we don't have?
        // Since there is no way to undo decorations, we need to reset these cases
        if(!this.decorations.containsAll(Cereal.this.style.decorations)) {
          this.applyFullFormat();
          return;
        }

        // Apply new decorations
        for(final TextDecoration decoration : this.decorations) {
          if(Cereal.this.style.decorations.add(decoration)) {
            Cereal.this.append(decoration);
          }
        }
      }

      private void applyFullFormat() {
        if(this.color != null) {
          Cereal.this.append(this.color);
        } else {
          Cereal.this.append(Reset.INSTANCE);
        }
        Cereal.this.style.color = this.color;

        for(final TextDecoration decoration : this.decorations) {
          Cereal.this.append(decoration);
        }

        Cereal.this.style.decorations.clear();
        Cereal.this.style.decorations.addAll(this.decorations);
      }
    }
  }

  /* package */ static final class BuilderImpl implements Builder {
    private char character = LegacyComponentSerializer.SECTION_CHAR;
    private char hexCharacter = LegacyComponentSerializer.HEX_CHAR;
    private Style urlStyle = null;
    private boolean urlLink = false;
    private boolean colorDownsample = true;
    private boolean useTerriblyStupidHexFormat = false;

    BuilderImpl() {

    }

    BuilderImpl(final @NonNull LegacyComponentSerializerImpl serializer) {
      this.character = serializer.character;
      this.hexCharacter = serializer.hexCharacter;
      this.urlStyle = serializer.urlStyle;
      this.urlLink = serializer.urlLink;
      this.colorDownsample = serializer.colorDownsample;
    }

    @Override
    public @NonNull Builder character(final char legacyCharacter) {
      this.character = legacyCharacter;
      return this;
    }

    @Override
    public @NonNull Builder hexCharacter(final char legacyHexCharacter) {
      this.hexCharacter = legacyHexCharacter;
      return this;
    }

    @Override
    public @NonNull Builder extractUrls() {
      return this.extractUrls(null);
    }

    @Override
    public @NonNull Builder extractUrls(final @Nullable Style style) {
      this.urlLink = true;
      this.urlStyle = style;
      return this;
    }

    @Override
    public @NonNull Builder hexColors() {
      this.colorDownsample = false;
      return this;
    }

    @Override
    public @NonNull Builder useUnusualXRepeatedCharacterHexFormat() {
      this.useTerriblyStupidHexFormat = true; // :(
      return this;
    }

    @Override
    public @NonNull LegacyComponentSerializer build() {
      return new LegacyComponentSerializerImpl(this.character, this.hexCharacter, this.urlStyle, this.urlLink, this.colorDownsample, this.useTerriblyStupidHexFormat);
    }
  }

  /* package */ enum FormatCodeType {
    MOJANG_LEGACY,
    KYORI_HEX,
    BUNGEECORD_UNUSUAL_HEX
  }

  /* package */ static final class DecodedFormat {
    final FormatCodeType encodedFormat;
    final TextFormat format;

    private DecodedFormat(final FormatCodeType encodedFormat, final TextFormat format) {
      if(format == null) {
        throw new IllegalStateException("No format found");
      }
      this.encodedFormat = encodedFormat;
      this.format = format;
    }
  }
}
