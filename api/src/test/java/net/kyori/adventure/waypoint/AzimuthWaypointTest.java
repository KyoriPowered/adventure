package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AzimuthWaypointTest {
  private final AtomicInteger style = new AtomicInteger();
  private final AtomicInteger color = new AtomicInteger();
  private final AtomicInteger angle = new AtomicInteger();
  private final Waypoint.Listener listener = new Waypoint.Listener() {
    @Override
    public void waypointStyleChanged(@NonNull Waypoint waypoint, @NonNull Key oldStyle, @NonNull Key newStyle) {
      AzimuthWaypointTest.this.style.incrementAndGet();
    }

    @Override
    public void waypointColorChanged(@NonNull Waypoint waypoint, @NonNull TextColor oldColor, @NonNull TextColor newColor) {
      AzimuthWaypointTest.this.color.incrementAndGet();
    }

    @Override
    public void waypointAngleChanged(@NonNull AzimuthWaypoint waypoint, final float oldAngle, final float newAngle) {
      AzimuthWaypointTest.this.angle.incrementAndGet();
    }
  };
  private final AzimuthWaypoint waypoint = Waypoint.azimuth(NamedTextColor.RED, 0f);

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
  void testAngle() {
    final float testAngle = 45f;
    assertEquals(testAngle, this.waypoint.angle(testAngle).angle());
    assertEquals(0, this.angle.get());

    this.waypoint.addListener(this.listener);
    final float testAngle2 = 120f;
    assertEquals(testAngle2, this.waypoint.angle(testAngle2).angle());
    assertEquals(1, this.angle.get());

    assertEquals(testAngle2, this.waypoint.angle(testAngle2).angle());
    assertEquals(1, this.angle.get()); // value has not changed, should not have incremented
  }
}
