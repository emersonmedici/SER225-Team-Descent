package MapEditor;

import Lighting.Light;
import Lighting.PointLight;
import Lighting.SpotLight;

// shared by the light panel and the map canvas (same idea as SelectedTileIndexHolder)
public class LightEditorState {
    public enum Mode { TILES, LIGHTS }

    private Mode mode = Mode.TILES;
    private Light selectedLight;
    private boolean previewLighting = true;

    // called whenever the selection changes, so the light panel can show the selected light's values
    private Runnable selectionListener;

    // settings for the next light that gets placed (the light panel edits these)
    private boolean placeSpotLight = false;
    private float radiusTiles = 4f;
    private float intensity = 1f;
    private int steps = 6;
    private float aimX = 1, aimY = 0;

    // builds a new light at a world position using the current settings
    public Light createLight(float x, float y, int tileWidth) {
        Light light;
        if (placeSpotLight) {
            SpotLight spot = new SpotLight(x, y, radiusTiles * tileWidth, 30);
            spot.setDirection(aimX, aimY);
            light = spot;
        } else {
            light = new PointLight(x, y, radiusTiles * tileWidth);
        }
        light.setIntensity(intensity);
        light.setSteps(steps);
        return light;
    }

    public Mode getMode() { return mode; }
    public void setMode(Mode mode) { this.mode = mode; }

    public Light getSelectedLight() { return selectedLight; }
    public void setSelectedLight(Light light) {
        selectedLight = light;
        if (selectionListener != null) {
            selectionListener.run();
        }
    }

    public void setSelectionListener(Runnable selectionListener) { this.selectionListener = selectionListener; }

    public boolean isPreviewLighting() { return previewLighting; }
    public void setPreviewLighting(boolean previewLighting) { this.previewLighting = previewLighting; }

    public void setPlaceSpotLight(boolean placeSpotLight) { this.placeSpotLight = placeSpotLight; }
    public void setRadiusTiles(float radiusTiles) { this.radiusTiles = radiusTiles; }
    public void setIntensity(float intensity) { this.intensity = intensity; }
    public void setSteps(int steps) { this.steps = steps; }
    public void setAim(float aimX, float aimY) { this.aimX = aimX; this.aimY = aimY; }
}