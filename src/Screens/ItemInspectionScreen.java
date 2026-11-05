package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Engine.ScreenManager;
import EnhancedMapTiles.CarriableObject;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ItemInspectionScreen extends Screen {
    private BufferedImage image;
    private String title;
    private Font titleFont;
    private Font controlsFont;
    private KeyLocker keyLocker;
    private boolean open;

    @Override
    public void initialize() {
        image = null;
        open = false;
        keyLocker = new KeyLocker();
        titleFont = new Font("Arial", Font.BOLD, 22);
        controlsFont = new Font("Arial", Font.PLAIN, 16);
    }

    public void open(CarriableObject item) {
        if (item == null || !item.isInspectable()) return;
        image = item.getInspectionImage();
        title = item.getInspectionTitle();
        open = true;
        keyLocker.lockKey(Key.E);
        keyLocker.lockKey(Key.ESC);
    }

    public boolean isOpen() { return open; }

    @Override
    public void update() {
        for (Key key : new Key[] {Key.E, Key.ESC}) {
            if (Keyboard.isKeyUp(key)) keyLocker.unlockKey(key);
            else if (!keyLocker.isKeyLocked(key)) {
                open = false;
                keyLocker.lockKey(key);
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (!open) return;
        int screenWidth = ScreenManager.getScreenWidth();
        int screenHeight = ScreenManager.getScreenHeight();
        int panelWidth = Math.min(640, screenWidth - 32);
        int panelHeight = Math.min(540, screenHeight - 32);
        int panelX = (screenWidth - panelWidth) / 2;
        int panelY = (screenHeight - panelHeight) / 2;
        graphicsHandler.drawFilledRectangle(0, 0, screenWidth, screenHeight, new Color(0, 0, 0, 60));
        graphicsHandler.drawFilledRectangleWithBorder(panelX, panelY, panelWidth,panelHeight, new Color(25, 25, 32), Color.LIGHT_GRAY, 2);
        graphicsHandler.drawString(title, panelX + 20, panelY + 32, titleFont, Color.WHITE);

        float scale = Math.min((panelWidth - 40f) / image.getWidth(),
                (panelHeight - 110f) / image.getHeight());
        if (scale >= 1) scale = (float) Math.floor(scale);
        int width = Math.max(1, Math.round(image.getWidth() * scale));
        int height = Math.max(1, Math.round(image.getHeight() * scale));
        Graphics2D graphics = graphicsHandler.getGraphics();
        RenderingHints previousHints = graphics.getRenderingHints();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphicsHandler.drawImage(image, panelX + (panelWidth - width) / 2,
                panelY + 50 + (panelHeight - 110 - height) / 2, width, height);
        graphics.setRenderingHints(previousHints);
        graphicsHandler.drawString("E or Escape: close", panelX + 20,
                panelY + panelHeight - 20, controlsFont, Color.WHITE);
    }
}
