package Maps;

import EnhancedMapTiles.MudObject;
import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Dave;
import NPCs.ParkedCar;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Utils.Point;
import java.util.ArrayList;

public class ProlougeMap extends Map {
    public ProlougeMap() {
        super("ProlougeMap.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(15, 33).getLocation();
        this.ambientTrack = "prologue_ambience.wav";
        // nothing can hurt the player here, so no hearts -- but they can still sprint
        this.showStaminaUI = true;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        
        PushableRock pushableRock = new PushableRock(getMapTile(10, 28).getLocation());
        enhancedMapTiles.add(pushableRock);

        MudObject mudTile = new MudObject(getMapTile(12, 32).getLocation());
        enhancedMapTiles.add(mudTile);
        MudObject mudTile2 = new MudObject(getMapTile(11, 32).getLocation());
        enhancedMapTiles.add(mudTile2);

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // The car stays where Dave used to be drawn as a car
        Point carLocation = getMapTile(15, 35).getLocation().subtractX(200);
        ParkedCar car = new ParkedCar(5, carLocation);
        npcs.add(car);

        // Dave stands just right of the car (the car is 128px wide) and keeps his talk script
        Dave dave = new Dave(4, carLocation.addX(0).addY(85));
        dave.setInteractScript(new DaveScript());
        // Dave turns to look at the player as they move around
        dave.setFacesPlayer(true);
        npcs.add(dave);

        return npcs;
    }


    //adding a trigger to the facility door to go inside
    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        //need to fix the coordinates
        Point triggerPoint = getMapTile(12, 8).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 150, 25, new FacilityDoorScript()));
        //triggerPoint = getMapTile(15, 33).getLocation(); 550, 1660,
        triggers.add(new Trigger(550, 1660, 75, 75, new DaveScript(), "hasTalkedToDave"));
        return triggers;
    }

}
