package EnhancedMapTiles.ArtTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.TileType;
import Utils.Point;

public class Couch extends EnhancedMapTile {

    // location is the top-left corner of the image (the map centres it on a tile)
    public Couch(Point location, Point image) {
        super(location.x, location.y, loadImage(location, image), null, TileType.NOT_PASSABLE);
    }

    // Built before super() runs, so it can't rely on any fields
    // image is (x = column, y = row) in the sprite sheet
    private static GameObject loadImage(Point location, Point image) {
        SpriteSheet spriteSheet = new SpriteSheet(ImageLoader.load("Couch.png"), 32, 32);
        Frame frame = new FrameBuilder(spriteSheet.getSubImage((int)image.y, (int)image.x))
            .withScale(3)
            .build();

        return new GameObject(location.x, location.y, frame);
    }
}
