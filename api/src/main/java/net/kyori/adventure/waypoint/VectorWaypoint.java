package net.kyori.adventure.waypoint;

import org.jetbrains.annotations.Contract;

/**
 * Represents a vector waypoint.
 *
 * @since 5.1.0
 * @sinceMinecraft 1.21.6
 */
public sealed interface VectorWaypoint extends Waypoint permits VectorWaypointImpl {

  /**
   * Gets the X coordinate.
   *
   * @return the X coordinate
   */
  int x();

  /**
   * Gets the Y coordinate.
   *
   * @return the Y coordinate
   */
  int y();

  /**
   * Gets the Z coordinate.
   *
   * @return the Z coordinate
   */
  int z();

  /**
   * Sets the X, Y and Z coordinates.
   *
   * @param x the X coordinate
   * @param y the Y coordinate
   * @param z the Z coordinate
   * @return the waypoint
   */
  @Contract("_, _, _ -> this")
  VectorWaypoint pos(final int x, final int y, final int z);

}
