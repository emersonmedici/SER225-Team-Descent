package GameObject;

import java.awt.image.BufferedImage;
import Utils.ImageUtils;

// This class represents a Frame in an animation -- an array of Frames is an animation
// A frame is a sprite that has a delay value which specifies how many milliseconds to stay on this frame before transitioning to the next frame in an animation
public class Frame extends Sprite {
	private int delay;

	public Frame(BufferedImage image) {
		super(image, 0, 0);
	}

	public Frame(BufferedImage image, ImageEffect imageEffect, float scale, Rectangle bounds, int delay) {
		super(image, 0, 0, imageEffect);
		this.scale = scale;
		if (bounds != null) {
			setBounds(bounds);
		}
		this.delay = delay;
	}

	public Frame(BufferedImage image, ImageEffect imageEffect, float scale, Rectangle bounds) {
		super(image, 0, 0, imageEffect);
		this.scale = scale;
		if (bounds != null) {
			setBounds(bounds);
		}
	}

	public Frame rotated(int quarterTurns) {
		int turns = ((quarterTurns % 4) + 4) % 4;
		if (turns == 0) {
			return copy();
		}
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		Rectangle b = getBoundsDimensions();
		int bx = Math.round(b.getX1());
		int by = Math.round(b.getY1());
		int bw = b.getWidth();
		int bh = b.getHeight();
		Rectangle rotatedBounds;
		if (turns == 1) {
			rotatedBounds = new Rectangle(imageHeight - by - bh, bx, bh, bw);
		} else if (turns == 2) {
			rotatedBounds = new Rectangle(imageWidth - bx - bw, imageHeight - by - bh, bw, bh);
		} else {
			rotatedBounds = new Rectangle(by, imageWidth - bx - bw, bh, bw);
		}
		return new Frame(ImageUtils.rotateImage(image, turns), imageEffect, scale, rotatedBounds, delay);
	}

	public int getDelay() {
		return delay;
	}

	public Frame copy() {
		return new Frame(image, imageEffect, scale, getBoundsDimensions(), delay);
	}
}
