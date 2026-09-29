package Utils;

import java.awt.*;
import java.awt.image.*;

// This class has some useful image methods that are used when loading in images to the game
public class ImageUtils {
	// changes desired color to be transparent (the chosen color will not be seen in game when drawn)
	public static BufferedImage transformColorToTransparency(BufferedImage image, Color transparentColor) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		int transparentColorIndex = transparentColor.getRGB();

		// iterates through each pixel of the image
		// if pixel is equal to the transparent color, changes that pixel to be fully transparent
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int rgb = image.getRGB(i, j);
				if (rgb == transparentColorIndex) {
					g.setColor(new Color(0, true));
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
				}
				else {
					g.setColor(new Color(rgb, false));
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
				}
				g.drawRect(i, j, 1, 1);
			}
		}
		g.dispose();
		return newImage;
	}

	public static BufferedImage rotateImage(BufferedImage image, int quarterTurns) {
    int turns = ((quarterTurns % 4) + 4) % 4;
    if (turns == 0) return image;
    int w = image.getWidth(), h = image.getHeight();
    boolean swap = turns % 2 == 1;
    BufferedImage out = new BufferedImage(swap ? h : w, swap ? w : h, BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < w; x++) {
        for (int y = 0; y < h; y++) {
            int rgb = image.getRGB(x, y);
            if (turns == 1)      out.setRGB(h - 1 - y, x, rgb);
            else if (turns == 2) out.setRGB(w - 1 - x, h - 1 - y, rgb);
            else                 out.setRGB(y, w - 1 - x, rgb);
        }
    }
    return out;
}

	// https://stackoverflow.com/a/4216315
	// resizes an image
	public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, image.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();
		return resized;
	}

	public static BufferedImage createSolidImage(Color color) {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		image = ImageUtils.transformColorToTransparency(image, color);
		return image;
	}

	public static BufferedImage createSolidImage(Color color, int width, int height) {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		image = ImageUtils.transformColorToTransparency(image, color);
		return resizeImage(image, width, height);
	}
}
