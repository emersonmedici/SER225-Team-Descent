package MapEditor;

import Builders.MapTileBuilder;
import Engine.GraphicsHandler;
import Level.Map;
import Level.MapTile;
import Level.Tileset;
import Utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TilePicker extends JPanel {

    private Tileset tileset;
    private GraphicsHandler graphicsHandler = new GraphicsHandler();
    private HashMap<Integer, MapTile> mapTiles = new HashMap<>();
    private int selectedTileIndex = 0;
    private int selectedTileRotation = 0;
    private SelectedTileIndexHolder selectedTileIndexHolder;
    private Map map;

    public TilePicker(SelectedTileIndexHolder selectedTileIndexHolder) {
        setBackground(Colors.MAGENTA);
        setLocation(0, 0);
        setPreferredSize(new Dimension(187, 391));
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
        setLayout(null);
        this.selectedTileIndexHolder = selectedTileIndexHolder;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tileSelected(e.getPoint());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                tileHovered(e.getPoint());
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotateTile");
        getActionMap().put("rotateTile", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        rotateSelectedTile();
            }
        });
    }

    public void setTileset(Map map, Tileset tileset) {
        mapTiles.clear();
        this.map = map;
        this.tileset = tileset;

        mapTiles.clear();
        this.tileset = tileset;
        HashMap<Integer, MapTileBuilder> mapTileBuilders = this.tileset.mapDefinedTilesToIndex();

        int width = (int)this.getPreferredSize().getWidth() / this.tileset.getScaledSpriteWidth();
        if (width == 0) {
            width = 1;
        }
        int height = (int)Math.ceil(mapTileBuilders.keySet().size() / (double)width);
        if (height == 0) {
            height = 1;
        }
        setPreferredSize(new Dimension(Math.max(144, width * tileset.getScaledSpriteWidth()), Math.max(391, height * tileset.getScaledSpriteHeight() + (6 * height))));

        Integer[] tileKeys = mapTileBuilders.keySet().toArray(new Integer[mapTileBuilders.keySet().size()]);
        Arrays.sort(tileKeys);
        int currentKeyIndex = 0;
        outerLoop: for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (currentKeyIndex >= tileKeys.length) {
                    break outerLoop;
                }

                int x = j * tileset.getScaledSpriteWidth() + ((j * 5) + 5);
                int y = i * tileset.getScaledSpriteHeight() + ((i * 5) + 5);
                MapTile tile = mapTileBuilders.get(tileKeys[currentKeyIndex]).build(x, y, selectedTileRotation);

                tile.setMap(map);
                mapTiles.put(currentKeyIndex, tile);
                currentKeyIndex++;
            }
        }
        repaint();

        
    }

    public void draw() {
        for (MapTile mapTile : mapTiles.values()) {
            mapTile.draw(graphicsHandler);
        }

        MapTile selectedTile = mapTiles.get(selectedTileIndex);
        if (selectedTile == null) {
            selectedTile = mapTiles.get(0);
            getParent().revalidate();
        }
        graphicsHandler.drawRectangle(
                Math.round(selectedTile.getX()) - 2,
                Math.round(selectedTile.getY()) - 2,
                selectedTile.getWidth() + 4,
                selectedTile.getHeight() + 4,
                Color.YELLOW,
                4
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphicsHandler.setGraphics((Graphics2D) g);
        draw();
    }
    
        protected void rotateSelectedTile() {
            if (tileset == null) {
                return;
            }
            selectedTileRotation = (selectedTileRotation + 1) % 4;
            selectedTileIndexHolder.setSelectedTileRotation(selectedTileRotation);

            // rebuild the picker's tiles so they preview the current rotation
            setTileset(map, tileset);
        }

    protected void tileSelected(Point clickedPoint) {
        int selectedTileIndex = getClickedTileIndex(clickedPoint);
        if (selectedTileIndex >= 0) {
            this.selectedTileIndex = selectedTileIndex;
            selectedTileIndexHolder.setSelectedTileIndex(selectedTileIndex);
            repaint();
        }
    }

    protected void tileHovered(Point hoveredPoint) {
        if (isMouseInTileBounds(hoveredPoint)) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    protected boolean isMouseInTileBounds(Point mousePoint) {
        for (MapTile mapTile : mapTiles.values()) {
            if (isPointInTile(mousePoint, mapTile)) {
                return true;
            }
        }
        return false;
    }

    protected int getClickedTileIndex(Point mousePoint) {
        for (Entry<Integer, MapTile> entry : mapTiles.entrySet()) {
            if (isPointInTile(mousePoint, entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    protected boolean isPointInTile(Point point, MapTile tile) {
        return (point.x >= tile.getX() && point.x <= tile.getX() + tile.getWidth() &&
                point.y >= tile.getY() && point.y <= tile.getY() + tile.getHeight());
    }
}
