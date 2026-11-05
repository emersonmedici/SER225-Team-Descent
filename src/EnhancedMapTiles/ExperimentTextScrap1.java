package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Scripts.PickupObjectScript;
import Utils.Point;
import java.awt.image.BufferedImage;

public class ExperimentTextScrap1 extends CarriableObject {
    private BufferedImage inspectionImage;

    public ExperimentTextScrap1(Point location) {
        super(location, loadSpriteSheet());
        setInteractScript(new PickupObjectScript());
    }

    private static SpriteSheet loadSpriteSheet() {
        BufferedImage image = ImageLoader.load("PaperIcon.png");
        return new SpriteSheet(image, image.getWidth(), image.getHeight());
    }

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        return new GameObject(x, y, new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(2).build());
    }

    @Override
    public boolean isInspectable() { return true; }

    @Override
    public String getInspectionTitle() { return "Experiment text scrap"; }

    @Override
    public BufferedImage getInspectionImage() {
        if (inspectionImage == null) {
            inspectionImage = ImageLoader.load("PaperText.png");
        }
        return inspectionImage;
    }
}
