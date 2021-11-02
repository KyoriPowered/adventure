/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.width;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

/**
 * Generated with <a href="https://github.com/KingOfSquares/MinecraftFontPixelWidth">MinecraftFontPixelWidth</a>.
 * <p>Can calculate for all characters found in the default ascii.png file</p>
 *
 * @since 4.10.0
 */
public class DefaultCharacterWidthFunction implements CharacterWidthFunction {
  public static final DefaultCharacterWidthFunction INSTANCE = new DefaultCharacterWidthFunction();

  @Override
  public final float widthOf(final int c, @NotNull final Style style) {
    float i = -1;
    switch(c) {
      case 33:
      case 39:
      case 44:
      case 46:
      case 58:
      case 59:
      case 105:
      case 124:
        i = 2.0F;
        break;
      case 96:
      case 108:
        i = 3.0F;
        break;
      case 34:
      case 40:
      case 41:
      case 42:
      case 73:
      case 91:
      case 93:
      case 116:
      case 123:
      case 125:
        i = 4.0F;
        break;
      case 35:
      case 36:
      case 37:
      case 38:
      case 43:
      case 45:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 61:
      case 63:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 92:
      case 94:
      case 95:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 103:
      case 104:
      case 106:
      case 109:
      case 110:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 117:
      case 118:
      case 119:
      case 120:
      case 121:
      case 122:
      case 163:
      case 172:
      case 177:
      case 247:
      case 402:
      case 8712:
      case 8729:
      case 8804:
      case 8805:
      case 9474:
      case 9488:
      case 9496:
      case 9508:
      case 9557:
      case 9563:
      case 9569:
      case 9632:
        i = 6.0F;
        break;
      case 8709:
      case 8992:
      case 9553:
      case 9558:
      case 9559:
      case 9564:
      case 9565:
      case 9570:
      case 9571:
      case 9617:
        i = 12.0F;
        break;
      case 9472:
      case 9484:
      case 9492:
      case 9500:
      case 9516:
      case 9524:
      case 9532:
      case 9552:
      case 9554:
      case 9555:
      case 9556:
      case 9560:
      case 9561:
      case 9562:
      case 9566:
      case 9567:
      case 9568:
      case 9572:
      case 9573:
      case 9574:
      case 9575:
      case 9576:
      case 9577:
      case 9578:
      case 9579:
      case 9580:
      case 9600:
      case 9604:
      case 9608:
      case 9616:
      case 9618:
      case 9619:
        i = 13.0F;
        break;
      case 60:
      case 62:
      case 102:
      case 107:
      case 170:
      case 176:
      case 178:
      case 186:
      case 8319:
      case 8993:
      case 9612:
        i = 5.0F;
        break;
      case 64:
      case 126:
      case 171:
      case 187:
      case 8730:
      case 8776:
      case 8801:
        i = 15.0F;
        break;
    }
    if (i != -1 && style.hasDecoration(TextDecoration.BOLD)) i++;
    return i;
  }
}
