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
package net.kyori.adventure.identity;

import com.google.common.testing.EqualsTester;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class IdentityTest {
  @Test
  void testNilIdentity() {
    assertSame(Identity.nil(), Identity.identity(new UUID(0, 0)));
    assertSame(Identity.nil(), Identity.identity(UUID.fromString("00000000-0000-0000-0000-000000000000")));
  }

  @Test
  void testIdentity() {
    final UUID uuid = UUID.randomUUID();
    final Identity identity = Identity.identity(uuid);
    assertSame(uuid, identity.uuid());
  }

  @Test
  void testEquality() {
    final UUID uuid = UUID.randomUUID();
    new EqualsTester()
      .addEqualityGroup(Identity.nil())
      .addEqualityGroup(Identity.identity(uuid), Identity.identity(uuid))
      .testEquals();
  }
}
