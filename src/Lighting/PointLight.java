package Lighting;

public class PointLight extends Light {

    public PointLight(float x, float y, float radius) {
        super(x, y, radius);   // the parent stores x, y, and radius
    }

    @Override
    public float brightnessAt(float dx, float dy) {
        // how far this pixel is from the light (Pythagorean theorem)
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        // 1 at the center, 0 at the edge, negative past the edge
        float t = 1f - dist / radius;
        if (t <= 0) {
            return 0;          // outside the circle, no light
        }

        // band() turns the smooth fade into steps; intensity dims the whole light
        return band(t) * intensity;
    }
}