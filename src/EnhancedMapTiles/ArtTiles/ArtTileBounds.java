package EnhancedMapTiles.ArtTiles;

import java.awt.image.BufferedImage;

import GameObject.ImageEffect;
import GameObject.Rectangle;

// Works out a hitbox that hugs the drawn part of an art tile's image,
// so the player collides with the object itself instead of its empty see-through corners
public class ArtTileBounds {

    // Returns the smallest box around every non-see-through pixel (in unscaled image pixels).
    // Returns null if the image is fully see-through, which leaves the default full-image hitbox.
    public static Rectangle fitToImage(BufferedImage image) {
        return fitToImage(image, ImageEffect.NONE);
    }

    // Same as above, but mirrors the box when the image is drawn flipped
    // (the engine doesn't flip a frame's bounds on its own)
    public static Rectangle fitToImage(BufferedImage image, ImageEffect imageEffect) {
        int left = image.getWidth();
        int top = image.getHeight();
        int right = -1;
        int bottom = -1;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = (image.getRGB(x, y) >>> 24) & 0xFF;
                if (alpha != 0) {
                    if (x < left) left = x;
                    if (x > right) right = x;
                    if (y < top) top = y;
                    if (y > bottom) bottom = y;
                }
            }
        }

        if (right == -1) {
            return null;
        }

        int width = right - left + 1;
        int height = bottom - top + 1;

        if (imageEffect == ImageEffect.FLIP_HORIZONTAL || imageEffect == ImageEffect.FLIP_H_AND_V) {
            left = image.getWidth() - left - width;
        }
        if (imageEffect == ImageEffect.FLIP_VERTICAL || imageEffect == ImageEffect.FLIP_H_AND_V) {
            top = image.getHeight() - top - height;
        }

        return new Rectangle(left, top, width, height);
    }
}
