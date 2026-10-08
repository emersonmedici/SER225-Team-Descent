package Maps;

import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Bug;
import NPCs.Dave;
import NPCs.Dinosaur;
import NPCs.Walrus;
import Scripts.SimpleTextScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Tilesets.SimpleTileSheet;
import Utils.Point;

import java.util.ArrayList;

// Represents a test map to be used in a level
public class NothingMap extends Map {

    public NothingMap() {
        super("NothingMap.txt", new SimpleTileSheet());
        this.playerStartPosition = getMapTile(12, 10).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
/* 
        PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        enhancedMapTiles.add(pushableRock);
*/
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
/* 
        Dave dave = new Dave(
                4,
                getMapTile(7, 35).getLocation().subtractX(20)
        );
        dave.setInteractScript(new DaveScript());
        npcs.add(dave);
*/
        return npcs;
    }


    //adding a trigger to the facility door to go inside
    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        //need to fix the coordinates
        //Point triggerPoint = getMapTile(12, 8).getLocation();
        //triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 150, 25, new FacilityDoorScript()));

        //triggers.add(new Trigger(0, 0, 1000, 1000, new NothingMapScript()));
        
        return triggers;
    }
 
}

