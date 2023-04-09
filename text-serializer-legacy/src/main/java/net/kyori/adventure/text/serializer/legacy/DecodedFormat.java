package net.kyori.adventure.text.serializer.legacy;

import net.kyori.adventure.text.format.TextFormat;

final class DecodedFormat {
  final FormatCodeType encodedFormat;
  final TextFormat format;

  DecodedFormat(final FormatCodeType encodedFormat, final TextFormat format) {
    if (format == null) {
      throw new IllegalStateException("No format found");
    }
    this.encodedFormat = encodedFormat;
    this.format = format;
  }
}
