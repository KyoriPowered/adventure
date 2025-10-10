/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.event;

import java.util.stream.Stream;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.examination.ExaminableProperty;

final class PayloadImpl {
  private PayloadImpl() {
  }

  record TextImpl(String value) implements ClickEventImpl.Payload.Text {
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("value", this.value)
      );
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  record IntImpl(int integer) implements ClickEventImpl.Payload.Int {
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("integer", this.integer)
      );
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  record DialogImpl(DialogLike dialog) implements ClickEventImpl.Payload.Dialog {
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("dialog", this.dialog)
      );
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  record CustomImpl(Key key, BinaryTagHolder nbt) implements ClickEventImpl.Payload.Custom {
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("nbt", this.nbt)
      );
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }
}
