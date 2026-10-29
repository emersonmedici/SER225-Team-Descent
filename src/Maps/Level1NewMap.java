package Maps;

import Level.*;
import Tilesets.Level1Tileset;

import java.util.ArrayList;

// Represents the new level 1 map (rebuild of Level1Map) -- starts empty, build it out in the map editor
public class Level1NewMap extends Map {

    public Level1NewMap() {
        super("level1_new.txt", new Level1Tileset());
        this.playerStartPosition = getMapTile(82, 62).getLocation();
        this.showHealthUI = true;
        this.ambientTrack = "level1_ambience.wav";
        this.chaseTrack = "chase.wav";
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        return triggers;
    }

}
