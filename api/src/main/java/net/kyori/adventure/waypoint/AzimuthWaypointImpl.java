package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;

final class AzimuthWaypointImpl extends WaypointImpl implements AzimuthWaypoint {

  private float angle;

  public AzimuthWaypointImpl(final Key style, final TextColor color, final float angle) {
    super(style, color);
    this.angle = angle;
  }

  public AzimuthWaypointImpl(final TextColor color, final float angle) {
    super(color);
    this.angle = angle;
  }

  @Override
  public float angle() {
    return this.angle;
  }

  @Override
  public AzimuthWaypoint angle(float angle) {
    this.angle = angle;
    return this;
  }
}
