package net.kyori.adventure.waypoint;

import org.jetbrains.annotations.Contract;

/**
 * Represents a chunk waypoint.
 *
 * @since 5.1.0
 * @sinceMinecraft 1.21.6
 */
public sealed interface ChunkWaypoint extends Waypoint permits ChunkWaypointImpl {

  /**
   * Gets the X coordinate.
   *
   * @return the X coordinate
   */
  int x();

  /**
   * Gets the Z coordinate.
   *
   * @return the Z coordinate
   */
  int z();

  /**
   * Sets the X and Z coordinate.
   *
   * @param x the X coordinate
   * @param z the Z coordinate
   * @return the waypoint
   */
  @Contract("_, _ -> this")
  ChunkWaypoint pos(final int x, final int z);

}
