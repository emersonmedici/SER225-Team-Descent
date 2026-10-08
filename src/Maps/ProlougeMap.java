package Maps;

import EnhancedMapTiles.CarriableObject;
import EnhancedMapTiles.PushableRock;
import EnhancedMapTiles.MudObject;
import Level.*;
import NPCs.Dave;
import Scripts.MiniGame2Script;
import Scripts.MiniGameScript;
import Scripts.PickupObjectScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Utils.Point;

import java.util.ArrayList;

public class ProlougeMap extends Map {
    public ProlougeMap() {
        super("ProlougeMap.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(15, 37).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        CarriableObject item1 = new CarriableObject(getMapTile(13, 33).getLocation());
        CarriableObject item2 = new CarriableObject(getMapTile(13, 34).getLocation());
        item1.setInteractScript(new PickupObjectScript());
        item2.setInteractScript(new PickupObjectScript());
        enhancedMapTiles.add(item1);
        enhancedMapTiles.add(item2);

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


    //adding a trigger to the facility door to go inside
    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        //need to fix the coordinates
        Point triggerPoint = getMapTile(12, 8).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 150, 25, new FacilityDoorScript()));
        
        return triggers;
    }

}