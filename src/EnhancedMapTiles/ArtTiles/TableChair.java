package EnhancedMapTiles.ArtTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.TileType;
import Utils.Point;

// One tile in size, so pass it a map tile's location (no centerOnTile needed)
public class TableChair extends EnhancedMapTile {

    // Indexes in TableChair.png
    public static final int TABLE = 0;
    public static final int CHAIR_RIGHT = 4;
    public static final int CHAIR_FORWARD = 5;
    public static final int CHAIR_BACKWARD = 6;
    // Not in the sheet: this draws CHAIR_RIGHT flipped
    public static final int CHAIR_LEFT = 7;

    public TableChair(Point location, int index) {
        super(location.x, location.y, loadImage(location, index), null, getTileType(index));
    }

    // Tables block the player, chairs can be walked over
    private static TileType getTileType(int index) {
        if (index == TABLE) {
            return TileType.NOT_PASSABLE;
        }
        return TileType.PASSABLE;
    }

    // Built before super() runs, so it can't rely on any fields
    private static GameObject loadImage(Point location, int index) {
        SpriteSheet spriteSheet = new SpriteSheet(ImageLoader.load("TableChair.png"), 16, 16);

        int sheetIndex = index;
        ImageEffect imageEffect = ImageEffect.NONE;
        if (index == CHAIR_LEFT) {
            sheetIndex = CHAIR_RIGHT;
            imageEffect = ImageEffect.FLIP_HORIZONTAL;
        }

        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, sheetIndex))
            .withScale(3)
            .withImageEffect(imageEffect)
            .build();

        return new GameObject(location.x, location.y, frame);
    }
}
