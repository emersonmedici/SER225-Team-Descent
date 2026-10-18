package Maps;

import EnhancedMapTiles.DangerEntity;
import EnhancedMapTiles.Keycard1;
import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Bug;
import NPCs.Dave;
import NPCs.Dinosaur;
import NPCs.Walrus;
import EnhancedMapTiles.DangerEntity;
import Scripts.MiniGame2Script;
import Scripts.MiniGameScript;
import Scripts.SimpleTextScript;
import EnhancedMapTiles.DangerEntity;
import EnhancedMapTiles.VentEntity;

import Scripts.TestMap.*;

import Tilesets.SimpleTileSheet;
import Utils.Point;
import java.util.ArrayList;


// Represents a test map to be used in a level
public class Level1Map extends Map {

    //private PlayLevelScreen screen;

    // public Level1Map(PlayLevelScreen screen) {67
    //     super("Level1Map.txt", new SimpleTileSheet());
    //     this.playerStartPosition = getMapTile(82, 97).getLocation();
    //     this.screen = screen;
    // }

    public Level1Map() {
        super("Level1Map.txt", new SimpleTileSheet());
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
        enhancedMapTiles.add(new Keycard1(getMapTile(84, 95).getLocation()));

        PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        enhancedMapTiles.add(pushableRock);
       
        // PushableRock pushableRock = new PushableRock(getMapTile(2, 7).getLocation());
        // enhancedMapTiles.add(pushableRock);

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

        Dave dave2 = new Dave(
                4,
                getMapTile(86, 98).getLocation().subtractX(20)
        );
        dave2.setInteractScript(new MiniGameScript());
        npcs.add(dave2);

        Dave dave3 = new Dave(
                4,
                getMapTile(88, 98).getLocation().subtractX(20)
        );
        dave3.setInteractScript(new MiniGame2Script());
        npcs.add(dave3);

        //DangerEntity enemy = new DangerEntity(1, getMapTile(82, 94).getLocation());
        //enemy.setInteractScript(new DangerEntityScript());
        //npcs.add(enemy);

        return npcs;
    }




    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        Point triggerPoint = getMapTile(82, 97).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 100, 100, new Level1Script1(), "hasLanded"));
        triggerPoint = getMapTile(88, 34).getLocation();
        triggers.add(new Trigger(triggerPoint.x, triggerPoint.y, 530, 150, new Level1Script2(), "newFlagTest"));
        return triggers;
    }


}

