/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.junit.jupiter.api.Test;

final class ClickEventTest implements NBTComponentTest {
  @Test
  public void testUrl() {
    this.test(Component.empty().clickEvent(ClickEvent.openUrl("https://docs.advntr.dev/")));
  }

  @Test
  public void testFile() {
    this.test(Component.empty().clickEvent(ClickEvent.openFile("/root/some-file.extension")));
  }

  @Test
  public void testRunCommand() {
    this.test(Component.empty().clickEvent(ClickEvent.runCommand("/help")));
  }

  @Test
  public void testSuggestCommand() {
    this.test(Component.empty().clickEvent(ClickEvent.suggestCommand("/?")));
  }

  @Test
  public void testChangePage() {
    this.test(Component.empty().clickEvent(ClickEvent.changePage(6)));
  }

  @Test
  public void testCopyToClipboard() {
    this.test(Component.empty().clickEvent(ClickEvent.copyToClipboard("abcdxyz")));
  }
}
