package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

// This class represents a "common" tileset of standard tiles defined in the CommonTileset.png file
public class SimpleTileSheet extends Tileset {

    public SimpleTileSheet() {
        super(ImageLoader.load("SimpleTileSheet.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Void (black)
        Frame voidFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder voidTile = new MapTileBuilder(voidFrame);

        mapTiles.add(voidTile);

        // Floor ()
        Frame floorFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder floorTile = new MapTileBuilder(floorFrame);

        mapTiles.add(floorTile);

        // left Wall
        Frame leftWallFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftWallTile = new MapTileBuilder(leftWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(leftWallTile);

        // Right Wall
        Frame rightWallFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightWallTile = new MapTileBuilder(rightWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(rightWallTile);

        // Bottom Wall
        Frame bottomWallFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder bottomWallTile = new MapTileBuilder(bottomWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bottomWallTile);

        // Top Wall
        Frame topWallFrame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder topWallTile = new MapTileBuilder(topWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(topWallTile);

        // Bottom Right dot
        Frame bottomRightDotFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder bottomRightDotTile = new MapTileBuilder(bottomRightDotFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bottomRightDotTile);
        
        // Bottom Left dot
        Frame bottomLeftDotFrame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder bottomLeftDotTile = new MapTileBuilder(bottomLeftDotFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bottomLeftDotTile);

        // Top Right dot
        Frame topRightDotFrame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder topRightDotTile = new MapTileBuilder(topRightDotFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(topRightDotTile);
        
        // Top Left dot
        Frame topLeftDotFrame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder topLeftDotTile = new MapTileBuilder(topLeftDotFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(topLeftDotTile);
        
        // Vent Floor
        Frame ventFloorFrame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder ventFloorTile = new MapTileBuilder(ventFloorFrame);

        mapTiles.add(ventFloorTile);

        // Top Left Wall
        Frame topLeftWallFrame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder topLeftWallTile = new MapTileBuilder(topLeftWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(topLeftWallTile);

        // Bottom Left Wall
        Frame bottomLeftWallFrame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder bottomLeftWallTile = new MapTileBuilder(bottomLeftWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bottomLeftWallTile);

        // Bottom Right Wall
        Frame bottomRightWallFrame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder bottomRightWallTile = new MapTileBuilder(bottomRightWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bottomRightWallTile);

        // Top Right Wall
        Frame topRightWallFrame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder topRightWallTile = new MapTileBuilder(topRightWallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(topRightWallTile);

        return mapTiles;
    }
}
