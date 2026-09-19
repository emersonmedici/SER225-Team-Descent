package Level;

import Engine.Config;
import Engine.GraphicsHandler;
import GameObject.ImageEffect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/*
 * A Decoration is a purely visual image that can be placed anywhere on a map, at any pixel position (it is NOT locked to the tile grid).
 * Decorations have no collision and cannot be interacted with -- they only exist to make maps look nicer.
 * They are placed using the "Decorations" tab of the Map Editor, and are saved in their own file next to the map's tile file (see DecorationFile).
 * An optional crop rectangle allows for using just one piece of a sprite sheet (such as a tileset) as a decoration's image.
 */
public class Decoration {
    // image file name, relative to the Resources folder (for example "Tree.png" or "Decorations/Flower.png")
    private String imageFileName;

    // area of the image file to use (in the image file's own pixels) -- null means the whole image is used
    private Rectangle crop;

    // location on the map in map pixels (the same coordinate space map tiles, npcs, and the player use)
    private float x, y;

    private float scale;
    private ImageEffect imageEffect;
    private DecorationLayer layer;

    // the final (cropped) image that gets drawn
    private BufferedImage image;

    // the map this decoration is a part of, needed so it can be drawn relative to the map's camera
    private Map map;

    // image files are cached so many decorations using the same image file only load it once
    private static final HashMap<String, BufferedImage> sourceImageCache = new HashMap<>();

    public Decoration(String imageFileName, Rectangle crop, float x, float y, float scale, ImageEffect imageEffect, DecorationLayer layer) {
        this.imageFileName = imageFileName;
        this.crop = crop == null ? null : new Rectangle(crop);
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.imageEffect = imageEffect;
        this.layer = layer;
        loadImage();
    }

    // builds the image that will be drawn from the image file and crop rectangle
    // if the image file can't be loaded, a placeholder is used so the decoration isn't lost and it's obvious something is wrong
    private void loadImage() {
        BufferedImage sourceImage = getSourceImage(imageFileName);
        if (sourceImage == null) {
            image = createMissingImagePlaceholder();
            return;
        }
        if (crop == null) {
            image = sourceImage;
            return;
        }

        // the image file may have been changed since this decoration was placed, so make sure the crop area is still inside of it
        Rectangle usableCrop = crop.intersection(new Rectangle(0, 0, sourceImage.getWidth(), sourceImage.getHeight()));
        if (usableCrop.isEmpty()) {
            System.out.println("Decoration crop area " + formatCrop(crop) + " is outside of image " + imageFileName + ", using placeholder image instead");
            image = createMissingImagePlaceholder();
            return;
        }
        image = sourceImage.getSubimage(usableCrop.x, usableCrop.y, usableCrop.width, usableCrop.height);
    }

    // loads an image file from the Resources folder (or gets it from the cache if it was already loaded)
    // returns null if the image can't be loaded
    public static BufferedImage getSourceImage(String imageFileName) {
        if (sourceImageCache.containsKey(imageFileName)) {
            return sourceImageCache.get(imageFileName);
        }

        BufferedImage loadedImage = null;
        try {
            BufferedImage fileImage = ImageIO.read(new File(Config.RESOURCES_PATH + imageFileName));
            if (fileImage != null) {
                loadedImage = applyTransparency(fileImage);
            }
            else {
                System.out.println("Decoration image " + Config.RESOURCES_PATH + imageFileName + " is not a supported image format");
            }
        } catch (IOException e) {
            System.out.println("Unable to find decoration image " + Config.RESOURCES_PATH + imageFileName);
        }

        // a failed load is also cached (as null) so the warning above only gets printed once per image file
        sourceImageCache.put(imageFileName, loadedImage);
        return loadedImage;
    }

    // forgets all loaded image files, so they will be reloaded from disk the next time they're needed
    // the map editor uses this to pick up changes made to image files while it's open
    public static void clearImageCache() {
        sourceImageCache.clear();
    }

    // unlike ImageLoader, this keeps the alpha (transparency) value of each pixel, so images with real transparency work as expected
    // pixels that are exactly the game's transparent color (magenta) are also made transparent, to match how the rest of the game's art works
    private static BufferedImage applyTransparency(BufferedImage fileImage) {
        int transparentColor = Config.TRANSPARENT_COLOR.getRGB();
        BufferedImage newImage = new BufferedImage(fileImage.getWidth(), fileImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < fileImage.getWidth(); i++) {
            for (int j = 0; j < fileImage.getHeight(); j++) {
                int argb = fileImage.getRGB(i, j);
                newImage.setRGB(i, j, argb == transparentColor ? 0 : argb);
            }
        }
        return newImage;
    }

    // a magenta box with an X through it, used when a decoration's image can't be loaded
    private static BufferedImage createMissingImagePlaceholder() {
        BufferedImage placeholder = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, 16, 16);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 15, 15);
        g.drawLine(0, 0, 15, 15);
        g.drawLine(15, 0, 0, 15);
        g.dispose();
        return placeholder;
    }

    public static String formatCrop(Rectangle crop) {
        return crop.x + "," + crop.y + "," + crop.width + "," + crop.height;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawImage(image, Math.round(getCalibratedX()), Math.round(getCalibratedY()), getWidth(), getHeight(), imageEffect);
    }

    // gets x location taking into account map camera position
    public float getCalibratedX() {
        return map != null ? Math.round(x) - map.getCamera().getX() : Math.round(x);
    }

    // gets y location taking into account map camera position
    public float getCalibratedY() {
        return map != null ? Math.round(y) - map.getCamera().getY() : Math.round(y);
    }

    // y position of the bottom of the decoration, which is what depth sorting against the player and npcs is based on
    public float getSortY() {
        return y + getHeight();
    }

    // checks if a point on the map (in map pixels) is inside this decoration's rectangle
    public boolean contains(float pointX, float pointY) {
        return pointX >= x && pointX < x + getWidth() && pointY >= y && pointY < y + getHeight();
    }

    // checks if a point on the map (in map pixels) lands on a visible (non transparent) pixel of this decoration
    // the map editor uses this so clicking the empty space around a decoration's image doesn't select it
    public boolean isOpaqueAt(float pointX, float pointY) {
        if (!contains(pointX, pointY)) {
            return false;
        }
        int imageX = Math.min(image.getWidth() - 1, (int)((pointX - x) / scale));
        int imageY = Math.min(image.getHeight() - 1, (int)((pointY - y) / scale));
        if (imageEffect == ImageEffect.FLIP_HORIZONTAL || imageEffect == ImageEffect.FLIP_H_AND_V) {
            imageX = image.getWidth() - 1 - imageX;
        }
        if (imageEffect == ImageEffect.FLIP_VERTICAL || imageEffect == ImageEffect.FLIP_H_AND_V) {
            imageY = image.getHeight() - 1 - imageY;
        }
        return (image.getRGB(imageX, imageY) >>> 24) != 0;
    }

    public String getImageFileName() { return imageFileName; }

    // returns a copy of the crop rectangle, or null if the whole image is used
    public Rectangle getCrop() { return crop == null ? null : new Rectangle(crop); }

    // changes the image this decoration uses, crop can be null to use the whole image
    public void setImage(String imageFileName, Rectangle crop) {
        this.imageFileName = imageFileName;
        this.crop = crop == null ? null : new Rectangle(crop);
        loadImage();
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void moveX(float dx) { this.x += dx; }
    public void moveY(float dy) { this.y += dy; }

    public int getWidth() { return Math.round(image.getWidth() * scale); }
    public int getHeight() { return Math.round(image.getHeight() * scale); }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }

    public ImageEffect getImageEffect() { return imageEffect; }
    public void setImageEffect(ImageEffect imageEffect) { this.imageEffect = imageEffect; }

    public DecorationLayer getLayer() { return layer; }
    public void setLayer(DecorationLayer layer) { this.layer = layer; }

    public Map getMap() { return map; }
    public void setMap(Map map) { this.map = map; }
}
