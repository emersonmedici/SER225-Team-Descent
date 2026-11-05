package Maps;

import EnhancedMapTiles.BlockedWallTile;
import EnhancedMapTiles.BlockedWallTile2;
import EnhancedMapTiles.DangerEntity;
import EnhancedMapTiles.Keycard1;
import EnhancedMapTiles.Mg;
import EnhancedMapTiles.PushableRock;
import EnhancedMapTiles.WallTileX;
import EnhancedMapTiles.WallTileY;
import EnhancedMapTiles.ArtTiles.*;
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
import Screens.MiniGame;

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



    private DangerEntity dangerEntity;
    private boolean spawnedIn;

    public Level1Map() {
        super("Level1Map.txt", new Level1Tileset());
        this.playerStartPosition = getMapTile(82, 97).getLocation();
        this.showHealthUI = true;
        this.ambientTrack = "level1_ambience.wav";
        this.chaseTrack = "chase.wav";
        this.spawnedIn = false;

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

        BlockedWallTile wall17 = new BlockedWallTile(getMapTile(94, 95).getLocation(), "UnlockWallX.png");
        enhancedMapTiles.add(wall17);
        
        BlockedWallTile wall18 = new BlockedWallTile(getMapTile(107, 84).getLocation());
        enhancedMapTiles.add(wall18);

        BlockedWallTile2 wall4 = new BlockedWallTile2(getMapTile(43, 72).getLocation());
        enhancedMapTiles.add(wall4); 

        BlockedWallTile2 wall5 = new BlockedWallTile2(getMapTile(44, 72).getLocation());
        enhancedMapTiles.add(wall5); 

        BlockedWallTile2 wall6 = new BlockedWallTile2(getMapTile(45, 72).getLocation());
        enhancedMapTiles.add(wall6); 

        BlockedWallTile2 wall19 = new BlockedWallTile2(getMapTile(50, 62).getLocation());
        enhancedMapTiles.add(wall19);

        BlockedWallTile2 wall20 = new BlockedWallTile2(getMapTile(51, 62).getLocation());
        enhancedMapTiles.add(wall20);

        BlockedWallTile2 wall21 = new BlockedWallTile2(getMapTile(52, 62).getLocation());
        enhancedMapTiles.add(wall21);

        WallTileX wall7 = new WallTileX(getMapTile(107, 57).getLocation());
        enhancedMapTiles.add(wall7); 

        WallTileX wall8 = new WallTileX(getMapTile(108, 57).getLocation());
        enhancedMapTiles.add(wall8); 

        WallTileX wall9 = new WallTileX(getMapTile(109, 57).getLocation());
        enhancedMapTiles.add(wall9); 

        WallTileX wall10 = new WallTileX(getMapTile(101, 67).getLocation());
        enhancedMapTiles.add(wall10); 

        WallTileX wall11 = new WallTileX(getMapTile(102, 67).getLocation());
        enhancedMapTiles.add(wall11); 

        WallTileX wall12 = new WallTileX(getMapTile(103, 67).getLocation());
        enhancedMapTiles.add(wall12); 

        WallTileX wall13 = new WallTileX(getMapTile(81, 93).getLocation());
        enhancedMapTiles.add(wall13); 

        WallTileX wall14 = new WallTileX(getMapTile(82, 93).getLocation());
        enhancedMapTiles.add(wall14); 

        WallTileX wall15 = new WallTileX(getMapTile(83, 93).getLocation());
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

        // ---------- Decorations ----------

        // Room 1
        Desk desk1 = new Desk(centerOnTile(82, 96), new Point(0, 0));
        enhancedMapTiles.add(desk1);
        Cabinet cabinet1 = new Cabinet(centerOnTile(85, 95), new Point(1, 1));
        enhancedMapTiles.add(cabinet1);
        Cabinet cabinet2 = new Cabinet(centerOnTile(79, 99), new Point(1, 2));
        enhancedMapTiles.add(cabinet2);
        Couch couch1 = new Couch(centerOnTile(84, 100), new Point(1, 0));
        enhancedMapTiles.add(couch1);

        // Room 9
        Couch couch2 = new Couch(centerOnTile(58, 89), new Point(1, 1));
        enhancedMapTiles.add(couch2);
        Desk desk2 = new Desk(centerOnTile(54, 93), new Point(0, 1));
        enhancedMapTiles.add(desk2);

        // Room 10
        Couch couch3 = new Couch(centerOnTile(41, 78), new Point(0, 1));
        enhancedMapTiles.add(couch3);
        Couch couch4 = new Couch(centerOnTile(41, 75), new Point(0, 1));
        enhancedMapTiles.add(couch4);
        Couch couch5 = new Couch(centerOnTile(47, 75), new Point(1, 1));
        enhancedMapTiles.add(couch5);
        Couch couch6 = new Couch(centerOnTile(44, 79), new Point(1, 0));
        enhancedMapTiles.add(couch6);

        // Room 8
        Couch couch7 = new Couch(centerOnTile(77, 82), new Point(1, 0));
        enhancedMapTiles.add(couch7);
        Couch couch8 = new Couch(centerOnTile(87, 78), new Point(0, 0));
        enhancedMapTiles.add(couch8);
        Desk desk3 = new Desk(centerOnTile(77, 79), new Point(0, 0));
        enhancedMapTiles.add(desk3);
        Desk desk4 = new Desk(centerOnTile(86, 81), new Point(0, 0));
        enhancedMapTiles.add(desk4);
        Cabinet cabinet3 = new Cabinet(centerOnTile(83, 78), new Point(0, 0));
        enhancedMapTiles.add(cabinet3);

        // Room 11
        Bed bed1 = new Bed(centerOnTile(63, 62), new Point(0, 1));
        enhancedMapTiles.add(bed1);
        Curtain curtain1 = new Curtain(centerOnTile(63, 63), new Point(0, 0));
        enhancedMapTiles.add(curtain1);
        Bed bed2 = new Bed(centerOnTile(63, 68), new Point(0, 1));
        enhancedMapTiles.add(bed2);
        Curtain curtain2 = new Curtain(centerOnTile(63, 67), new Point(0, 0));
        enhancedMapTiles.add(curtain2);
        HeartMonitor heartMonitor1 = new HeartMonitor(centerOnTile(64, 62), new Point(1, 0));
        enhancedMapTiles.add(heartMonitor1);
        HeartMonitor heartMonitor2 = new HeartMonitor(centerOnTile(65, 68), new Point(1, 0));
        enhancedMapTiles.add(heartMonitor2);

        // Room 7
        StorageRack storageRack1 = new StorageRack(centerOnTile(88, 41), new Point(0, 0));
        enhancedMapTiles.add(storageRack1);
        StorageRack storageRack2 = new StorageRack(centerOnTile(88, 39), new Point(0, 0));
        enhancedMapTiles.add(storageRack2);
        StorageRack storageRack3 = new StorageRack(centerOnTile(88, 37), new Point(0, 0));
        enhancedMapTiles.add(storageRack3);
        StorageRack storageRack4 = new StorageRack(centerOnTile(88, 35), new Point(0, 0));
        enhancedMapTiles.add(storageRack4);
        StorageRack storageRack5 = new StorageRack(centerOnTile(98, 41), new Point(0, 0));
        enhancedMapTiles.add(storageRack5);
        StorageRack storageRack6 = new StorageRack(centerOnTile(98, 39), new Point(0, 0));
        enhancedMapTiles.add(storageRack6);
        StorageRack storageRack7 = new StorageRack(centerOnTile(98, 37), new Point(0, 0));
        enhancedMapTiles.add(storageRack7);
        StorageRack storageRack8 = new StorageRack(centerOnTile(98, 35), new Point(0, 0));
        enhancedMapTiles.add(storageRack8);
        StorageRack storageRack9 = new StorageRack(centerOnTile(95, 36), new Point(0, 0));
        enhancedMapTiles.add(storageRack9);
        StorageRack storageRack10 = new StorageRack(centerOnTile(91, 36), new Point(0, 0));
        enhancedMapTiles.add(storageRack10);
        StorageRack storageRack11 = new StorageRack(centerOnTile(91, 40), new Point(0, 0));
        enhancedMapTiles.add(storageRack11);
        StorageRack storageRack12 = new StorageRack(centerOnTile(95, 40), new Point(0, 0));
        enhancedMapTiles.add(storageRack12);
        Bed bed3 = new Bed(centerOnTile(93, 44), new Point(1, 0));
        enhancedMapTiles.add(bed3);
        Bed bed4 = new Bed(centerOnTile(96, 44), new Point(1, 0));
        enhancedMapTiles.add(bed4);

        // Room 2
        StorageRack storageRack13 = new StorageRack(centerOnTile(105, 92), new Point(0, 0));
        enhancedMapTiles.add(storageRack13);
        StorageRack storageRack14 = new StorageRack(centerOnTile(105, 102), new Point(0, 0));
        enhancedMapTiles.add(storageRack14);
        Cabinet cabinet4 = new Cabinet(centerOnTile(109, 102), new Point(0, 1));
        enhancedMapTiles.add(cabinet4);
        Cabinet cabinet5 = new Cabinet(centerOnTile(110, 98), new Point(1, 1));
        enhancedMapTiles.add(cabinet5);
        Cabinet cabinet6 = new Cabinet(centerOnTile(110, 94), new Point(0, 2));
        enhancedMapTiles.add(cabinet6);
        Cabinet cabinet7 = new Cabinet(centerOnTile(104, 99), new Point(1, 2));
        enhancedMapTiles.add(cabinet7);
        Cabinet cabinet8 = new Cabinet(centerOnTile(110, 96), new Point(1, 1));
        enhancedMapTiles.add(cabinet8);
        StorageRack storageRack15 = new StorageRack(centerOnTile(107, 92), new Point(0, 0));
        enhancedMapTiles.add(storageRack15);
        StorageRack storageRack16 = new StorageRack(centerOnTile(107, 102), new Point(0, 0));
        enhancedMapTiles.add(storageRack16);

        // Room 3
        Cabinet cabinet9 = new Cabinet(centerOnTile(138, 92), new Point(1, 1));
        enhancedMapTiles.add(cabinet9);
        Cabinet cabinet10 = new Cabinet(centerOnTile(138, 94), new Point(0, 2));
        enhancedMapTiles.add(cabinet10);
        Cabinet cabinet11 = new Cabinet(centerOnTile(138, 96), new Point(1, 1));
        enhancedMapTiles.add(cabinet11);
        Cabinet cabinet12 = new Cabinet(centerOnTile(138, 98), new Point(0, 2));
        enhancedMapTiles.add(cabinet12);
        Cabinet cabinet13 = new Cabinet(centerOnTile(132, 92), new Point(0, 0));
        enhancedMapTiles.add(cabinet13);
        Cabinet cabinet14 = new Cabinet(centerOnTile(133, 92), new Point(1, 0));
        enhancedMapTiles.add(cabinet14);
        Cabinet cabinet15 = new Cabinet(centerOnTile(134, 92), new Point(2, 0));
        enhancedMapTiles.add(cabinet15);
        Cabinet cabinet16 = new Cabinet(centerOnTile(133, 98), new Point(0, 1));
        enhancedMapTiles.add(cabinet16);
        Cabinet cabinet17 = new Cabinet(centerOnTile(134, 98), new Point(0, 1));
        enhancedMapTiles.add(cabinet17);
        Cabinet cabinet18 = new Cabinet(centerOnTile(135, 98), new Point(0, 1));
        enhancedMapTiles.add(cabinet18);
        Cabinet cabinet19 = new Cabinet(centerOnTile(136, 98), new Point(0, 1));
        enhancedMapTiles.add(cabinet19);
        // Table table1 = new Table(centerOnTile(135, 95), new Point(0, 0));
        // enhancedMapTiles.add(table1);

        // Room 4
        Bed bed5 = new Bed(centerOnTile(128, 78), new Point(1, 0));
        enhancedMapTiles.add(bed5);
        Bed bed6 = new Bed(centerOnTile(134, 78), new Point(1, 0));
        enhancedMapTiles.add(bed6);
        HeartMonitor heartMonitor3 = new HeartMonitor(centerOnTile(129, 78), new Point(0, 0));
        enhancedMapTiles.add(heartMonitor3);
        HeartMonitor heartMonitor4 = new HeartMonitor(centerOnTile(133, 78), new Point(0, 0));
        enhancedMapTiles.add(heartMonitor4);

        // Room 5
        Cabinet cabinet20 = new Cabinet(centerOnTile(149, 52), new Point(0, 0));
        enhancedMapTiles.add(cabinet20);
        Cabinet cabinet21 = new Cabinet(centerOnTile(150, 52), new Point(1, 0));
        enhancedMapTiles.add(cabinet21);
        Cabinet cabinet22 = new Cabinet(centerOnTile(151, 52), new Point(2, 0));
        enhancedMapTiles.add(cabinet22);
        Cabinet cabinet23 = new Cabinet(centerOnTile(153, 52), new Point(0, 0));
        enhancedMapTiles.add(cabinet23);
        Cabinet cabinet24 = new Cabinet(centerOnTile(154, 52), new Point(1, 0));
        enhancedMapTiles.add(cabinet24);
        Cabinet cabinet25 = new Cabinet(centerOnTile(155, 52), new Point(2, 0));
        enhancedMapTiles.add(cabinet25);
        Cabinet cabinet26 = new Cabinet(centerOnTile(156, 52), new Point(0, 0));
        enhancedMapTiles.add(cabinet26);
        Cabinet cabinet27 = new Cabinet(centerOnTile(157, 54), new Point(1, 1));
        enhancedMapTiles.add(cabinet27);
        Cabinet cabinet28 = new Cabinet(centerOnTile(157, 56), new Point(0, 2));
        enhancedMapTiles.add(cabinet28);
        Couch couch9 = new Couch(centerOnTile(148, 58), new Point(1, 0));
        enhancedMapTiles.add(couch9);
        Couch couch10 = new Couch(centerOnTile(151, 58), new Point(1, 0));
        enhancedMapTiles.add(couch10);
        Cabinet cabinet29 = new Cabinet(centerOnTile(155, 58), new Point(0, 1));
        enhancedMapTiles.add(cabinet29);

        // Room 6
        Bed bed7 = new Bed(centerOnTile(101, 58), new Point(0, 0));
        enhancedMapTiles.add(bed7);
        Bed bed8 = new Bed(centerOnTile(103, 58), new Point(0, 0));
        enhancedMapTiles.add(bed8);
        Bed bed9 = new Bed(centerOnTile(105, 58), new Point(0, 0));
        enhancedMapTiles.add(bed9);
        HeartMonitor heartMonitor5 = new HeartMonitor(centerOnTile(102, 58), new Point(0, 0));
        enhancedMapTiles.add(heartMonitor5);
        HeartMonitor heartMonitor6 = new HeartMonitor(centerOnTile(104, 58), new Point(0, 0));
        enhancedMapTiles.add(heartMonitor6);
        Bed bed10 = new Bed(centerOnTile(109, 66), new Point(1, 0));
        enhancedMapTiles.add(bed10);
        Bed bed11 = new Bed(centerOnTile(107, 66), new Point(1, 0));
        enhancedMapTiles.add(bed11);
        Curtain curtain3 = new Curtain(centerOnTile(108, 66), new Point(0, 0));
        enhancedMapTiles.add(curtain3);
        Bed bed12 = new Bed(centerOnTile(109, 64), new Point(1, 0));
        enhancedMapTiles.add(bed12);
        Bed bed13 = new Bed(centerOnTile(107, 64), new Point(1, 0));
        enhancedMapTiles.add(bed13);
        Curtain curtain4 = new Curtain(centerOnTile(108, 64), new Point(0, 0));
        enhancedMapTiles.add(curtain4);
        StorageRack storageRack17 = new StorageRack(centerOnTile(104, 66), new Point(0, 0));
        enhancedMapTiles.add(storageRack17);
        StorageRack storageRack18 = new StorageRack(centerOnTile(101, 64), new Point(0, 0));
        enhancedMapTiles.add(storageRack18);

        dangerEntity = new DangerEntity(getMapTile(82, 97).getLocation()); // 93 38 og
        dangerEntity.setIsUpdateOffScreen(true);
        dangerEntity.setIsHidden(true);
        enhancedMapTiles.add(dangerEntity);

        VentEntity ventEnemy = new VentEntity(getMapTile(94, 84).getLocation());
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
    //exit trigger
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

    @Override 
    public void update(Player player) {
        super.update(player);
        if(MiniGame.getIsCorrect() == true && spawnedIn == false) {
            dangerEntity.triggerSpawnEvent(player);
            spawnedIn = true;
        }
    }

    public DangerEntity getDangerEntity() {
        return this.dangerEntity;
    }

    // Decorations are 2x2 tiles. Returns the top-left pixel position that centres the object on tile (x, y)
    private Point centerOnTile(int x, int y) {
        int tileSize = getTileset().getScaledSpriteWidth();
        return new Point((x - 0.5f) * tileSize, (y - 0.5f) * tileSize);
    }

}

