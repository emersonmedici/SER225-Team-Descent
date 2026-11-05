package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Scripts.PickupObjectScript;
import Utils.Point;
import java.awt.image.BufferedImage;

// Shared pickup sprite and inspection behavior for the numbered journal pages.
public abstract class JournalEntry extends CarriableObject {
    private final int entryNumber;
    private BufferedImage inspectionImage;

    protected JournalEntry(Point location, int entryNumber) {
        super(location, loadSpriteSheet());
        this.entryNumber = entryNumber;
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
    public String getInspectionTitle() { return "Journal entry " + entryNumber; }

    @Override
    public BufferedImage getInspectionImage() {
        if (inspectionImage == null) {
            inspectionImage = ImageLoader.load("JournalEntry" + entryNumber + ".png");
        }
        return inspectionImage;
    }
}
