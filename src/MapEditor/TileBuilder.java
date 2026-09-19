package MapEditor;

import Engine.GraphicsHandler;
import Level.*;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Comparator;

public class TileBuilder extends JPanel {
    private Map map;
    private MapTile hoveredMapTile;
    private SelectedTileIndexHolder controlPanelHolder;
    private GraphicsHandler graphicsHandler = new GraphicsHandler();
    private JLabel hoveredTileIndexLabel;
    private boolean showNPCs;
    private boolean showEnhancedMapTiles;
    private boolean showTriggers;
    private boolean showDecorations = true;

    // decoration editing (used while the Decorations tab is showing)
    private DecorationEditorState decorationEditorState;
    private Point mousePosition; // null when the mouse is not over the map
    private boolean shiftHeld;
    private Decoration hoveredDecoration;
    private Decoration draggedDecoration;
    private float dragOffsetX, dragOffsetY;

    public TileBuilder(SelectedTileIndexHolder controlPanelHolder, JLabel hoveredTileIndexLabel, DecorationEditorState decorationEditorState) {
        setBackground(Colors.MAGENTA);
        setLocation(0, 0);
        setPreferredSize(new Dimension(585, 562));
        setFocusable(true);
        this.controlPanelHolder = controlPanelHolder;
        this.hoveredTileIndexLabel = hoveredTileIndexLabel;
        this.decorationEditorState = decorationEditorState;
        decorationEditorState.addChangeListener(this::repaint);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredMapTile = null;
                hoveredDecoration = null;
                mousePosition = null;
                hoveredTileIndexLabel.setText("");
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // lets this panel receive key presses (such as Delete) after being clicked on
                requestFocusInWindow();
                if (isDecorationMode()) {
                    decorationMousePressed(e);
                }
                else {
                    tileSelected(e.getPoint());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedDecoration = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) { }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isDecorationMode()) {
                    decorationMouseMoved(e);
                }
                else {
                    tileHovered(e.getPoint());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDecorationMode()) {
                    decorationMouseDragged(e);
                }
                else {
                    tileHovered(e.getPoint());
                    tileSelected(e.getPoint());
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isDecorationMode()) {
                    decorationKeyPressed(e);
                }
            }
        });
    }

    public void setMap(Map map) {
        this.map = map;
        hoveredDecoration = null;
        draggedDecoration = null;
        decorationEditorState.setSelectedDecoration(null);
        setPreferredSize(new Dimension(map.getWidthPixels(), map.getHeightPixels()));
        repaint();
    }

    // draws everything in the same order the game does, so the map editor shows what the map will look like in game
    public void draw() {
        boolean drawDecorations = showDecorations || isDecorationMode();

        for (MapTile tile : map.getMapTiles()) {
            tile.drawBottomLayer(graphicsHandler);
        }

        if (drawDecorations) {
            drawDecorationsOnLayer(DecorationLayer.GROUND);
        }

        if (showEnhancedMapTiles) {
            for (EnhancedMapTile enhancedMapTile : map.getEnhancedMapTiles()) {
                enhancedMapTile.draw(graphicsHandler);
            }
        }

        if (showNPCs) {
            for (NPC npc : map.getNPCs()) {
                npc.draw(graphicsHandler);
            }
        }

        if (drawDecorations) {
            drawDecorationsOnLayer(DecorationLayer.DEPTH_SORTED);
        }

        for (MapTile tile : map.getMapTiles()) {
            if (tile.getTopLayer() != null) {
                tile.drawTopLayer(graphicsHandler);
            }
        }

        if (drawDecorations) {
            drawDecorationsOnLayer(DecorationLayer.OVERHEAD);
        }

        if (showTriggers) {
            for (Trigger trigger : map.getTriggers()) {
                trigger.draw(graphicsHandler, new Color(255, 0, 255, 100));
            }
        }

        if (isDecorationMode()) {
            drawDecorationEditingOverlay();
        }
        else if (hoveredMapTile != null) {
            graphicsHandler.drawRectangle(
                    Math.round(hoveredMapTile.getX()) + 2,
                    Math.round(hoveredMapTile.getY()) + 2,
                    hoveredMapTile.getWidth() - 5,
                    hoveredMapTile.getHeight() - 5,
                    Color.YELLOW,
                    5
            );
        }
    }

    private void drawDecorationsOnLayer(DecorationLayer layer) {
        for (Decoration decoration : getDecorationsInDrawOrder()) {
            if (decoration.getLayer() == layer) {
                decoration.draw(graphicsHandler);
            }
        }
    }

    // outlines the hovered and selected decorations, and shows a see-through preview of what will be placed under the mouse
    private void drawDecorationEditingOverlay() {
        Decoration selectedDecoration = decorationEditorState.getSelectedDecoration();
        if (hoveredDecoration != null && hoveredDecoration != selectedDecoration) {
            drawDecorationOutline(hoveredDecoration, Color.WHITE, 1);
        }
        if (selectedDecoration != null) {
            drawDecorationOutline(selectedDecoration, Color.CYAN, 2);
        }

        Decoration brushPreview = decorationEditorState.getBrushPreview();
        if (mousePosition != null && hoveredDecoration == null && draggedDecoration == null && brushPreview != null) {
            positionCenteredOnMouse(brushPreview, mousePosition, shiftHeld);
            Graphics2D g = graphicsHandler.getGraphics();
            Composite previousComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            brushPreview.draw(graphicsHandler);
            g.setComposite(previousComposite);
            drawDecorationOutline(brushPreview, new Color(255, 255, 0, 160), 1);
        }
    }

    private void drawDecorationOutline(Decoration decoration, Color color, int thickness) {
        graphicsHandler.drawRectangle(
                Math.round(decoration.getX()) - thickness,
                Math.round(decoration.getY()) - thickness,
                decoration.getWidth() + thickness,
                decoration.getHeight() + thickness,
                color,
                thickness
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphicsHandler.setGraphics((Graphics2D) g);
        draw();
    }

    public void tileSelected(Point selectedPoint) {
        int selectedTileIndex = getSelectedTileIndex(selectedPoint);
        if (selectedTileIndex != -1) {
            MapTile oldMapTile = map.getMapTiles()[selectedTileIndex];
            MapTile newMapTile =  map.getTileset().getTile(controlPanelHolder.getSelectedTileIndex()).build(oldMapTile.getX(), oldMapTile.getY());
            newMapTile.setMap(map);
            map.getMapTiles()[selectedTileIndex] = newMapTile;

        }
        repaint();
    }

    public void tileHovered(Point hoveredPoint) {
        this.hoveredMapTile = getHoveredTile(hoveredPoint);
        if (this.hoveredMapTile != null) {
            int hoveredIndexX = Math.round(this.hoveredMapTile.getX()) / map.getTileset().getScaledSpriteWidth();
            int hoveredIndexY = Math.round(this.hoveredMapTile.getY()) / map.getTileset().getScaledSpriteHeight();
            hoveredTileIndexLabel.setText("X: " + hoveredIndexX + ", Y: " + hoveredIndexY);
            repaint();
        }
    }

    protected MapTile getHoveredTile(Point mousePoint) {
        for (MapTile mapTile : map.getMapTiles()) {
            if (isPointInTile(mousePoint, mapTile)) {
                return mapTile;
            }
        }
        return null;
    }

    protected int getSelectedTileIndex(Point mousePoint) {
        MapTile[] mapTiles = map.getMapTiles();
        for (int i = 0; i < mapTiles.length; i++) {
            if (isPointInTile(mousePoint, mapTiles[i])) {
                return i;
            }
        }
        return -1;
    }

    protected boolean isPointInTile(Point point, MapTile tile) {
        return (point.x >= tile.getX() && point.x <= tile.getX() + tile.getWidth() &&
                point.y >= tile.getY() && point.y <= tile.getY() + tile.getHeight());
    }

    private boolean isDecorationMode() {
        return decorationEditorState.isDecorationModeActive();
    }

    // left click on a decoration selects it (and starts dragging it), left click on empty space places a new decoration
    // right click on a decoration deletes it
    private void decorationMousePressed(MouseEvent e) {
        Point point = e.getPoint();
        shiftHeld = e.isShiftDown();
        Decoration clickedDecoration = getDecorationAt(point);

        if (SwingUtilities.isRightMouseButton(e)) {
            if (clickedDecoration != null) {
                deleteDecoration(clickedDecoration);
            }
            return;
        }
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        if (clickedDecoration == null) {
            clickedDecoration = decorationEditorState.createDecoration(0, 0);
            if (clickedDecoration == null) {
                return; // no image has been chosen yet
            }
            positionCenteredOnMouse(clickedDecoration, point, shiftHeld);
            map.addDecoration(clickedDecoration);
        }

        decorationEditorState.setSelectedDecoration(clickedDecoration);
        draggedDecoration = clickedDecoration;
        dragOffsetX = point.x - clickedDecoration.getX();
        dragOffsetY = point.y - clickedDecoration.getY();
        hoveredDecoration = clickedDecoration;
        updateDecorationModeLabel(point);
        repaint();
    }

    private void decorationMouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
        shiftHeld = e.isShiftDown();
        hoveredDecoration = getDecorationAt(mousePosition);
        updateDecorationModeLabel(mousePosition);
        repaint();
    }

    private void decorationMouseDragged(MouseEvent e) {
        mousePosition = e.getPoint();
        shiftHeld = e.isShiftDown();
        if (draggedDecoration != null) {
            positionDecoration(draggedDecoration, mousePosition.x - dragOffsetX, mousePosition.y - dragOffsetY, shiftHeld);
            decorationEditorState.selectedDecorationMoved();
        }
        updateDecorationModeLabel(mousePosition);
        repaint();
    }

    private void decorationKeyPressed(KeyEvent e) {
        Decoration selectedDecoration = decorationEditorState.getSelectedDecoration();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_BACK_SPACE:
                deleteSelectedDecoration();
                e.consume();
                return;
            case KeyEvent.VK_ESCAPE:
                decorationEditorState.setSelectedDecoration(null);
                e.consume();
                return;
        }

        // arrow keys nudge the selected decoration by 1 pixel, or by a whole tile while holding shift
        if (selectedDecoration == null) {
            return;
        }
        int stepX = e.isShiftDown() ? map.getTileset().getScaledSpriteWidth() : 1;
        int stepY = e.isShiftDown() ? map.getTileset().getScaledSpriteHeight() : 1;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                selectedDecoration.moveX(-stepX);
                break;
            case KeyEvent.VK_RIGHT:
                selectedDecoration.moveX(stepX);
                break;
            case KeyEvent.VK_UP:
                selectedDecoration.moveY(-stepY);
                break;
            case KeyEvent.VK_DOWN:
                selectedDecoration.moveY(stepY);
                break;
            default:
                return;
        }
        // consuming the key press stops the scroll pane from also scrolling the map
        e.consume();
        decorationEditorState.selectedDecorationMoved();
    }

    private void positionCenteredOnMouse(Decoration decoration, Point mousePoint, boolean snapToTiles) {
        positionDecoration(decoration, mousePoint.x - decoration.getWidth() / 2f, mousePoint.y - decoration.getHeight() / 2f, snapToTiles);
    }

    // moves a decoration to a whole pixel location, or to the nearest tile corner when snapping
    private void positionDecoration(Decoration decoration, float x, float y, boolean snapToTiles) {
        if (snapToTiles) {
            int tileWidth = map.getTileset().getScaledSpriteWidth();
            int tileHeight = map.getTileset().getScaledSpriteHeight();
            decoration.setLocation(Math.round(x / tileWidth) * tileWidth, Math.round(y / tileHeight) * tileHeight);
        }
        else {
            decoration.setLocation(Math.round(x), Math.round(y));
        }
    }

    private void updateDecorationModeLabel(Point point) {
        int tileX = point.x / map.getTileset().getScaledSpriteWidth();
        int tileY = point.y / map.getTileset().getScaledSpriteHeight();
        hoveredTileIndexLabel.setText("X: " + tileX + ", Y: " + tileY + "   (pixel " + point.x + ", " + point.y + ")");
    }

    // gets the top-most decoration with a visible pixel at a point on the map, or null if there isn't one
    private Decoration getDecorationAt(Point point) {
        ArrayList<Decoration> decorationsInDrawOrder = getDecorationsInDrawOrder();
        for (int i = decorationsInDrawOrder.size() - 1; i >= 0; i--) {
            if (decorationsInDrawOrder.get(i).isOpaqueAt(point.x, point.y)) {
                return decorationsInDrawOrder.get(i);
            }
        }
        return null;
    }

    // map's decorations in the order they get drawn: ground layer, then depth sorted layer (top of the map to bottom), then overhead layer
    private ArrayList<Decoration> getDecorationsInDrawOrder() {
        ArrayList<Decoration> ground = new ArrayList<>();
        ArrayList<Decoration> depthSorted = new ArrayList<>();
        ArrayList<Decoration> overhead = new ArrayList<>();
        for (Decoration decoration : map.getDecorations()) {
            switch (decoration.getLayer()) {
                case GROUND:
                    ground.add(decoration);
                    break;
                case DEPTH_SORTED:
                    depthSorted.add(decoration);
                    break;
                case OVERHEAD:
                    overhead.add(decoration);
                    break;
            }
        }
        depthSorted.sort(Comparator.comparingDouble(Decoration::getSortY));

        ArrayList<Decoration> decorationsInDrawOrder = new ArrayList<>(ground);
        decorationsInDrawOrder.addAll(depthSorted);
        decorationsInDrawOrder.addAll(overhead);
        return decorationsInDrawOrder;
    }

    private void deleteDecoration(Decoration decoration) {
        map.removeDecoration(decoration);
        if (hoveredDecoration == decoration) {
            hoveredDecoration = null;
        }
        if (draggedDecoration == decoration) {
            draggedDecoration = null;
        }
        if (decorationEditorState.getSelectedDecoration() == decoration) {
            decorationEditorState.setSelectedDecoration(null);
        }
        repaint();
    }

    public void deleteSelectedDecoration() {
        Decoration selectedDecoration = decorationEditorState.getSelectedDecoration();
        if (selectedDecoration != null) {
            deleteDecoration(selectedDecoration);
        }
    }

    // reloads the image of every decoration on the map (used after image files have been changed)
    public void reloadDecorationImages() {
        for (Decoration decoration : map.getDecorations()) {
            decoration.setImage(decoration.getImageFileName(), decoration.getCrop());
        }
        repaint();
    }

    public boolean getShowNPCs() {
        return showNPCs;
    }

    public void setShowNPCs(boolean showNPCs) {
        this.showNPCs = showNPCs;
        repaint();
    }

    public boolean getShowEnhancedMapTiles() {
        return showEnhancedMapTiles;
    }

    public void setShowEnhancedMapTiles(boolean showEnhancedMapTiles) {
        this.showEnhancedMapTiles = showEnhancedMapTiles;
        repaint();
    }

    public boolean getShowTriggers() {
        return showTriggers;
    }

    public void setShowTriggers(boolean showTriggers) {
        this.showTriggers = showTriggers;
        repaint();
    }

    public boolean getShowDecorations() {
        return showDecorations;
    }

    // decorations are always shown while the Decorations tab is open, this only affects the Tiles tab
    public void setShowDecorations(boolean showDecorations) {
        this.showDecorations = showDecorations;
        repaint();
    }
}
