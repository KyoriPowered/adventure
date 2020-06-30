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
    assertThat(ma.audiences()).containsExactly(a0, a1).inOrder();
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
    final Audience a0 = (Audience.Message) message -> i.incrementAndGet();

    a0.perform(Audience.Message.class, a -> a.sendMessage(TextComponent.of("hi")));
    assertEquals(1, i.get());
  }

  @Test
  void testWiden() {
    final AtomicInteger i = new AtomicInteger();
    final Audience a0 = (Audience.Message) message -> i.incrementAndGet();
    final Audience.Everything a1 = Audience.of(a0);

    a1.clearTitle(); // just make sure unsupported things don't throw an exception

    a1.sendMessage(TextComponent.of("hi"));
    assertEquals(1, i.get());

    assertEquals(Audience.empty(), a1.perform(Audience.Message.class, a -> a.sendMessage(TextComponent.of("hi"))));
    assertEquals(a1, a1.perform(Audience.Title.class, Audience.Title::clearTitle));
    assertEquals(2, i.get());
  }

  @Test
  void testForward() {
    final AtomicInteger i = new AtomicInteger();
    final Audience a0 = (Audience.Message) message -> i.incrementAndGet();
    final ForwardingAudience a1 = () -> a0;

    a1.clearTitle(); // just make sure unsupported things don't throw an exception

    a1.sendMessage(TextComponent.of("hi"));
    assertEquals(1, i.get());

    assertEquals(Audience.empty(), a1.perform(Audience.Message.class, a -> a.sendMessage(TextComponent.of("hi"))));
    assertEquals(a1, a1.perform(Audience.Title.class, Audience.Title::clearTitle));
    assertEquals(2, i.get());
  }

  @Test
  void testMultiForward() {
    class MsgAudience implements Audience.Message {
      int msgCount = 0;
      @Override
      public void sendMessage(@NonNull Component message) {
        this.msgCount++;
      }
    }
    class MsgActionAudience extends MsgAudience implements Audience.ActionBar {
      int actionCount = 0;
      @Override
      public void sendActionBar(@NonNull Component message) {
        this.actionCount++;
      }
    }

    final MsgActionAudience a0 = new MsgActionAudience();
    final MsgAudience a1 = new MsgAudience();

    final MultiAudience ma = MultiAudience.of(a0, a1);
    final TextComponent c = TextComponent.of("hi");

    ma.sendMessage(c);
    assertEquals(1, a0.msgCount);
    assertEquals(1, a1.msgCount);
    assertEquals(0, a0.actionCount);

    ma.sendActionBar(c);
    assertEquals(1, a0.msgCount);
    assertEquals(1, a1.msgCount);
    assertEquals(1, a0.actionCount);

    ma.perform(Audience.ActionBar.class, a -> a.sendActionBar(c))
      .perform(Audience.Message.class, a -> a.sendMessage(c));
    assertEquals(1, a0.msgCount);
    assertEquals(2, a1.msgCount);
    assertEquals(2, a0.actionCount);
  }
}
