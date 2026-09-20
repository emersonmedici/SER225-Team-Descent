package Maps;

import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Bug;
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
}
