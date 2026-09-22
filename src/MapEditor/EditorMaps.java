package MapEditor;

import Level.Map;
import Maps.ProlougeMap;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.Level1Map;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("ProlougeMap");
            add("Level1Map");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "ProlougeMap":
                return new ProlougeMap();
            case "Level1Map":
                return new Level1Map();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
