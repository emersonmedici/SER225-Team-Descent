package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import GameObject.Rectangle;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Level.MapTile;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.NPC;
import Utils.Point;

public class CarriableObject extends EnhancedMapTile {
    private Player carrier;

    public CarriableObject(Point location) {
        super(
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Rock.png"), 16, 16),
            TileType.PASSABLE
        );
        // Passable pickups can overlap the player when interacted with.
        setIsUncollidable(true);
    }

    public boolean isCarried() {
        return carrier != null;
    }

    public boolean pickUp(Player player) {
        if (player == null || isCarried()) {
            return false;
        }

        carrier = player;
        setIsUncollidable(true);
        updateCarriedPosition();
        return true;
    }

    // Check a proposed position without moving the carried object.
    public boolean canDropAt(float x, float y) {
        if (map == null || carrier == null) {
            return false;
        }
        Rectangle bounds = getBounds();
        Rectangle target = new Rectangle(x + bounds.getX1() - getX(),
                y + bounds.getY1() - getY(), bounds.getWidth(), bounds.getHeight());
        if (target.getX1() < 0 || target.getY1() < 0
                || target.getX2() + 1 > map.getWidthPixels()
                || target.getY2() + 1 > map.getHeightPixels()
                || target.intersects(carrier.getBounds())) {
            return false;
        }

        int tileWidth = map.getTileset().getScaledSpriteWidth();
        int tileHeight = map.getTileset().getScaledSpriteHeight();
        int lastColumn = (int) Math.ceil((target.getX2() + 1) / tileWidth) - 1;
        int lastRow = (int) Math.ceil((target.getY2() + 1) / tileHeight) - 1;
        for (int row = (int) (target.getY1() / tileHeight); row <= lastRow; row++) {
            for (int column = (int) (target.getX1() / tileWidth); column <= lastColumn; column++) {
                MapTile tile = map.getMapTile(column, row);
                if (tile == null || (tile.getTileType() == TileType.NOT_PASSABLE
                        && target.intersects(tile.getBounds()))) {
                    return false;
                }
            }
        }
        for (EnhancedMapTile tile : map.getEnhancedMapTiles()) {
            if (tile != this && isDropObstacle(tile)
                    && tile.getTileType() == TileType.NOT_PASSABLE
                    && target.intersects(tile.getBounds())) {
                return false;
            }
        }
        for (NPC npc : map.getNPCs()) {
            if (isDropObstacle(npc) && target.intersects(npc.getBounds())) {
                return false;
            }
        }
        return true;
    }

    private boolean isDropObstacle(MapEntity entity) {
        return !entity.isHidden() && entity.exists() && !entity.isUncollidable()
                && entity.getMapEntityStatus() != MapEntityStatus.REMOVED;
    }

    // Player.dropCarriedObject validates the position and clears its held-item slot.
    public void dropAt(float x, float y) {
        if (!isCarried()) {
            return;
        }

        setLocation(x, y);
        carrier = null;
        setIsUncollidable(true);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (isCarried()) {
            updateCarriedPosition();
        }
    }

    private void updateCarriedPosition() {
        float carriedX =
            carrier.getX() + (carrier.getWidth() - getWidth()) / 2f;
        float carriedY =
            carrier.getY() - getHeight() + 8;

        setLocation(carriedX, carriedY);
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
            .withScale(3)
            .build();

        return new GameObject(x, y, frame);
    }
}
