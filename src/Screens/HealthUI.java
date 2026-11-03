package Screens;

import Engine.GraphicsHandler;
import Engine.ScreenManager;
import Level.Player;
import java.awt.Color;

// Draws the player's health as a row of pixel hearts in the top-right corner of the screen,
// with their stamina bar underneath
public class HealthUI {
    // 1 = draw a pixel, 0 = leave blank
    private static final int[][] HEART_SHAPE = {
        {0,1,1,0,0,0,1,1,0},
        {1,1,1,1,0,1,1,1,1},
        {1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1},
        {0,1,1,1,1,1,1,1,0},
        {0,0,1,1,1,1,1,0,0},
        {0,0,0,1,1,1,0,0,0},
        {0,0,0,0,1,0,0,0,0}
    };
    private static final int PIXEL_SIZE = 4;     // each "pixel" of the heart is 4x4 screen pixels
    private static final int HEART_SPACING = 8;  // gap between hearts
    private static final int MARGIN_X = 16;      // gap between the last heart and the right edge
    private static final int START_Y = 16;
    private static final int STAMINA_GAP = 8;                    // gap between the hearts and the stamina bar
    private static final int STAMINA_HEIGHT = 2 * PIXEL_SIZE;    // 2 heart-pixels tall, so it matches the hearts' chunky look

    private final Color fullColor = new Color(220, 30, 40);
    private final Color emptyColor = new Color(60, 60, 60);
    private final Color staminaColor = new Color(240, 200, 40);
    private final Color exhaustedColor = new Color(140, 110, 30);  // dimmer yellow while the player can't sprint

    public void draw(GraphicsHandler graphicsHandler, Player player) {
        int heartWidth = HEART_SHAPE[0].length * PIXEL_SIZE;
        int maxHealth = player.getMaxHealth();

        // width of the whole row (hearts plus the gaps between them), so it can sit against the right edge
        int rowWidth = maxHealth * heartWidth + (maxHealth - 1) * HEART_SPACING;
        int startX = ScreenManager.getScreenWidth() - MARGIN_X - rowWidth;

        for (int i = 0; i < maxHealth; i++) {
            // hearts below current health are full, the rest are empty
            Color color = i < player.getHealth() ? fullColor : emptyColor;
            int heartX = startX + i * (heartWidth + HEART_SPACING);
            drawHeart(graphicsHandler, heartX, START_Y, color);
        }

        int staminaY = START_Y + HEART_SHAPE.length * PIXEL_SIZE + STAMINA_GAP;
        drawStaminaBar(graphicsHandler, player, startX, staminaY, rowWidth);
    }

    // a bar the same width as the row of hearts, filled in by how much stamina is left
    private void drawStaminaBar(GraphicsHandler graphicsHandler, Player player, int x, int y, int width) {
        float percentLeft = player.getStamina() / (float) player.getMaxStamina();
        // round down to a whole heart-pixel so the bar shrinks in chunks, like the hearts
        int fillWidth = (int) (width * percentLeft) / PIXEL_SIZE * PIXEL_SIZE;
        Color fillColor = player.isExhausted() ? exhaustedColor : staminaColor;

        graphicsHandler.drawFilledRectangle(x, y, width, STAMINA_HEIGHT, emptyColor);
        graphicsHandler.drawFilledRectangle(x, y, fillWidth, STAMINA_HEIGHT, fillColor);
    }

    private void drawHeart(GraphicsHandler graphicsHandler, int x, int y, Color color) {
        for (int row = 0; row < HEART_SHAPE.length; row++) {
            for (int col = 0; col < HEART_SHAPE[row].length; col++) {
                if (HEART_SHAPE[row][col] == 1) {
                    graphicsHandler.drawFilledRectangle(x + col * PIXEL_SIZE, y + row * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE, color);
                }
            }
        }
    }
}