package Maps;

import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Bug;
import NPCs.Dinosaur;
import NPCs.Walrus;
import EnhancedMapTiles.DangerEntity;
import Scripts.SimpleTextScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Tilesets.SimpleTileSheet;
import java.util.ArrayList;
import Screens.PlayLevelScreen;

// Represents a test map to be used in a level
public class Level1Map extends Map {

    //private PlayLevelScreen screen;

    // public Level1Map(PlayLevelScreen screen) {
    //     super("Level1Map.txt", new SimpleTileSheet());
    //     this.playerStartPosition = getMapTile(82, 97).getLocation();
    //     this.screen = screen;
    // }

    public Level1Map() {
        super("Level1Map.txt", new SimpleTileSheet());
        this.playerStartPosition = getMapTile(82, 97).getLocation();
       // this.screen = null;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        enhancedMapTiles.add(pushableRock);
       
        // PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        // enhancedMapTiles.add(pushableRock);

        DangerEntity enemy = new DangerEntity(getMapTile(93, 38).getLocation());
        enemy.setIsUpdateOffScreen(true);
        enhancedMapTiles.add(enemy);

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // Walrus walrus = new Walrus(1, getMapTile(4, 28).getLocation().subtractY(40));
        // walrus.setInteractScript(new WalrusScript());
        // npcs.add(walrus);

        // Dinosaur dinosaur = new Dinosaur(2, getMapTile(82, 94).getLocation());
        // dinosaur.setExistenceFlag("hasTalkedToDinosaur");
        // dinosaur.setInteractScript(new DinoScript());
        // npcs.add(dinosaur);
        
        // Bug bug = new Bug(3, getMapTile(7, 12).getLocation().subtractX(20));
        // bug.setInteractScript(new BugScript());
        // npcs.add(bug);

        //DangerEntity enemy = new DangerEntity(1, getMapTile(82, 94).getLocation());
        //enemy.setInteractScript(new DangerEntityScript());
        //npcs.add(enemy);

        return npcs;
    }

    // public PlayLevelScreen getScreen() {
    //     return this.screen;
    // }

    // @Override
    // public ArrayList<Trigger> loadTriggers() {
    //     ArrayList<Trigger> triggers = new ArrayList<>();
    //     triggers.add(new Trigger(790, 1030, 100, 10, new LostBallScript(), "hasLostBall"));
    //     triggers.add(new Trigger(790, 960, 10, 80, new LostBallScript(), "hasLostBall"));
    //     triggers.add(new Trigger(890, 960, 10, 80, new LostBallScript(), "hasLostBall"));
    //     return triggers;
    // }

    // @Override
    // public void loadScripts() {
    //     getMapTile(21, 19).setInteractScript(new SimpleTextScript("Cat's house"));

    //     getMapTile(7, 26).setInteractScript(new SimpleTextScript("Walrus's house"));

    //     getMapTile(20, 4).setInteractScript(new SimpleTextScript("Dino's house"));

    //     getMapTile(2, 6).setInteractScript(new TreeScript());
    // }
}

