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
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;

import static java.util.Objects.requireNonNull;

record ClickEventImpl<T extends ClickEvent.Payload>(Action<T> action, Payload payload) implements ClickEvent<T> {

  static <T extends ClickEvent.Payload> ClickEvent<T> create(final Action<T> action, final T payload) {
    return new ClickEventImpl<>(requireNonNull(action, "action"), requireNonNull(payload, "payload"));
  }

  @Override
  public void styleApply(final Style.Builder style) {
    style.clickEvent(this);
  }

  @Override
  public Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("payload", this.payload)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  record ActionImpl<T extends Payload>(String name, boolean readable, Class<? extends Payload> payloadType) implements ClickEvent.Action<T> {
    @Override
    public boolean supports(final Payload payload) {
      return payload.getClass() == this.payloadType;
    }
  }
}
