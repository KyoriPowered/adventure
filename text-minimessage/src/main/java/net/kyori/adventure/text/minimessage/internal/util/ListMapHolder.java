package net.kyori.adventure.text.minimessage.internal.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ListMapHolder<E, K, V>(List<E> list, Map<K, V> map) {
  public static <E, K, V> ListMapHolder<E, K, V> empty() {
    return new ListMapHolder<>(Collections.emptyList(), Collections.emptyMap());
  }

  public static <E, K, V> ListMapHolder<E, K, V> of(List<E> list, Map<K, V> map) {
    return new ListMapHolder<>(list, map);
  }
}
