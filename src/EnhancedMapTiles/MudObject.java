package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.TileType;
import Utils.Point;

public class MudObject extends EnhancedMapTile {

    public MudObject(Point location) {
        super(
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Mud.png"), 16, 16),
            TileType.PASSABLE
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