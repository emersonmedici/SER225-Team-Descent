package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
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

    // Call only after checking that this location is safe.
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
