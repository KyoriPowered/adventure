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
package net.kyori.adventure.audience;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class AudienceTest {
  @Test
  void testMultiOf_many() {
    final Audience a0 = Audience.empty();
    final Audience a1 = Audience.empty();
    final MultiAudience ma = MultiAudience.of(a0, a1);
    assertThat(ma.viewers()).containsExactly(a0, a1).inOrder();
  }

  @Test
  void testWeakOf_none() {
    final Audience empty = Audience.empty();
    final Audience weak = Audience.weakOf(empty);
    assertSame(empty, weak);
  }

  @Test
  void testPerform() {
    final AtomicInteger i = new AtomicInteger();
    final Viewer v = (Viewer.Messages) message -> i.incrementAndGet();

    v.perform(Viewer.Messages.class, a -> a.sendMessage(TextComponent.of("hi")));
    assertEquals(1, i.get());
  }

  @Test
  void testWiden() {
    final AtomicInteger i = new AtomicInteger();
    final Viewer v = (Viewer.Messages) message -> i.incrementAndGet();
    final Audience a = v.asAudience();

    a.clearTitle(); // just make sure unsupported things don't throw an exception

    a.sendMessage(TextComponent.of("hi"));
    assertEquals(1, i.get());

    assertEquals(Audience.empty(), a.perform(Viewer.Messages.class, x -> x.sendMessage(TextComponent.of("hi"))));
    assertEquals(a, a.perform(Viewer.Titles.class, Viewer.Titles::clearTitle));
    assertEquals(2, i.get());
  }

  @Test
  void testForward() {
    final AtomicInteger i = new AtomicInteger();
    final Viewer v = (Viewer.Messages) message -> i.incrementAndGet();
    final ForwardingAudience a = () -> v;

    a.clearTitle(); // just make sure unsupported things don't throw an exception

    a.sendMessage(TextComponent.of("hi"));
    assertEquals(1, i.get());

    assertEquals(Audience.empty(), a.perform(Viewer.Messages.class, x -> x.sendMessage(TextComponent.of("hi"))));
    assertEquals(a, a.perform(Viewer.Titles.class, Viewer.Titles::clearTitle));
    assertEquals(2, i.get());
  }

  @Test
  void testMultiForward() {
    class MsgViewer implements Viewer.Messages {
      int msgCount = 0;
      @Override
      public void sendMessage(final @NonNull Component message) {
        this.msgCount++;
      }
    }
    class MsgActionViewer extends MsgViewer implements Viewer.ActionBars {
      int actionCount = 0;
      @Override
      public void sendActionBar(final @NonNull Component message) {
        this.actionCount++;
      }
    }

    final MsgActionViewer v0 = new MsgActionViewer();
    final MsgViewer v1 = new MsgViewer();

    final MultiAudience ma = MultiAudience.of(v0, v1);
    final TextComponent c = TextComponent.of("hi");

    ma.sendMessage(c);
    assertEquals(1, v0.msgCount);
    assertEquals(1, v1.msgCount);
    assertEquals(0, v0.actionCount);

    ma.sendActionBar(c);
    assertEquals(1, v0.msgCount);
    assertEquals(1, v1.msgCount);
    assertEquals(1, v0.actionCount);

    ma.perform(Viewer.ActionBars.class, a -> a.sendActionBar(c))
      .perform(Viewer.Messages.class, a -> a.sendMessage(c));
    assertEquals(1, v0.msgCount);
    assertEquals(2, v1.msgCount);
    assertEquals(2, v0.actionCount);
  }
}
