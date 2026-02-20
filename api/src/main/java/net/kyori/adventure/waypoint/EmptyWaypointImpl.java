package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;

final class EmptyWaypointImpl extends WaypointImpl implements EmptyWaypoint {
  EmptyWaypointImpl(final Key style, final TextColor color) {
    super(style, color);
  }

  EmptyWaypointImpl(final TextColor color) {
    super(color);
  }
}
