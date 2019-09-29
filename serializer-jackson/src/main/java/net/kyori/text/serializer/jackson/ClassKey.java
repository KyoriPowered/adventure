/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.serializer.jackson;

// This is reimplementation of ClassKey from Jackson, immutable, more features and under another license
public class ClassKey implements Comparable<ClassKey>, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final Class<?> clazz;
    private final String clazzName;
    private final int clazzHash;

    ClassKey(final Class<?> clazz) {
        this.clazz = clazz;
        this.clazzName = clazz.getName();
        this.clazzHash = clazz.hashCode();
    }

    boolean isAssignableFrom(final Class<?> clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

    @Override
    public int compareTo(final ClassKey o) {
        return this.clazzName.compareTo(o.clazzName);
    }

    @Override
    public boolean equals(final Object o) {
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        final ClassKey classKey = (ClassKey) o;
        return this.clazz == classKey.clazz;
    }

    @Override
    public int hashCode() {
        return this.clazzHash;
    }

    @Override
    public String toString() {
        return "ClassKey{" + this.clazzName + "}";
    }
}
