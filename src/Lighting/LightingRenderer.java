package Lighting;

import Engine.GraphicsHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.List;

public class LightingRenderer {
    // how many screen pixels one art pixel covers (your tileset scale, 3)
    private final int pixelSize;

    // the small darkness image, and direct access to its pixels
    private BufferedImage lightmap;
    private int[] pixels;

    // how lit each cell is this frame, 0..1 (same layout as pixels)
    private float[] lightLevel;
    private int cols, rows;

    public LightingRenderer(int pixelSize) {
        this.pixelSize = pixelSize;
    }

    // view = the part of the world to light (world pixels)
    // camera = where the screen's top-left sits in the world
    public void render(GraphicsHandler graphicsHandler, List<Light> lights, float ambient, Color ambientColor,
                       int viewX, int viewY, int viewWidth, int viewHeight, int cameraX, int cameraY) {
        int P = pixelSize;

        // 1. snap the grid's top-left to the art-pixel grid, in WORLD space
        int originX = Math.floorDiv(viewX, P) * P;
        int originY = Math.floorDiv(viewY, P) * P;

        // enough cells to cover the view; +2 because snapping can shift the grid up/left
        ensureSize(viewWidth / P + 2, viewHeight / P + 2);

        // 2. every cell starts unlit
        Arrays.fill(lightLevel, 0f);

        // 3. each light brightens the cells it reaches
        for (Light light : lights) {
            if (light.isEnabled()) {
                addLight(light, originX, originY);
            }
        }

        // 4. light level -> darkness pixel
        int rgb = ambientColor.getRGB() & 0x00FFFFFF;       // keep the color, drop its alpha
        for (int i = 0; i < lightLevel.length; i++) {
            int alpha = Math.round(255 * ambient * (1f - lightLevel[i]));
            pixels[i] = (alpha << 24) | rgb;                // ARGB: alpha goes in the top 8 bits
        }

        // 5. stretch it over the screen with hard edges (no blur)
        Graphics2D g = graphicsHandler.getGraphics();
        Object oldHint = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(lightmap, originX - cameraX, originY - cameraY, cols * P, rows * P, null);
        if (oldHint != null) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldHint);
        }
    }

    private void addLight(Light light, int originX, int originY) {
        int P = pixelSize;

        // snap the light's center to the middle of an art pixel
        float lx = Math.floorDiv((int) light.getX(), P) * P + P / 2f;
        float ly = Math.floorDiv((int) light.getY(), P) * P + P / 2f;

        // only visit cells inside the light's bounding square, clamped to the grid
        float r = light.getRadius();
        int minCol = Math.max(0, (int) Math.floor((lx - r - originX) / P));
        int maxCol = Math.min(cols - 1, (int) Math.ceil((lx + r - originX) / P));
        int minRow = Math.max(0, (int) Math.floor((ly - r - originY) / P));
        int maxRow = Math.min(rows - 1, (int) Math.ceil((ly + r - originY) / P));

        for (int row = minRow; row <= maxRow; row++) {
            float wy = originY + row * P + P / 2f;          // world y of this cell's center
            for (int col = minCol; col <= maxCol; col++) {
                float wx = originX + col * P + P / 2f;      // world x of this cell's center
                float b = light.brightnessAt(wx - lx, wy - ly);
                int i = row * cols + col;
                // overlapping lights combine: each light removes part of the darkness that's left
                lightLevel[i] = 1f - (1f - lightLevel[i]) * (1f - b);
            }
        }
    }

    // build the image only when the size changes, not every frame
    private void ensureSize(int newCols, int newRows) {
        if (lightmap != null && newCols == cols && newRows == rows) {
            return;
        }
        cols = newCols;
        rows = newRows;
        lightmap = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) lightmap.getRaster().getDataBuffer()).getData();
        lightLevel = new float[cols * rows];
    }
}