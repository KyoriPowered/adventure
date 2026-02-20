package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;

final class VectorWaypointImpl extends WaypointImpl implements VectorWaypoint {

  private int x;
  private int y;
  private int z;

  public VectorWaypointImpl(final Key style, final TextColor color, final int x, final int y, final int z) {
    super(style, color);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public VectorWaypointImpl(final TextColor color, final int x, final int y, final int z) {
    super(color);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public int x() {
    return this.x;
  }

  @Override
  public int y() {
    return this.y;
  }

  @Override
  public int z() {
    return this.z;
  }

  @Override
  public VectorWaypoint pos(final int x, final int y, final int z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }
}
