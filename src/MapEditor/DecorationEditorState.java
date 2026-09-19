package MapEditor;

import GameObject.ImageEffect;
import Level.Decoration;
import Level.DecorationLayer;

import java.awt.*;
import java.util.ArrayList;

/*
 * Shared state for editing decorations in the map editor.
 * The Decorations tab (DecorationPicker) changes these settings, and the map (TileBuilder) uses them to place decorations.
 * The settings act as a "brush" for new decorations. When a decoration on the map is selected, its settings are loaded into the brush,
 * and any change to the brush is also applied to the selected decoration -- this is how an already placed decoration gets edited.
 */
public class DecorationEditorState {
    // true while the Decorations tab is showing, which switches clicking on the map from painting tiles to editing decorations
    private boolean decorationModeActive;

    // brush settings
    private String imageFileName;
    private Rectangle crop; // null means the whole image
    private float scale = 1;
    private ImageEffect imageEffect = ImageEffect.NONE;
    private DecorationLayer layer = DecorationLayer.GROUND;

    // decoration on the map currently being edited, null if there isn't one
    private Decoration selectedDecoration;

    // decoration built from the brush settings, used to show a preview of what will be placed under the mouse
    private Decoration brushPreview;

    private final ArrayList<Runnable> changeListeners = new ArrayList<>();

    // listeners are called any time anything in this class changes
    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    private void notifyChanged() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    // called any time the brush settings change
    private void brushChanged() {
        brushPreview = null;
        if (selectedDecoration != null) {
            selectedDecoration.setImage(imageFileName, crop);
            selectedDecoration.setScale(scale);
            selectedDecoration.setImageEffect(imageEffect);
            selectedDecoration.setLayer(layer);
        }
        notifyChanged();
    }

    // creates a new decoration at a map location using the brush settings, or returns null if no image has been chosen yet
    public Decoration createDecoration(float x, float y) {
        if (imageFileName == null) {
            return null;
        }
        return new Decoration(imageFileName, crop, x, y, scale, imageEffect, layer);
    }

    // decoration (not part of any map) showing what the brush will place, or null if no image has been chosen yet
    public Decoration getBrushPreview() {
        if (brushPreview == null) {
            brushPreview = createDecoration(0, 0);
        }
        return brushPreview;
    }

    public boolean isDecorationModeActive() { return decorationModeActive; }

    public void setDecorationModeActive(boolean decorationModeActive) {
        this.decorationModeActive = decorationModeActive;
        if (!decorationModeActive) {
            selectedDecoration = null;
        }
        notifyChanged();
    }

    public Decoration getSelectedDecoration() { return selectedDecoration; }

    // selects a decoration (or nothing if null), loading its settings into the brush so they can be edited
    public void setSelectedDecoration(Decoration decoration) {
        this.selectedDecoration = decoration;
        if (decoration != null) {
            imageFileName = decoration.getImageFileName();
            crop = decoration.getCrop();
            scale = decoration.getScale();
            imageEffect = decoration.getImageEffect();
            layer = decoration.getLayer();
            brushPreview = null;
        }
        notifyChanged();
    }

    // lets listeners know the selected decoration was changed from somewhere else (such as being moved on the map)
    public void selectedDecorationMoved() {
        notifyChanged();
    }

    public String getImageFileName() { return imageFileName; }
    public Rectangle getCrop() { return crop == null ? null : new Rectangle(crop); }

    // changes the brush image, crop can be null to use the whole image
    public void setImage(String imageFileName, Rectangle crop) {
        this.imageFileName = imageFileName;
        this.crop = crop == null ? null : new Rectangle(crop);
        brushChanged();
    }

    public void setCrop(Rectangle crop) {
        setImage(imageFileName, crop);
    }

    public float getScale() { return scale; }

    public void setScale(float scale) {
        this.scale = scale;
        brushChanged();
    }

    public ImageEffect getImageEffect() { return imageEffect; }

    public void setImageEffect(ImageEffect imageEffect) {
        this.imageEffect = imageEffect;
        brushChanged();
    }

    public DecorationLayer getLayer() { return layer; }

    public void setLayer(DecorationLayer layer) {
        this.layer = layer;
        brushChanged();
    }
}
