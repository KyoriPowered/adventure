package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorWaypointTest {
  private final AtomicInteger style = new AtomicInteger();
  private final AtomicInteger color = new AtomicInteger();
  private final AtomicInteger pos = new AtomicInteger();
  private final Waypoint.Listener listener = new Waypoint.Listener() {
    @Override
    public void waypointStyleChanged(@NonNull Waypoint waypoint, @NonNull Key oldStyle, @NonNull Key newStyle) {
      VectorWaypointTest.this.style.incrementAndGet();
    }

    @Override
    public void waypointColorChanged(@NonNull Waypoint waypoint, @NonNull TextColor oldColor, @NonNull TextColor newColor) {
      VectorWaypointTest.this.color.incrementAndGet();
    }

    @Override
    public void waypointVectorChanged(
      @NonNull VectorWaypoint waypoint,
      final int oldX,
      final int oldY,
      final int oldZ,
      final int newX,
      final int newY,
      final int newZ
    ) {
      VectorWaypointTest.this.pos.incrementAndGet();
    }
  };

  private final VectorWaypoint waypoint = Waypoint.vector(NamedTextColor.RED, 0, 0, 0);

  @Test
  void testColor() {
    final TextColor testColor = NamedTextColor.BLUE;
    assertEquals(testColor, this.waypoint.color(testColor).color());
    assertEquals(0, this.color.get());

    this.waypoint.addListener(this.listener);
    final TextColor testColor2 = NamedTextColor.YELLOW;
    assertEquals(testColor2, this.waypoint.color(testColor2).color());
    assertEquals(1, this.color.get());

    assertEquals(testColor2, this.waypoint.color(testColor2).color());
    assertEquals(1, this.color.get()); // value has not changed, should not have incremented
  }

  @Test
  void testStyle() {
    final Key testStyle = Key.key("bowtie");
    assertEquals(testStyle, this.waypoint.style(testStyle).style());
    assertEquals(0, this.style.get());

    this.waypoint.addListener(this.listener);
    final Key testStyle2 = Key.key("default");
    assertEquals(testStyle2, this.waypoint.style(testStyle2).style());
    assertEquals(1, this.style.get());

    assertEquals(testStyle2, this.waypoint.style(testStyle2).style());
    assertEquals(1, this.style.get()); // value has not changed, should not have incremented
  }

  @Test
  void testPos() {
    final int testX = 15;
    final int testY = 60;
    final int testZ = 25;
    assertEquals(testX, this.waypoint.pos(testX, testY, testZ).x());
    assertEquals(testY, this.waypoint.y());
    assertEquals(testZ, this.waypoint.z());
    assertEquals(0, this.pos.get());

    this.waypoint.addListener(this.listener);
    final int testX2 = -8;
    final int testY2 = 120;
    final int testZ2 = 31;
    assertEquals(testX2, this.waypoint.pos(testX2, testY2, testZ2).x());
    assertEquals(testY2, this.waypoint.y());
    assertEquals(testZ2, this.waypoint.z());
    assertEquals(1, this.pos.get());

    assertEquals(testX2, this.waypoint.pos(testX2, testY2, testZ2).x());
    assertEquals(testY2, this.waypoint.y());
    assertEquals(testZ2, this.waypoint.z());
    assertEquals(1, this.pos.get()); // value has not changed, should not have incremented
  }
}
