package MapEditor;

import Level.Map;
import Lighting.Light;
import Lighting.SpotLight;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;

// the "inspector" for lights: edits the selected light, and the settings for the next light placed
public class LightPanel extends JPanel {
    private LightEditorState state;
    private Runnable onChange;   // repaints the map canvas
    private Map map;

    private JComboBox<String> typeBox = new JComboBox<>(new String[] { "Point", "Spot" });
    private JComboBox<String> aimBox = new JComboBox<>(new String[] { "Right", "Left", "Up", "Down", "Up-Right", "Up-Left", "Down-Right", "Down-Left" });
    private JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(4.0, 0.5, 30.0, 0.5));
    private JSpinner intensitySpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.1));
    private JSpinner stepsSpinner = new JSpinner(new SpinnerNumberModel(6, 1, 64, 1));
    private JSlider ambientSlider = new JSlider(0, 100, 0);
    private JCheckBox previewBox = new JCheckBox("Preview", true);
    private JLabel selectedLabel = new JLabel("Nothing selected");

    // aim directions, in the same order as aimBox's options
    private static final int[][] AIMS = { {1, 0}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {-1, -1}, {1, 1}, {-1, 1} };

    // true while copying a light's values INTO the controls, so the listeners don't write them back out
    private boolean loadingControls = false;

    public LightPanel(LightEditorState state, Runnable onChange) {
        this.state = state;
        this.onChange = onChange;
        setLayout(new BorderLayout());
        setBackground(Colors.CORNFLOWER_BLUE);

        // label / control pairs, two columns
        JPanel controls = new JPanel(new GridLayout(0, 2, 4, 6));
        controls.setBackground(Colors.CORNFLOWER_BLUE);
        controls.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        controls.add(new JLabel("Type"));
        controls.add(typeBox);
        controls.add(new JLabel("Radius (tiles)"));
        controls.add(radiusSpinner);
        controls.add(new JLabel("Intensity"));
        controls.add(intensitySpinner);
        controls.add(new JLabel("Steps"));
        controls.add(stepsSpinner);
        controls.add(new JLabel("Spot aim"));
        controls.add(aimBox);
        controls.add(new JLabel("Darkness"));
        controls.add(ambientSlider);
        controls.add(previewBox);
        controls.add(new JLabel(""));
        add(controls, BorderLayout.NORTH);

        JLabel help = new JLabel("<html>" +
                "Left click: place / select<br>" +
                "Drag: move selected<br>" +
                "Right click: delete<br><br>" +
                "Type only applies to new lights.<br>Save Map writes the .lights file.</html>");
        help.setVerticalAlignment(SwingConstants.TOP);
        help.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Colors.CORNFLOWER_BLUE);
        selectedLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        info.add(selectedLabel, BorderLayout.NORTH);
        info.add(help, BorderLayout.CENTER);
        add(info, BorderLayout.CENTER);

        // any light control change -> applyControls
        typeBox.addActionListener(e -> applyControls());
        aimBox.addActionListener(e -> applyControls());
        radiusSpinner.addChangeListener(e -> applyControls());
        intensitySpinner.addChangeListener(e -> applyControls());
        stepsSpinner.addChangeListener(e -> applyControls());

        // darkness belongs to the map, not to a light
        ambientSlider.addChangeListener(e -> {
            if (map != null && !loadingControls) {
                map.setAmbientDarkness(ambientSlider.getValue() / 100f);
                onChange.run();
            }
        });

        previewBox.addActionListener(e -> {
            state.setPreviewLighting(previewBox.isSelected());
            onChange.run();
        });

        // when the canvas selects a light, show its values here
        state.setSelectionListener(this::loadSelectedLight);
    }

    public void setMap(Map map) {
        this.map = map;
        loadingControls = true;
        ambientSlider.setValue(Math.round(map.getAmbientDarkness() * 100));
        loadingControls = false;
    }

    // controls -> next-light settings, and the selected light if there is one
    private void applyControls() {
        if (loadingControls) {
            return;
        }
        float radiusTiles = ((Number) radiusSpinner.getValue()).floatValue();
        float intensity = ((Number) intensitySpinner.getValue()).floatValue();
        int steps = ((Number) stepsSpinner.getValue()).intValue();
        int[] aim = AIMS[aimBox.getSelectedIndex()];

        state.setPlaceSpotLight(typeBox.getSelectedIndex() == 1);
        state.setRadiusTiles(radiusTiles);
        state.setIntensity(intensity);
        state.setSteps(steps);
        state.setAim(aim[0], aim[1]);

        Light light = state.getSelectedLight();
        if (light != null && map != null) {
            light.setRadius(radiusTiles * map.getTileset().getScaledSpriteWidth());
            light.setIntensity(intensity);
            light.setSteps(steps);
            if (light instanceof SpotLight) {
                ((SpotLight) light).setDirection(aim[0], aim[1]);
            }
        }
        onChange.run();
    }

    // selected light -> controls
    private void loadSelectedLight() {
        Light light = state.getSelectedLight();
        if (light == null || map == null) {
            selectedLabel.setText("Nothing selected");
            return;
        }

        loadingControls = true;
        selectedLabel.setText(light instanceof SpotLight ? "Spot light selected" : "Point light selected");
        typeBox.setSelectedIndex(light instanceof SpotLight ? 1 : 0);
        radiusSpinner.setValue((double) (light.getRadius() / map.getTileset().getScaledSpriteWidth()));
        intensitySpinner.setValue((double) light.getIntensity());
        stepsSpinner.setValue(light.getSteps());
        if (light instanceof SpotLight) {
            SpotLight spot = (SpotLight) light;
            int ax = Math.round(Math.signum(spot.getDirX()));
            int ay = Math.round(Math.signum(spot.getDirY()));
            for (int i = 0; i < AIMS.length; i++) {
                if (AIMS[i][0] == ax && AIMS[i][1] == ay) {
                    aimBox.setSelectedIndex(i);
                }
            }
        }
        ambientSlider.setValue(Math.round(map.getAmbientDarkness() * 100));
        loadingControls = false;

        // copy these values into the next-light settings, so the next light you place matches this one
        applyControls();
    }
}