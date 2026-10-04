package MapEditor;

import Engine.GraphicsHandler;
import Level.*;
import Lighting.Light;
import Lighting.LightingRenderer;
import Lighting.SpotLight;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

public class TileBuilder extends JPanel {
    private Map map;
    private MapTile hoveredMapTile;
    private SelectedTileIndexHolder controlPanelHolder;
    private GraphicsHandler graphicsHandler = new GraphicsHandler();
    private JLabel hoveredTileIndexLabel;
    private boolean showNPCs;
    private boolean showEnhancedMapTiles;
    private boolean showTriggers;
    private LightEditorState lightState;
    private LightingRenderer previewRenderer;

    public TileBuilder(SelectedTileIndexHolder controlPanelHolder, JLabel hoveredTileIndexLabel, LightEditorState lightState) {
        this.lightState = lightState;
        setBackground(Colors.MAGENTA);
        setLocation(0, 0);
        setPreferredSize(new Dimension(585, 562));
        this.controlPanelHolder = controlPanelHolder;
        this.hoveredTileIndexLabel = hoveredTileIndexLabel;
        addMouseListener(new MouseListener() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredMapTile = null;
                hoveredTileIndexLabel.setText("");
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
               if (lightState.getMode() == LightEditorState.Mode.LIGHTS) {
                    lightPressed(e);
                } else {
                    tileSelected(e.getPoint());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                tileHovered(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
               tileHovered(e.getPoint());
                if (lightState.getMode() == LightEditorState.Mode.LIGHTS) {
                    // in light mode, dragging moves the selected light instead of painting tiles
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        moveSelectedLight(e.getPoint());
                    }
                } else {
                    tileSelected(e.getPoint());
                }
            }
        });
    }

    public void setMap(Map map) {
        this.map = map;
        this.previewRenderer = new LightingRenderer(Math.round(map.getTileset().getTileScale()));
        setPreferredSize(new Dimension(map.getWidthPixels(), map.getHeightPixels()));
        repaint();
    }

    public void draw() {
        for (MapTile tile : map.getMapTiles()) {
            tile.draw(graphicsHandler);
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

        if (showTriggers) {
            for (Trigger trigger : map.getTriggers()) {
                trigger.draw(graphicsHandler, new Color(255, 0, 255, 100));
            }
        }

         if (lightState.getMode() == LightEditorState.Mode.LIGHTS) {
            if (lightState.isPreviewLighting() && map.getAmbientDarkness() > 0) {
                // the panel's coordinates ARE world coordinates, so the "camera" is at 0, 0
                Rectangle view = getVisibleRect();
                previewRenderer.render(graphicsHandler, map.getLights(), map.getAmbientDarkness(), map.getAmbientColor(),
                        view.x, view.y, view.width, view.height, 0, 0);
            }
            drawLightGizmos();
        }

        if (hoveredMapTile != null) {
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
            MapTile newMapTile =  map.getTileset().getTile(controlPanelHolder.getSelectedTileIndex()).build(oldMapTile.getX(), oldMapTile.getY(), controlPanelHolder.getSelectedTileRotation());
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

        // left click: select the light on this tile, or place a new one; right click: delete
    private void lightPressed(MouseEvent e) {
        MapTile tile = getHoveredTile(e.getPoint());
        if (tile == null) {
            return;
        }
        Light clicked = getLightOnTile(tile);

        if (SwingUtilities.isRightMouseButton(e)) {
            if (clicked != null) {
                map.getLights().remove(clicked);
                if (clicked == lightState.getSelectedLight()) {
                    lightState.setSelectedLight(null);
                }
            }
        } else if (clicked != null) {
            lightState.setSelectedLight(clicked);
        } else {
            // first light on a fully lit map: turn darkness on so the light actually shows
            if (map.getAmbientDarkness() == 0) {
                map.setAmbientDarkness(0.9f);
            }
            Light light = lightState.createLight(getTileCenterX(tile), getTileCenterY(tile), map.getTileset().getScaledSpriteWidth());
            map.getLights().add(light);
            lightState.setSelectedLight(light);
        }
        repaint();
    }

    private void moveSelectedLight(Point mousePoint) {
        Light light = lightState.getSelectedLight();
        MapTile tile = getHoveredTile(mousePoint);
        if (light != null && tile != null) {
            light.setPosition(getTileCenterX(tile), getTileCenterY(tile));
            repaint();
        }
    }

    // returns the light whose center is inside this tile, or null
    private Light getLightOnTile(MapTile tile) {
        for (Light light : map.getLights()) {
            if (light.getX() >= tile.getX() && light.getX() < tile.getX() + tile.getWidth() &&
                    light.getY() >= tile.getY() && light.getY() < tile.getY() + tile.getHeight()) {
                return light;
            }
        }
        return null;
    }

    private float getTileCenterX(MapTile tile) { return tile.getX() + tile.getWidth() / 2f; }
    private float getTileCenterY(MapTile tile) { return tile.getY() + tile.getHeight() / 2f; }

    // draws each light's reach, a handle to click, and a spot light's aim
    private void drawLightGizmos() {
        Graphics2D g = graphicsHandler.getGraphics();
        Stroke oldStroke = g.getStroke();
        for (Light light : map.getLights()) {
            boolean selected = light == lightState.getSelectedLight();
            g.setColor(selected ? Color.YELLOW : Color.WHITE);
            g.setStroke(new BasicStroke(selected ? 2 : 1));

            int cx = Math.round(light.getX());
            int cy = Math.round(light.getY());
            int r = Math.round(light.getRadius());
            g.drawOval(cx - r, cy - r, r * 2, r * 2);
            g.fillRect(cx - 5, cy - 5, 10, 10);

            if (light instanceof SpotLight) {
                SpotLight spot = (SpotLight) light;
                g.drawLine(cx, cy, cx + Math.round(spot.getDirX() * r), cy + Math.round(spot.getDirY() * r));
            }
        }
        g.setStroke(oldStroke);
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
}
