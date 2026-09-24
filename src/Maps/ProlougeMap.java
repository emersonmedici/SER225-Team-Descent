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

import java.util.ArrayList;

public class ProlougeMap extends Map {
    public ProlougeMap() {
        super("ProlougeMap.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(15, 35).getLocation();
    }

//an array list of npcs, only contains one right now
    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        //dave's
        Dave dave = new Dave(4, getMapTile(7, 35).getLocation().subtractX(20));
        // dave.setExistenceFlag("hasTalkedToDave");
        dave.setInteractScript(new DaveScript());
        npcs.add(dave);

        return npcs;
    }

}
