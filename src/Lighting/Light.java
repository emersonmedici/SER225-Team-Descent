package Lighting;

public abstract class Light {
    protected float x, y;            // center, world pixels
    protected float radius;          // world pixels
    protected float intensity = 1f;  // 0..1
    protected int steps = 6;         // number of brightness bands
    protected boolean enabled = true;

    public Light(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    // brightness 0..1 at offset (dx, dy) in world px from the center
    public abstract float brightnessAt(float dx, float dy);

    protected float band(float t) {  // snaps a smooth 0..1 value to a step
        return (float) Math.ceil(t * steps) / steps;
    }

// Map, Player, and the editor are in other packages, so they need these
public void setPosition(float x, float y) { this.x = x; this.y = y; }
public float getX() { return x; }
public float getY() { return y; }
public float getRadius() { return radius; }
public void setRadius(float radius) { this.radius = radius; }
public float getIntensity() { return intensity; }
public void setIntensity(float intensity) { this.intensity = intensity; }
public int getSteps() { return steps; }
public void setSteps(int steps) { this.steps = Math.max(1, steps); } // 0 steps would divide by zero in band()
public boolean isEnabled() { return enabled; }
public void setEnabled(boolean enabled) { this.enabled = enabled; }
}