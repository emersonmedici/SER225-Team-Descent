package Maps;

import EnhancedMapTiles.BlockedWallTile2;
import EnhancedMapTiles.Mg;
import Level.*;
import Scripts.MiniGame2Script;
import Scripts.TestMap.ElevatorScript;
import Tilesets.Level1Tileset;
import Utils.Point;

import java.util.ArrayList;

// Represents the new level 1 map (rebuild of Level1Map)
// layout: elevator spawn room -> center room -> locked door -> level 2 room, with the puzzle room only reachable through the vents
public class Level1NewMap extends Map {

    public Level1NewMap() {
        super("level1_new.txt", new Level1Tileset());
        this.playerStartPosition = getMapTile(14, 90).getLocation();
        this.showHealthUI = true;
        this.ambientTrack = "level1_ambience.wav";
        this.chaseTrack = "chase.wav";
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        // locked door at the bottom of the hallway up to the level 2 room, one tile above the center room
        // Player removes every BlockedWallTile2 once the simon says puzzle is won
        enhancedMapTiles.add(new BlockedWallTile2(getMapTile(26, 86).getLocation()));
        enhancedMapTiles.add(new BlockedWallTile2(getMapTile(27, 86).getLocation()));

        // simon says puzzle in the puzzle room
        Mg simonSays = new Mg(getMapTile(43, 90).getLocation(), 5);
        simonSays.setInteractScript(new MiniGame2Script());
        enhancedMapTiles.add(simonSays);

        // post under the puzzle sign
        enhancedMapTiles.add(new Mg(getMapTile(43, 91).getLocation(), "w"));

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

        // one tile in front of the elevator -- walking into it loads the next level
        Point elevatorPoint = getMapTile(27, 72).getLocation();
        triggers.add(new Trigger(elevatorPoint.x, elevatorPoint.y, 48, 48, new ElevatorScript()));

        return triggers;
    }

}
