package net.kyori.adventure.waypoint;

import org.jetbrains.annotations.Contract;

/**
 * Represents an azimuth waypoint.
 *
 * @since 5.1.0
 * @sinceMinecraft 1.21.6
 */
public sealed interface AzimuthWaypoint extends Waypoint permits AzimuthWaypointImpl {

  /**
   * Gets the angle.
   *
   * @return the angle
   */
  float angle();

  /**
   * Sets the angle.
   *
   * @param angle the angle
   * @return the waypoint
   */
  @Contract("_ -> this")
  AzimuthWaypoint angle(final float angle);

}
