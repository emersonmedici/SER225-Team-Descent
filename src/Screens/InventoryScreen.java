package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import EnhancedMapTiles.CarriableObject;
import GameObject.Frame;
import GameObject.GameObject;
import Level.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// inventory screen drawn over the map to show whatever items the player has in their inventory. The player can select an item to use or drop it.
public class InventoryScreen extends Screen {
    private final PlayLevelScreen playLevelScreen;
    private Font font;
    private Color backgroundColor;

    public InventoryScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
    }

    @Override
    public void initialize() {
        font = new Font("Arial", Font.BOLD, 14);
        backgroundColor = new Color(20, 20, 30, 220);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Resolve the current player because map transitions can create a new cat.
        Player player = playLevelScreen.player;
        for (int slot = 0; slot < player.getInventorySize(); slot++) {
            int x = 12 + slot * 46;
            Color borderColor = Color.GRAY;
            if (player.getSelectedInventorySlot() == slot) {
                borderColor = Color.YELLOW;
            }

            graphicsHandler.drawFilledRectangleWithBorder(
                    x, 10, 40, 48, backgroundColor, borderColor, 2);
            graphicsHandler.drawString(Integer.toString(slot + 1),
                    x + 5, 27, font, Color.WHITE);

            CarriableObject item = player.getInventoryItem(slot);
            if (item != null) {
                drawItemIcon(item, x + 4, 30, graphicsHandler);
            }
        }
        graphicsHandler.drawString("1-9: select   0: hands   Space: pick up / drop",
                12, 77, font, Color.WHITE);
    }

    private void drawItemIcon(CarriableObject item, int x, int y,
                              GraphicsHandler graphicsHandler) {
        GameObject sprite = item.getBottomLayer();
        Frame frame = sprite.getCurrentAnimation()[sprite.getCurrentFrameIndex()];
        int imageWidth = frame.getImage().getWidth();
        int imageHeight = frame.getImage().getHeight();
        // Fit the sprite below the slot number without stretching its proportions.
        float scale = Math.min(32f / imageWidth, 24f / imageHeight);
        int width = Math.max(1, Math.round(imageWidth * scale));
        int height = Math.max(1, Math.round(imageHeight * scale));

        Graphics2D graphics = graphicsHandler.getGraphics();
        RenderingHints previousHints = graphics.getRenderingHints();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphicsHandler.drawImage(frame.getImage(), x + (32 - width) / 2,
                y + (24 - height) / 2, width, height, frame.getImageEffect());
        graphics.setRenderingHints(previousHints);
    }
}
