package Maps;

import EnhancedMapTiles.CarriableObject;
import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Dave;
import NPCs.Car;
import Scripts.MiniGameScript;
import Scripts.MiniGame2Script;
import Scripts.PickupObjectScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Utils.Point;

import java.util.ArrayList;

public class ProlougeCutsceneMap extends Map {
    public ProlougeCutsceneMap() {
        super("ProlougeMap.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(15, 33).getLocation();
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

        Car car = new Car(
                4,
                getMapTile(15, 35).getLocation().subtractX(20)
        );
        car.setInteractScript(new DaveScript());
        npcs.add(car);

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
        triggerPoint = getMapTile(15, 33).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 100, 100, new PlayerInCarScript()));
        
        return triggers;
    }

}