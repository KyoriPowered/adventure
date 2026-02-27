package net.kyori.adventure.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;

final class EmptyWaypointImpl extends WaypointImpl implements EmptyWaypoint {
  EmptyWaypointImpl(Key style, TextColor color) {
    super(style, color);
  }
}
