package Level;

import Utils.Point;

// Anything that should make the chase music kick in when it gets close to the player.
// The music doesn't care what kind of monster it is -- only where it is and whether it's actually after you.
public interface Threat {
    // false while this threat can't actually get to the player (not started yet, despawned, waiting outside a vent...)
    boolean isHunting();

    // center of the threat, in world pixels
    Point getThreatCenter();
}