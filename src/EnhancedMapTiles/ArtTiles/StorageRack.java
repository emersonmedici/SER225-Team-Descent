package EnhancedMapTiles.ArtTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.TileType;
import Utils.Point;

public class StorageRack extends EnhancedMapTile {

    // Drawn a little smaller than the other decorations (x3)
    private static final float SCALE = 2.5f;

    // The map centres a x3 (96px) image, so shift by half the size difference to keep this one centred
    private static final float SHRINK_OFFSET = (32 * 3 - 32 * SCALE) / 2;

    // location is the top-left corner of the image (the map centres it on a tile)
    public StorageRack(Point location, Point image) {
        super(location.x + SHRINK_OFFSET, location.y + SHRINK_OFFSET, loadImage(location, image), null, TileType.NOT_PASSABLE);
    }

    // Built before super() runs, so it can't rely on any fields
    // image is (x = column, y = row) in the sprite sheet
    private static GameObject loadImage(Point location, Point image) {
        SpriteSheet spriteSheet = new SpriteSheet(ImageLoader.load("StorageRack.png"), 32, 32);
        Frame frame = new FrameBuilder(spriteSheet.getSubImage((int)image.y, (int)image.x))
            .withScale(SCALE)
            .build();

        return new GameObject(location.x + SHRINK_OFFSET, location.y + SHRINK_OFFSET, frame);
    }
}
