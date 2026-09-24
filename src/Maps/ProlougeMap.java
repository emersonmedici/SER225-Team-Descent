package Maps;

import EnhancedMapTiles.CarriableObject;
import Level.*;
import Scripts.PickupObjectScript;
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

        CarriableObject item1 = new CarriableObject(getMapTile(13, 33).getLocation());
        item1.setInteractScript(new PickupObjectScript());
        enhancedMapTiles.add(item1);
        //PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        //enhancedMapTiles.add(pushableRock);

        return enhancedMapTiles;
    }

}
