package Maps;

import EnhancedMapTiles.CarriableObject;
import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Dave;
import Scripts.PickupObjectScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import java.util.ArrayList;

public class ProlougeMap extends Map {
    public ProlougeMap() {
        super("ProlougeMap.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(15, 35).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        CarriableObject item1 =
                new CarriableObject(getMapTile(13, 33).getLocation());
        item1.setInteractScript(new PickupObjectScript());
        enhancedMapTiles.add(item1);

        PushableRock pushableRock = new PushableRock(getMapTile(10, 28).getLocation());
        enhancedMapTiles.add(pushableRock);

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        Dave dave = new Dave(
                4,
                getMapTile(7, 35).getLocation().subtractX(20)
        );
        dave.setInteractScript(new DaveScript());
        npcs.add(dave);

        return npcs;
    }
}