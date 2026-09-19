package MapEditor;

import Level.Map;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;

public class MapBuilder extends JPanel {
    private Map map;
    private JScrollPane tileBuilderScroll;
    private TileBuilder tileBuilder;
    private JLabel mapWidthLabel;
    private JLabel mapHeightLabel;
    private JLabel hoveredTileIndexLabel;

    public MapBuilder(SelectedTileIndexHolder controlPanelHolder, DecorationEditorState decorationEditorState) {
        setBackground(Colors.CORNFLOWER_BLUE);
        setLocation(205, 5);
        setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(null);
        labelPanel.setPreferredSize(new Dimension(200, 50));
        labelPanel.setBackground(Colors.CORNFLOWER_BLUE);
        mapWidthLabel = new JLabel("Width: ");
        mapWidthLabel.setSize(70, 20);
        mapWidthLabel.setLocation(2, 5);
        labelPanel.add(mapWidthLabel);
        mapHeightLabel = new JLabel("Height: ");
        mapHeightLabel.setSize(70, 20);
        mapHeightLabel.setLocation(76, 5);
        labelPanel.add(mapHeightLabel);
        hoveredTileIndexLabel = new JLabel("X: , Y:");
        hoveredTileIndexLabel.setSize(300, 20);
        hoveredTileIndexLabel.setLocation(152, 5);
        labelPanel.add(hoveredTileIndexLabel);

        // controls reminder, only shown while editing decorations
        JLabel decorationControlsLabel = new JLabel("Click: place/select    Drag: move (Shift snaps to tiles)    Right-click or Del: delete    Arrows: nudge");
        decorationControlsLabel.setFont(decorationControlsLabel.getFont().deriveFont(11f));
        decorationControlsLabel.setSize(700, 20);
        decorationControlsLabel.setLocation(2, 26);
        decorationControlsLabel.setVisible(false);
        labelPanel.add(decorationControlsLabel);
        decorationEditorState.addChangeListener(() -> decorationControlsLabel.setVisible(decorationEditorState.isDecorationModeActive()));

        add(labelPanel, BorderLayout.SOUTH);

        tileBuilder = new TileBuilder(controlPanelHolder, hoveredTileIndexLabel, decorationEditorState);
        tileBuilderScroll = new JScrollPane();
        tileBuilderScroll.setViewportView(tileBuilder);
        scrollToMaxY();
        tileBuilderScroll.setLocation(0, 0);
        tileBuilderScroll.setSize(585, 546);
        add(tileBuilderScroll, BorderLayout.CENTER);
    }

    public void setMap(Map map) {
        this.map = map;
        refreshTileBuilder();
    }

    public void refreshTileBuilder() {
        tileBuilder.setMap(map);
        tileBuilderScroll.setViewportView(tileBuilder);
        tileBuilderScroll.getVerticalScrollBar().setValue(tileBuilderScroll.getVerticalScrollBar().getMaximum());
        mapWidthLabel.setText("Width: " + map.getWidth());
        mapHeightLabel.setText("Height: " + map.getHeight());
    }

    public void scrollToMaxY() {
        tileBuilderScroll.getVerticalScrollBar().setValue(tileBuilderScroll.getVerticalScrollBar().getMaximum());
    }

    public TileBuilder getTileBuilder() { return tileBuilder; }
}
