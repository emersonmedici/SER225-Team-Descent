package NPCs;

import java.util.HashMap;

import Builders.FrameBuilder;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Engine.ImageLoader;
import Level.NPC;
import Utils.Point;

// The car that sits still in the prologue (what Dave.java used to draw)
// Car.java is the one that drives
public class ParkedCar extends NPC {

    public ParkedCar(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("car2.png"), 64, 64), "WALK_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 6))
                    .withScale(2)
                    .withBounds(12, 20, 30, 20)
                    .build()
            });
            put("STAND_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 6))
                    .withScale(2)
                    .withBounds(12, 20, 30, 20)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
            put("WALK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 6))
                    .withScale(2)
                    .withBounds(12, 20, 30, 20)
                    .build()
            });
            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 6))
                    .withScale(2)
                    .withBounds(12, 20, 30, 20)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
        }};
    }
}
