package Maps;

import EnhancedMapTiles.BlockedWallTile;
import EnhancedMapTiles.BlockedWallTile2;
import EnhancedMapTiles.DangerEntity;
import EnhancedMapTiles.Keycard1;
import EnhancedMapTiles.Mg;
import EnhancedMapTiles.PushableRock;
import EnhancedMapTiles.WallTile;
import Level.*;
import NPCs.Bug;
import NPCs.Dave;
import NPCs.Dinosaur;
import NPCs.Walrus;
import EnhancedMapTiles.DangerEntity;
import Scripts.MiniGame2Script;
import Scripts.MiniGameScript;
import Scripts.SimpleTextScript;
import ScriptActions.*;
import EnhancedMapTiles.DangerEntity;
import EnhancedMapTiles.VentEntity;

import Scripts.TestMap.*;

import Tilesets.Level1Tileset;
import Utils.Point;
import java.util.ArrayList;


// Represents a test map to be used in a level
public class Level1Map extends Map {

    //private PlayLevelScreen screen;

    // public Level1Map(PlayLevelScreen screen) {67
    //     super("Level1Map.txt", new Level1Tileset());
    //     this.playerStartPosition = getMapTile(82, 97).getLocation();
    //     this.screen = screen;
    // }

    public Level1Map() {
        super("Level1Map.txt", new Level1Tileset());
        this.playerStartPosition = getMapTile(82, 97).getLocation();
        this.showHealthUI = true;
        this.ambientTrack = "level1_ambience.wav";
        this.chaseTrack = "chase.wav";
       // this.screen = null;
       // this.screen = null;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        // keycard is at the beginning of level for now, move to where puzzle is afterwards
        enhancedMapTiles.add(new Keycard1(getMapTile(44, 68).getLocation()));

        PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        enhancedMapTiles.add(pushableRock);

        BlockedWallTile wall = new BlockedWallTile(getMapTile(78, 96).getLocation());
        enhancedMapTiles.add(wall);
        
        BlockedWallTile wall2 = new BlockedWallTile(getMapTile(78, 97).getLocation());
        enhancedMapTiles.add(wall2);

        BlockedWallTile wall3 = new BlockedWallTile(getMapTile(78, 98).getLocation());
        enhancedMapTiles.add(wall3);

        BlockedWallTile2 wall4 = new BlockedWallTile2(getMapTile(43, 72).getLocation());
        enhancedMapTiles.add(wall4); 

        BlockedWallTile2 wall5 = new BlockedWallTile2(getMapTile(44, 72).getLocation());
        enhancedMapTiles.add(wall5); 

        BlockedWallTile2 wall6 = new BlockedWallTile2(getMapTile(45, 72).getLocation());
        enhancedMapTiles.add(wall6); 

        WallTile wall7 = new WallTile(getMapTile(107, 57).getLocation());
        enhancedMapTiles.add(wall7); 

        WallTile wall8 = new WallTile(getMapTile(108, 57).getLocation());
        enhancedMapTiles.add(wall8); 

        WallTile wall9 = new WallTile(getMapTile(109, 57).getLocation());
        enhancedMapTiles.add(wall9); 

        WallTile wall10 = new WallTile(getMapTile(101, 67).getLocation());
        enhancedMapTiles.add(wall10); 

        WallTile wall11 = new WallTile(getMapTile(102, 67).getLocation());
        enhancedMapTiles.add(wall11); 

        WallTile wall12 = new WallTile(getMapTile(103, 67).getLocation());
        enhancedMapTiles.add(wall12); 

        WallTile wall13 = new WallTile(getMapTile(81, 93).getLocation());
        enhancedMapTiles.add(wall13); 

        WallTile wall14 = new WallTile(getMapTile(82, 93).getLocation());
        enhancedMapTiles.add(wall14); 

        WallTile wall15 = new WallTile(getMapTile(83, 93).getLocation());
        enhancedMapTiles.add(wall15); 

        Mg mg = new Mg(getMapTile(103, 64).getLocation());
        mg.setInteractScript(new MiniGameScript());
        enhancedMapTiles.add(mg);

        Mg mg2 = new Mg(getMapTile(44, 76).getLocation(), 5);
        mg2.setInteractScript(new MiniGame2Script());
        enhancedMapTiles.add(mg2);

        Mg mg4 = new Mg(getMapTile(103, 65).getLocation(), "w");
       // mg4.setInteractScript(new MiniGame2Script());
        enhancedMapTiles.add(mg4);

        Mg mg5 = new Mg(getMapTile(44, 77).getLocation(), "w");
       // mg5.setInteractScript(new MiniGame2Script());
        enhancedMapTiles.add(mg5);

        DangerEntity enemy = new DangerEntity(getMapTile(93, 38).getLocation());
        enemy.setIsUpdateOffScreen(true);
        enhancedMapTiles.add(enemy);

        VentEntity ventEnemy = new VentEntity(getMapTile(94, 87).getLocation());
        ventEnemy.setIsUpdateOffScreen(true);
        enhancedMapTiles.add(ventEnemy);
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
 
    //     getMapTile(78, 95).setInteractScript(new BlockedTileScript());
    // }



    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        Point triggerPoint = getMapTile(82, 97).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 100, 100, new Level1Script1(), "hasLanded"));
        triggerPoint = getMapTile(88, 34).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 530, 150, new Level1Script2(), "newFlagTest"));
        
        /*Point triggerPoint2 = getMapTile(78, 95).getLocation();
        triggers.add(new Trigger(triggerPoint2.x, triggerPoint2.y, 100, 100, new BlockedTileScript(), "touchedWall"));*/
        return triggers;

    }

}

