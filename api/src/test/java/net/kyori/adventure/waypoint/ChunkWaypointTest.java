package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChunkWaypointTest {
  private final AtomicInteger style = new AtomicInteger();
  private final AtomicInteger color = new AtomicInteger();
  private final AtomicInteger pos = new AtomicInteger();
  private final Waypoint.Listener listener = new Waypoint.Listener() {
    @Override
    public void waypointStyleChanged(@NonNull Waypoint waypoint, @NonNull Key oldStyle, @NonNull Key newStyle) {
      ChunkWaypointTest.this.style.incrementAndGet();
    }

    @Override
    public void waypointColorChanged(@NonNull Waypoint waypoint, @NonNull TextColor oldColor, @NonNull TextColor newColor) {
      ChunkWaypointTest.this.color.incrementAndGet();
    }

    @Override
    public void waypointChunkPositionChanged(
      @NonNull ChunkWaypoint waypoint,
      final int oldX, final int oldZ,
      final int newX, final int newZ
    ) {
      ChunkWaypointTest.this.pos.incrementAndGet();
    }
  };
  private final ChunkWaypoint waypoint = Waypoint.chunk(NamedTextColor.RED, 0, 0);

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
    final int testZ = 25;
    assertEquals(testX, this.waypoint.pos(testX, testZ).x());
    assertEquals(testZ, this.waypoint.z());
    assertEquals(0, this.pos.get());

    this.waypoint.addListener(this.listener);
    final int testX2 = -8;
    final int testZ2 = 31;
    assertEquals(testX2, this.waypoint.pos(testX2, testZ2).x());
    assertEquals(testZ2, this.waypoint.z());
    assertEquals(1, this.pos.get());

    assertEquals(testX2, this.waypoint.pos(testX2, testZ2).x());
    assertEquals(testZ2, this.waypoint.z());
    assertEquals(1, this.pos.get()); // value has not changed, should not have incremented
  }
}
