package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.Config;
import GameObject.Frame;
import Level.Surface;
import Level.TileType;
import Level.Tileset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// This class represents the level 1 facility tileset defined in the level1Spritesheet1.png file
// Wall pieces are only drawn in one direction -- use the map editor's rotate tool (R key) to place the other directions
public class Level1Tileset extends Tileset {

    // the sprite sheet's see-through pixels (floor grates, vent entrance) are filled with this color
    // magenta can't be painted directly in the png since the engine treats it as the transparent color
    private static final Color FILL_COLOR = new Color(255, 0, 255);

    public Level1Tileset() {
        super(loadWithFilledTransparency("level1Spritesheet1.png", FILL_COLOR), 16, 16, 3);
    }

    // loads the image without ImageLoader (which would turn transparent pixels black and magenta pixels transparent)
    // and replaces every fully transparent pixel with the given color
    private static BufferedImage loadWithFilledTransparency(String imageFileName, Color fillColor) {
        BufferedImage image;
        try {
            BufferedImage initialImage = ImageIO.read(new File(Config.RESOURCES_PATH + imageFileName));
            image = new BufferedImage(initialImage.getWidth(), initialImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(initialImage, 0, 0, null);
        } catch (IOException e) {
            System.out.println("Unable to find file " + Config.RESOURCES_PATH + imageFileName);
            throw new RuntimeException(e);
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if ((image.getRGB(x, y) >>> 24) == 0) {
                    image.setRGB(x, y, fillColor.getRGB());
                }
            }
        }
        return image;
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // 0: Void (black)
        BufferedImage voidImage = new BufferedImage(spriteWidth, spriteHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D voidGraphics = voidImage.createGraphics();
        voidGraphics.setColor(Color.BLACK);
        voidGraphics.fillRect(0, 0, spriteWidth, spriteHeight);
        voidGraphics.dispose();
        Frame voidFrame = new FrameBuilder(voidImage)
                .withScale(tileScale)
                .build();

        MapTileBuilder voidTile = new MapTileBuilder(voidFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(voidTile);

        // 1: Floor grate
        Frame floorFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder floorTile = new MapTileBuilder(floorFrame)
                .withSurface(Surface.METAL);

        mapTiles.add(floorTile);

        // 2: Floor grate variant
        Frame floor2Frame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder floor2Tile = new MapTileBuilder(floor2Frame)
                .withSurface(Surface.METAL);

        mapTiles.add(floor2Tile);

        // 3: Floor grate variant
        Frame floor3Frame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder floor3Tile = new MapTileBuilder(floor3Frame)
                .withSurface(Surface.METAL);

        mapTiles.add(floor3Tile);

        // 4: Floor grate with dirt
        Frame floorDirtFrame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder floorDirtTile = new MapTileBuilder(floorDirtFrame)
                .withSurface(Surface.METAL);

        mapTiles.add(floorDirtTile);

        // 5: Wall (unrotated: void on top, room below)
        Frame wallFrame = new FrameBuilder(getSubImage(0, 8))
                .withScale(tileScale)
                .build();

        MapTileBuilder wallTile = new MapTileBuilder(wallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(wallTile);

        // 6: Wall corner (unrotated: void in the top right)
        Frame wallCornerFrame = new FrameBuilder(getSubImage(0, 9))
                .withScale(tileScale)
                .build();

        MapTileBuilder wallCornerTile = new MapTileBuilder(wallCornerFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(wallCornerTile);

        // 7: Wall edge corner (unrotated: void on the left and bottom)
        Frame wallEdgeCornerFrame = new FrameBuilder(getSubImage(0, 10))
                .withScale(tileScale)
                .build();

        MapTileBuilder wallEdgeCornerTile = new MapTileBuilder(wallEdgeCornerFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(wallEdgeCornerTile);

        // 8: Inside vent
        Frame ventFloorFrame = new FrameBuilder(getSubImage(0, 7))
                .withScale(tileScale)
                .build();

        MapTileBuilder ventFloorTile = new MapTileBuilder(ventFloorFrame)
                .withSurface(Surface.METAL)
                .withTileType(TileType.VENT);

        mapTiles.add(ventFloorTile);

        // 9: Vent entrance
        Frame ventEntranceFrame = new FrameBuilder(getSubImage(0, 6))
                .withScale(tileScale)
                .build();

        MapTileBuilder ventEntranceTile = new MapTileBuilder(ventEntranceFrame)
                .withSurface(Surface.METAL)
                .withTileType(TileType.VENT);

        mapTiles.add(ventEntranceTile);

        // 10: Elevator
        Frame elevatorFrame = new FrameBuilder(getSubImage(0, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder elevatorTile = new MapTileBuilder(elevatorFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(elevatorTile);

        // 11: Bloody elevator
        Frame bloodyElevatorFrame = new FrameBuilder(getSubImage(0, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder bloodyElevatorTile = new MapTileBuilder(bloodyElevatorFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bloodyElevatorTile);

        return mapTiles;
    }
}
