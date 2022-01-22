package net.kyori.adventure.title;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class TitleTest {
  private static final Component TITLE = Component.text("title");
  private static final Component SUBTITLE = Component.text("subtitle");
  private static final Title.Times TIMES = Title.Times.times(Duration.ZERO, Duration.ZERO, Duration.ZERO);
  
  @Test
  void testTitle() {
    final Title t = Title.title(TITLE, SUBTITLE, TIMES);
    assertSame(TITLE, t.title());
  }

  @Test
  void testSubtitle() {
    final Title t = Title.title(TITLE, SUBTITLE, TIMES);
    assertSame(SUBTITLE, t.subtitle());
  }

  @Test
  void testTimes() {
    final Title t0 = Title.title(TITLE, SUBTITLE, TIMES);
    assertSame(TIMES, t0.times());

    final Title t1 = Title.title(TITLE, SUBTITLE, null);
    assertNull(t1.times());
  }

  @Test
  void testRebuild() {
    final Title title = Title.title(TITLE, SUBTITLE, TIMES);
    assertEquals(title, title.toBuilder().build());
  }

  @Test
  void testBuilder() {
    final Title t0 = Title.builder()
      .title(TITLE)
      .subtitle(SUBTITLE)
      .times(TIMES)
      .build();
    assertEquals(Title.title(TITLE, SUBTITLE, TIMES), t0);
  }

}
