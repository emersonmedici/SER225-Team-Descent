package Maps;

import EnhancedMapTiles.CarriableObject;
import EnhancedMapTiles.PushableRock;
import EnhancedMapTiles.MudObject;
import Level.*;
import NPCs.Dave;
import Scripts.MiniGameScript;
import Scripts.MiniGame2Script;
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

        MudObject mudTile = new MudObject(getMapTile(12, 32).getLocation());
        enhancedMapTiles.add(mudTile);

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

        Dave dave2 = new Dave(
                4,
                getMapTile(13, 10).getLocation().subtractX(20)
        );
        dave2.setInteractScript(new MiniGameScript());
        npcs.add(dave2);

        Dave dave3 = new Dave(
                4,
                getMapTile(15, 9).getLocation().subtractX(20)
        );
        dave3.setInteractScript(new MiniGame2Script());
        npcs.add(dave3);

        return npcs;
    }
}