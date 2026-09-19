package MapEditor;

import Engine.Config;
import GameObject.ImageEffect;
import Level.Decoration;
import Level.DecorationLayer;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/*
 * The "Decorations" tab of the map editor's side panel.
 * Lets the user choose an image from the Resources folder (optionally cropping out just part of it, such as one sprite from a sprite sheet),
 * and set the scale, flip, and layer that decorations get placed with.
 * When a decoration on the map is selected, these controls edit that decoration instead.
 */
public class DecorationPicker extends JPanel {
    private static final String[] IMAGE_FILE_EXTENSIONS = { ".png", ".gif", ".jpg", ".jpeg", ".bmp" };
    private static final int[] CROP_GRID_SIZES = { 1, 8, 16, 24, 32 };
    private static final String[] CROP_GRID_NAMES = { "Free (1 px)", "8 px", "16 px", "24 px", "32 px" };
    private static final ImageEffect[] FLIP_EFFECTS = { ImageEffect.NONE, ImageEffect.FLIP_HORIZONTAL, ImageEffect.FLIP_VERTICAL, ImageEffect.FLIP_H_AND_V };
    private static final String[] FLIP_NAMES = { "None", "Horizontal", "Vertical", "Both" };

    // width the image preview is scaled to fit in
    private static final int PREVIEW_WIDTH = 148;

    private final DecorationEditorState decorationEditorState;
    private final TileBuilder tileBuilder;

    private JComboBox<String> imageComboBox;
    private SourceImagePreview sourceImagePreview;
    private JComboBox<String> cropGridComboBox;
    private JSpinner scaleSpinner;
    private JComboBox<String> flipComboBox;
    private JComboBox<DecorationLayer> layerComboBox;
    private JLabel selectedDecorationLabel;
    private JButton deleteButton;

    // true while controls are being updated to match the editor state, so their listeners know to ignore those changes
    private boolean updatingControls;

    // image file currently shown in the preview, used to only reload the preview when the image actually changes
    private String previewImageFileName;

    public DecorationPicker(DecorationEditorState decorationEditorState, TileBuilder tileBuilder) {
        this.decorationEditorState = decorationEditorState;
        this.tileBuilder = tileBuilder;
        setLayout(new BorderLayout(0, 4));
        setBackground(Colors.CORNFLOWER_BLUE);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        add(createImageChooser(), BorderLayout.NORTH);

        sourceImagePreview = new SourceImagePreview();
        JScrollPane previewScroll = new JScrollPane(sourceImagePreview);
        previewScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(previewScroll, BorderLayout.CENTER);

        add(createSettingsPanel(), BorderLayout.SOUTH);

        decorationEditorState.addChangeListener(this::refreshControls);

        loadImageFileNames();
        if (decorationEditorState.getImageFileName() == null && imageComboBox.getItemCount() > 0) {
            decorationEditorState.setImage(imageComboBox.getItemAt(0), null);
        }
        refreshControls();
    }

    private JPanel createImageChooser() {
        JPanel imageChooser = new JPanel(new BorderLayout(4, 2));
        imageChooser.setOpaque(false);

        imageChooser.add(new JLabel("Image (from Resources folder):"), BorderLayout.NORTH);

        imageComboBox = new JComboBox<>();
        imageComboBox.addActionListener(e -> {
            String imageFileName = (String) imageComboBox.getSelectedItem();
            // picking a different image starts off using the whole image
            if (!updatingControls && imageFileName != null && !imageFileName.equals(decorationEditorState.getImageFileName())) {
                decorationEditorState.setImage(imageFileName, null);
            }
        });
        imageChooser.add(imageComboBox, BorderLayout.CENTER);

        JButton reloadButton = new JButton("Reload");
        reloadButton.setMargin(new Insets(2, 4, 2, 4));
        reloadButton.setToolTipText("Re-read the Resources folder, picking up new or changed image files");
        reloadButton.addActionListener(e -> reloadImages());
        imageChooser.add(reloadButton, BorderLayout.EAST);

        JLabel cropHint = new JLabel("Click or drag below to crop:");
        cropHint.setFont(cropHint.getFont().deriveFont(11f));
        imageChooser.add(cropHint, BorderLayout.SOUTH);
        return imageChooser;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 0, 1, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        cropGridComboBox = new JComboBox<>(CROP_GRID_NAMES);
        cropGridComboBox.setSelectedIndex(2); // 16 px, the size of this game's tiles and most of its sprites
        cropGridComboBox.setToolTipText("Snaps the crop area in the image above to a grid of this size");
        cropGridComboBox.addActionListener(e -> sourceImagePreview.repaint());
        addSettingRow(settingsPanel, constraints, row++, "Crop grid", cropGridComboBox);

        JButton wholeImageButton = new JButton("Use whole image");
        wholeImageButton.setMargin(new Insets(2, 4, 2, 4));
        wholeImageButton.addActionListener(e -> decorationEditorState.setCrop(null));
        addFullWidthRow(settingsPanel, constraints, row++, wholeImageButton);

        scaleSpinner = new JSpinner(new SpinnerNumberModel(3.0, 0.5, 20.0, 0.5));
        scaleSpinner.setToolTipText("Map tiles in this game are drawn at a scale of 3");
        scaleSpinner.addChangeListener(e -> {
            if (!updatingControls) {
                decorationEditorState.setScale(((Number) scaleSpinner.getValue()).floatValue());
            }
        });
        addSettingRow(settingsPanel, constraints, row++, "Scale", scaleSpinner);

        flipComboBox = new JComboBox<>(FLIP_NAMES);
        flipComboBox.addActionListener(e -> {
            if (!updatingControls) {
                decorationEditorState.setImageEffect(FLIP_EFFECTS[flipComboBox.getSelectedIndex()]);
            }
        });
        addSettingRow(settingsPanel, constraints, row++, "Flip", flipComboBox);

        layerComboBox = new JComboBox<>(DecorationLayer.values());
        layerComboBox.setToolTipText("<html>Ground: under the player (flowers, rugs)<br>Depth sorted: player can walk behind or in front of it (barrels, signs)<br>Overhead: over the player (tree tops, roofs)<br>Ground and Depth sorted stay under tiles that cover the player, like tree trunks and roofs</html>");
        layerComboBox.addActionListener(e -> {
            if (!updatingControls) {
                decorationEditorState.setLayer((DecorationLayer) layerComboBox.getSelectedItem());
            }
        });
        addSettingRow(settingsPanel, constraints, row++, "Layer", layerComboBox);

        selectedDecorationLabel = new JLabel();
        selectedDecorationLabel.setFont(selectedDecorationLabel.getFont().deriveFont(11f));
        selectedDecorationLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
        addFullWidthRow(settingsPanel, constraints, row++, selectedDecorationLabel);

        deleteButton = new JButton("Delete selected");
        deleteButton.setMargin(new Insets(2, 4, 2, 4));
        deleteButton.addActionListener(e -> tileBuilder.deleteSelectedDecoration());
        addFullWidthRow(settingsPanel, constraints, row, deleteButton);

        return settingsPanel;
    }

    private void addSettingRow(JPanel panel, GridBagConstraints constraints, int row, String labelText, JComponent control) {
        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        panel.add(label, constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        panel.add(control, constraints);
    }

    private void addFullWidthRow(JPanel panel, GridBagConstraints constraints, int row, JComponent component) {
        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        panel.add(component, constraints);
    }

    // fills the image dropdown with every image file in the Resources folder (including subfolders)
    private void loadImageFileNames() {
        ArrayList<String> imageFileNames = new ArrayList<>();
        findImageFiles(new File(Config.RESOURCES_PATH), "", imageFileNames);
        imageFileNames.sort(String.CASE_INSENSITIVE_ORDER);

        updatingControls = true;
        try {
            imageComboBox.removeAllItems();
            for (String imageFileName : imageFileNames) {
                imageComboBox.addItem(imageFileName);
            }
        } finally {
            updatingControls = false;
        }
    }

    private static void findImageFiles(File folder, String pathPrefix, ArrayList<String> imageFileNames) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                findImageFiles(file, pathPrefix + file.getName() + "/", imageFileNames);
            }
            else if (isImageFile(file.getName())) {
                imageFileNames.add(pathPrefix + file.getName());
            }
        }
    }

    private static boolean isImageFile(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        for (String extension : IMAGE_FILE_EXTENSIONS) {
            if (lowerCaseFileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    // picks up new image files and changes to existing image files (for example after editing an image in an art program)
    private void reloadImages() {
        Decoration.clearImageCache();
        loadImageFileNames();
        previewImageFileName = null;
        tileBuilder.reloadDecorationImages();
        if (decorationEditorState.getImageFileName() != null) {
            // re-applying the image rebuilds the brush preview (and the selected decoration) from the reloaded image file
            decorationEditorState.setImage(decorationEditorState.getImageFileName(), decorationEditorState.getCrop());
        }
        else if (imageComboBox.getItemCount() > 0) {
            decorationEditorState.setImage(imageComboBox.getItemAt(0), null);
        }
        refreshControls();
    }

    // updates all controls to match the editor state
    private void refreshControls() {
        updatingControls = true;
        try {
            String imageFileName = decorationEditorState.getImageFileName();
            if (imageFileName != null && !isInImageComboBox(imageFileName)) {
                // a selected decoration may use an image that isn't in the Resources folder anymore, it still needs to show up here
                imageComboBox.addItem(imageFileName);
            }
            imageComboBox.setSelectedItem(imageFileName);

            if (imageFileName == null || !imageFileName.equals(previewImageFileName)) {
                previewImageFileName = imageFileName;
                sourceImagePreview.setImage(imageFileName == null ? null : Decoration.getSourceImage(imageFileName));
            }
            sourceImagePreview.repaint();

            scaleSpinner.setValue((double) decorationEditorState.getScale());
            for (int i = 0; i < FLIP_EFFECTS.length; i++) {
                if (FLIP_EFFECTS[i] == decorationEditorState.getImageEffect()) {
                    flipComboBox.setSelectedIndex(i);
                }
            }
            layerComboBox.setSelectedItem(decorationEditorState.getLayer());

            Decoration selectedDecoration = decorationEditorState.getSelectedDecoration();
            if (selectedDecoration != null) {
                selectedDecorationLabel.setText("<html>Editing selected decoration<br>at x " + Math.round(selectedDecoration.getX()) + ", y " + Math.round(selectedDecoration.getY()) + " (Esc to deselect)</html>");
            }
            else {
                selectedDecorationLabel.setText("<html>Nothing selected, settings<br>apply to new decorations</html>");
            }
            deleteButton.setEnabled(selectedDecoration != null);
        } finally {
            updatingControls = false;
        }
    }

    private boolean isInImageComboBox(String imageFileName) {
        for (int i = 0; i < imageComboBox.getItemCount(); i++) {
            if (imageComboBox.getItemAt(i).equals(imageFileName)) {
                return true;
            }
        }
        return false;
    }

    private int getCropGridSize() {
        return CROP_GRID_SIZES[cropGridComboBox.getSelectedIndex()];
    }

    /*
     * Shows the chosen image file zoomed in, with the crop area outlined in yellow.
     * Clicking picks a single grid cell of the image, and dragging picks a rectangle of grid cells.
     */
    private class SourceImagePreview extends JPanel {
        private BufferedImage image;
        private float zoom = 1;

        // image pixel the current crop drag started on, null if not dragging
        private Point dragStart;

        public SourceImagePreview() {
            setBackground(Color.DARK_GRAY);
            setImage(null);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (image != null && SwingUtilities.isLeftMouseButton(e)) {
                        dragStart = toImagePixel(e.getPoint());
                        cropToCells(dragStart, dragStart);
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStart != null) {
                        cropToCells(dragStart, toImagePixel(e.getPoint()));
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragStart = null;
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        public void setImage(BufferedImage image) {
            this.image = image;
            if (image == null) {
                setPreferredSize(new Dimension(PREVIEW_WIDTH, 40));
            }
            else {
                // small images get zoomed in to fill the preview (up to 8x), large ones get shrunk to fit
                zoom = Math.min(8f, PREVIEW_WIDTH / (float) image.getWidth());
                setPreferredSize(new Dimension(Math.round(image.getWidth() * zoom), Math.round(image.getHeight() * zoom)));
            }
            revalidate();
            repaint();
        }

        private Point toImagePixel(Point previewPoint) {
            int x = Math.max(0, Math.min(image.getWidth() - 1, (int)(previewPoint.x / zoom)));
            int y = Math.max(0, Math.min(image.getHeight() - 1, (int)(previewPoint.y / zoom)));
            return new Point(x, y);
        }

        // crops to the rectangle of grid cells covering both image pixels
        private void cropToCells(Point pixel1, Point pixel2) {
            int gridSize = getCropGridSize();
            int left = (Math.min(pixel1.x, pixel2.x) / gridSize) * gridSize;
            int top = (Math.min(pixel1.y, pixel2.y) / gridSize) * gridSize;
            int right = Math.min(image.getWidth(), (Math.max(pixel1.x, pixel2.x) / gridSize + 1) * gridSize);
            int bottom = Math.min(image.getHeight(), (Math.max(pixel1.y, pixel2.y) / gridSize + 1) * gridSize);
            Rectangle crop = new Rectangle(left, top, right - left, bottom - top);
            boolean isWholeImage = crop.x == 0 && crop.y == 0 && crop.width == image.getWidth() && crop.height == image.getHeight();
            Rectangle currentCrop = decorationEditorState.getCrop();
            Rectangle newCrop = isWholeImage ? null : crop;
            if (newCrop == null ? currentCrop != null : !newCrop.equals(currentCrop)) {
                decorationEditorState.setCrop(newCrop);
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            if (image == null) {
                g.setColor(Color.WHITE);
                g.drawString("No image", 8, 24);
                return;
            }

            int width = Math.round(image.getWidth() * zoom);
            int height = Math.round(image.getHeight() * zoom);

            // checkerboard behind the image so transparent areas are easy to see
            for (int y = 0; y < height; y += 8) {
                for (int x = 0; x < width; x += 8) {
                    g.setColor(((x + y) / 8) % 2 == 0 ? new Color(200, 200, 200) : new Color(150, 150, 150));
                    g.fillRect(x, y, Math.min(8, width - x), Math.min(8, height - y));
                }
            }
            g.drawImage(image, 0, 0, width, height, null);

            // faint grid lines, only when the grid cells are big enough on screen to be useful
            int gridSize = getCropGridSize();
            if (gridSize > 1 && gridSize * zoom >= 6) {
                g.setColor(new Color(255, 255, 255, 70));
                for (int x = gridSize; x < image.getWidth(); x += gridSize) {
                    g.drawLine(Math.round(x * zoom), 0, Math.round(x * zoom), height);
                }
                for (int y = gridSize; y < image.getHeight(); y += gridSize) {
                    g.drawLine(0, Math.round(y * zoom), width, Math.round(y * zoom));
                }
            }

            Rectangle crop = decorationEditorState.getCrop();
            if (crop == null) {
                crop = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            }
            g.setColor(Color.YELLOW);
            g.setStroke(new BasicStroke(2));
            g.drawRect(Math.round(crop.x * zoom) + 1, Math.round(crop.y * zoom) + 1, Math.round(crop.width * zoom) - 2, Math.round(crop.height * zoom) - 2);
        }
    }
}
