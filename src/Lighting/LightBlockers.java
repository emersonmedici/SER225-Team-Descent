package Lighting;

import java.util.Arrays;

// a true/false grid of which map tiles stop light, plus the "can this light reach that spot?" test
// it only knows tiles as numbers (not Map or MapTile), so the Lighting package still doesn't depend on Level
public class LightBlockers {
    private final int width, height;   // map size in tiles
    private final int tileSize;        // world px per tile (16 px art * scale 3 = 48)
    private final boolean[] solid;     // same layout as Map's mapTiles: x + width * y

    public LightBlockers(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.solid = new boolean[width * height];
    }

    public void clear() {
        Arrays.fill(solid, false);
    }

    public void setSolid(int tileX, int tileY) {
        if (tileX >= 0 && tileY >= 0 && tileX < width && tileY < height) {
            solid[tileX + width * tileY] = true;
        }
    }

    // off the map counts as open (the map's edges are already void/wall tiles)
    public boolean isSolid(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= width || tileY >= height) {
            return false;
        }
        return solid[tileX + width * tileY];
    }

    public boolean isSolidAt(float worldX, float worldY) {
        return isSolid(tileOf(worldX), tileOf(worldY));
    }

    // world px -> tile index (floor instead of an (int) cast, so it's right for negative numbers too)
    private int tileOf(float worldPosition) {
        return (int) Math.floor(worldPosition / tileSize);
    }

    // can a light at (lightX, lightY) reach the spot (x, y)?
    public boolean canLight(float lightX, float lightY, float x, float y) {
        int tileX = tileOf(x);
        int tileY = tileOf(y);

        // open floor: lit if no wall sits on the line between them
        if (!isSolid(tileX, tileY)) {
            return hasLineOfSight(lightX, lightY, x, y);
        }

        // a wall: light it if the light can see the side of the wall that faces it
        // (checking each pixel's own line instead gives jagged sawtooth teeth along walls,
        // because lines to the far pixels clip the wall tile next door)

        // the spot on this wall tile closest to the light...
        float left = tileX * tileSize;
        float top = tileY * tileSize;
        float nearX = Math.max(left, Math.min(lightX, left + tileSize));
        float nearY = Math.max(top, Math.min(lightY, top + tileSize));

        // ...then half a pixel back toward the light, which lands in the tile in front of the wall
        float dx = lightX - nearX;
        float dy = lightY - nearY;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length == 0) {
            return true;   // the light is touching this wall
        }
        float frontX = nearX + dx / length * 0.5f;
        float frontY = nearY + dy / length * 0.5f;

        // the tile in front has to be open (if it's solid, this is the back of a thick wall) and reachable
        return !isSolidAt(frontX, frontY) && hasLineOfSight(lightX, lightY, frontX, frontY);
    }

    // true if no solid tile sits on the line between the two points
    // the start tile (where the light is) and the end tile aren't checked
    private boolean hasLineOfSight(float fromX, float fromY, float toX, float toY) {
        // the tile we're in now, and the tile we're heading for
        int tileX = tileOf(fromX);
        int tileY = tileOf(fromY);
        int endTileX = tileOf(toX);
        int endTileY = tileOf(toY);

        float dx = toX - fromX;
        float dy = toY - fromY;
        int stepX = dx > 0 ? 1 : -1;   // which way we move when we cross a left/right tile edge
        int stepY = dy > 0 ? 1 : -1;   // which way we move when we cross a top/bottom tile edge

        // progress along the line runs from 0 (start) to 1 (end)
        // tDelta = how much progress it takes to cross one whole tile left-right / up-down
        float tDeltaX = dx != 0 ? Math.abs(tileSize / dx) : Float.MAX_VALUE;
        float tDeltaY = dy != 0 ? Math.abs(tileSize / dy) : Float.MAX_VALUE;

        // tNext = progress at which the line reaches the next left/right edge, and the next top/bottom edge
        float edgeX = stepX > 0 ? (tileX + 1) * tileSize : tileX * tileSize;
        float edgeY = stepY > 0 ? (tileY + 1) * tileSize : tileY * tileSize;
        float tNextX = dx != 0 ? (edgeX - fromX) / dx : Float.MAX_VALUE;
        float tNextY = dy != 0 ? (edgeY - fromY) / dy : Float.MAX_VALUE;

        // each edge crossed moves us exactly one tile, so this is how many moves it takes to arrive
        int crossings = Math.abs(endTileX - tileX) + Math.abs(endTileY - tileY);

        // move one tile at a time through whichever edge comes first, checking each tile we enter
        // i starts at 1 so the final move (into the end tile) never happens
        for (int i = 1; i < crossings; i++) {
            // once we're in the end tile's row (or column), only move the other way;
            // this stops float rounding from ever walking past the end tile
            boolean moveSideways = tileY == endTileY || (tileX != endTileX && tNextX < tNextY);
            if (moveSideways) {
                tileX += stepX;
                tNextX += tDeltaX;
            } else {
                tileY += stepY;
                tNextY += tDeltaY;
            }
            if (isSolid(tileX, tileY)) {
                return false;
            }
        }
        return true;
    }
}
