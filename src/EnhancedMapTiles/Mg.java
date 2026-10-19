package EnhancedMapTiles;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.NPC;
import Level.TileType;
import Utils.Point;

public class Mg extends EnhancedMapTile {
    public Mg(Point location) {
        super(
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Mg1.png"), 16, 16),
            TileType.NOT_PASSABLE 
    );
    }

    public Mg(Point location, int k) {
        super(
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Mg2.png"), 16, 16),
            TileType.NOT_PASSABLE 
    );
    }

    public Mg(Point location, String k) {
        super(
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Panel.png"), 16, 16),
            TileType.NOT_PASSABLE 
    );
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
            .withScale(3)
            .build();

        return new GameObject(x, y, frame);
    }
}
