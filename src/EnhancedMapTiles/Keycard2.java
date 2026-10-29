package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Scripts.PickupObjectScript;
import Utils.Point;
import java.awt.image.BufferedImage;

public class Keycard2 extends CarriableObject {
    public Keycard2(Point location) {
        super(location, loadSpriteSheet());
        setInteractScript(new PickupObjectScript());
    }

    private static SpriteSheet loadSpriteSheet() {
        BufferedImage image = ImageLoader.load("Keycard1.png");
        return new SpriteSheet(image, image.getWidth(), image.getHeight());
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        // This asset is already larger than a 16-pixel tile sprite.
        return new GameObject(x, y, new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(1).build());
    }
}
