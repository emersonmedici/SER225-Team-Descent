package Lighting;

public class SpotLight extends Light {
    // which way the beam points; always kept at length 1 (see setDirection)
    protected float dirX = 1, dirY = 0;

    // cosine of half the cone's width, computed once instead of every pixel
    protected float cosHalfAngle;
    protected float halfAngleDegrees;

    public float getHalfAngle() { return halfAngleDegrees; }
    public float getDirX() { return dirX; }
    public float getDirY() { return dirY; }

    public SpotLight(float x, float y, float radius, float halfAngleDegrees) {
        super(x, y, radius);
        setHalfAngle(halfAngleDegrees);
    }

    // halfAngle 30 means the beam is 60 degrees wide in total
    public void setHalfAngle(float degrees) {
        
        this.halfAngleDegrees = degrees;
        cosHalfAngle = (float) Math.cos(Math.toRadians(degrees));
        
    }

    public void setDirection(float aimX, float aimY) {
        float length = (float) Math.sqrt(aimX * aimX + aimY * aimY);
        if (length == 0) {
            return;            // (0, 0) has no direction, so keep pointing where we were
        }
        // divide by the length so the direction has length 1
        // e.g. diagonal (1, -1) becomes (0.707, -0.707)
        dirX = aimX / length;
        dirY = aimY / length;
    }

    @Override
    public float brightnessAt(float dx, float dy) {
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) {
            return intensity;  // the light's own pixel; also avoids dividing by 0 below
        }

        // dot product divided by distance = how directly in front this pixel is
        // 1 = straight ahead, 0 = off to the side, -1 = behind
        float cos = (dx * dirX + dy * dirY) / dist;
        if (cos < cosHalfAngle) {
            return 0;          // outside the cone
        }

        // from here on it's the same distance fade as PointLight
        float t = 1f - dist / radius;
        if (t <= 0) {
            return 0;
        }

        // 0 at the cone's side, 1 down the middle; fades the beam's edges
        float edge = (cos - cosHalfAngle) / (1f - cosHalfAngle);
        return band(t * Math.min(1f, edge * 2f)) * intensity;
    }
}