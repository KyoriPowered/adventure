package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;

final class ChunkWaypointImpl extends WaypointImpl implements ChunkWaypoint {

  private int x;
  private int z;

  public ChunkWaypointImpl(final Key style, final TextColor color, final int x, final int z) {
    super(style, color);
    this.x = x;
    this.z = z;
  }

  public ChunkWaypointImpl(final TextColor color, final int x, final int z) {
    super(color);
    this.x = x;
    this.z = z;
  }

  @Override
  public int x() {
    return this.x;
  }

  @Override
  public int z() {
    return this.z;
  }

  @Override
  public ChunkWaypoint pos(final int x, final int z) {
    this.x = x;
    this.z = z;
    return this;
  }

}
