package Utils;

// Represents a 2D direction, which can be either left, right, up, or down
// each direction is given a property for velocity, which is based on screen coordinates (e.g. going downwards adds 1 to Y, so DOWN has a velocity of 1)
public enum Direction {
	LEFT(-1, 0), RIGHT(1, 0), UP(0, -1), DOWN(0, 1), NONE(0, 0),
	UPLEFT(-1, -1), UPRIGHT(-1, 1), DOWNLEFT(-1, 1), DOWNRIGHT(1, 1);

	private int velocityX;
	private int velocityY;

	Direction(int velocityX, int velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	public int getVelocityX() {
		return this.velocityX;
	}

	public int getVelocityY() {
		return this.velocityY;
	}


}
