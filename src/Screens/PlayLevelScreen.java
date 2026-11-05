package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Utils.Mouse;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.Cutscene1Map;
import Maps.Level1Map;
import Maps.Level1NewMap;
import Maps.NothingMap;
import Maps.ProlougeCutsceneMap;
import Maps.ProlougeMap;
import Music.MusicDirector;
import Players.Cat;
import Sound.SoundManager;
import Utils.Direction;

// This class is for when the RPG game is actually being played
public class PlayLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected DarkScreen1 darkScreen1;
    protected DarkScreen2 darkScreen2;
    protected InventoryScreen inventoryScreen;
    protected ItemInspectionScreen itemInspectionScreen;
    private KeyLocker inspectionKeyLocker;
    protected FlagManager flagManager;

    // created once here, NOT in initialize() -- resetLevel() calls initialize() again,
    // and a second director would leave the first one's music playing forever
    protected MusicDirector musicDirector = new MusicDirector();

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        // load every sound effect now, while the screen is changing anyway, so the first footstep doesn't stutter
        // (sounds stay loaded for the whole game, so coming back from the menu doesn't load them twice)
        SoundManager.preloadAll();
    }

    public void initialize() {
        Mouse.discardClicks();
        // setup state
        flagManager = new FlagManager();
        flagManager.addFlag("hasLostBall", false);
        flagManager.addFlag("hasTalkedToWalrus", false);
        flagManager.addFlag("hasTalkedToDinosaur", false);
        flagManager.addFlag("hasFoundBall", false);

        //adding a flag for dave npc
        flagManager.addFlag("hasTalkedToDave", false);
        flagManager.addFlag("hasEnteredBuilding", false);
        flagManager.addFlag("hasLanded", false);
        flagManager.addFlag("newFlagTest", false);
        flagManager.addFlag("hasCheckedLock", false);

        // define/setup map
        map = new ProlougeMap();
        //map = new Level1NewMap();
        map.setFlagManager(flagManager);

        // setup player
        int playerHealth = 3;
        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y, playerHealth);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.RUNNING;
        player.setFacingDirection(Direction.LEFT);

        map.setPlayer(player);

        // let pieces of map know which button to listen for as the "interact" button
        map.getTextbox().setInteractKey(player.getInteractKey());

        // add this screen as a "game listener" so other areas of the game that don't normally have direct access to it (such as scripts) can "signal" to have it do something
        // this is used in the "onWin" method -- a script signals to this class that the game has been won by calling its "onWin" method
        map.addListener(this);

        // preloads all scripts ahead of time rather than loading them dynamically
        // both are supported, however preloading is recommended
        map.preloadScripts();

        winScreen = new WinScreen(this);
        darkScreen1 = new DarkScreen1(this);
        darkScreen2 = new DarkScreen2(this);

        //adding this so it starts black
        playLevelScreenState = PlayLevelScreenState.DARK2;

        inventoryScreen = new InventoryScreen(this);
        inventoryScreen.initialize();
        itemInspectionScreen = new ItemInspectionScreen();
        itemInspectionScreen.initialize();
        inspectionKeyLocker = new KeyLocker();
    }

    public void update() {
        // Read once even when blocked, so ignored clicks cannot activate an item later.
        boolean usePressed = Mouse.consumeLeftClick();
        // the music follows whichever map is loaded, so every map transition picks up its new track automatically
        // this goes BEFORE the switch: the win screen can call goBackToMenu() inside the switch, and running
        // the director after that would start the music right back up over the menu
        musicDirector.update(map, player);
        boolean inspectPressed = false;
        if (Keyboard.isKeyUp(Key.E)) {
            inspectionKeyLocker.unlockKey(Key.E);
        } else if (!inspectionKeyLocker.isKeyLocked(Key.E)) {
            inspectionKeyLocker.lockKey(Key.E);
            inspectPressed = true;
        }
        if (playLevelScreenState == PlayLevelScreenState.RUNNING) {
            if (itemInspectionScreen.isOpen()) {
                itemInspectionScreen.update();
                if (!itemInspectionScreen.isOpen()) {
                    player.suppressInspectionInputsUntilRelease();
                }
                return;
            }
            if (inspectPressed && !player.isLocked() && !map.getTextbox().isActive()
                    && map.getActiveScript() == null) {
                itemInspectionScreen.open(player.getCarriedObject());
                if (itemInspectionScreen.isOpen()) return;
            }
        }
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                if (usePressed && !player.isLocked()) {
                    player.useEquippedItem();
                }
                player.update();
                map.update(player);
                inventoryScreen.update();
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                winScreen.update();
                break;
            case DARK:
                darkScreen1.update();
                break;
            case DARK2:
                darkScreen2.update();
                break;
        }
    }

    @Override
    public void onWin() {
        // when this method is called within the game, it signals the game has been "won"
        playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
    }

    @Override
    public void onRestart() {
        // Restart still returns to Level 1 with a fresh cat and empty inventory.
        System.out.println("Level completed!");
        loadMap(new Level1NewMap(), PlayLevelScreenState.RUNNING, false);
    }
    //scene to start the prologue
    @Override
    public void onPrologueCutscene() {
        System.out.println("Level completed!");
        loadMap(new ProlougeCutsceneMap(), PlayLevelScreenState.RUNNING, true);
    }

    //scene to start the prologue
    @Override
    public void onPrologueGameplayStarted() {
        System.out.println("Level completed!");
        loadMap(new ProlougeMap(), PlayLevelScreenState.RUNNING, true, 550, 1660);
    }


    //scene change from prologue to lobby
    @Override
    public void onPrologueCompleted() {
        System.out.println("Level completed!");
        loadMap(new Cutscene1Map(), PlayLevelScreenState.RUNNING, true);
    }

    @Override
    public void onLobbyTraversedTest() {
        System.out.println("Level completed!");
        loadMap(new Level1NewMap(), PlayLevelScreenState.RUNNING, true);
    }

    //from lobby cut to black
    @Override
    public void onElevatorDrop() {
        System.out.println("Level completed!");
        loadMap(new NothingMap(), PlayLevelScreenState.DARK, true);
        darkScreen1.initialize();
    }

    //from black to level 1
    @Override
    public void onCutscene1Completed() {
        System.out.println("Level completed!");
        // both levels open their BlockedWallTile2 doors with the simon says puzzle -- reset it so level 2's door starts locked
        MiniGame2.setWinGame(false);
        flagManager.unsetFlag("hasCheckedLock");
        loadMap(new Level1Map(), PlayLevelScreenState.RUNNING, true);
    }

    //from level 1 (the elevator in Level1NewMap) to level 2, the map with the killer
    @Override
    public void onLevel1Completed() {
        System.out.println("Level completed!");
        // both levels open their BlockedWallTile2 doors with the simon says puzzle -- reset it so level 2's door starts locked
        MiniGame2.setWinGame(false);
        // level 2 starts the danger entity's stages from minigame 1, so a win from before a death can't carry over
        MiniGame.setIsCorrect(false);
        flagManager.unsetFlag("hasCheckedLock");
        loadMap(new Level1Map(), PlayLevelScreenState.RUNNING, true);
    }

    //from level 2 to the next thing (currently just puts you back in the prologue map)
    @Override
    public void onLevel2Completed() {
        System.out.println("Level completed!");
        // Keep the current placeholder destination until Level 3 is ready.
        loadMap(new ProlougeMap(), PlayLevelScreenState.RUNNING, true);
    }

    private void loadMap(Map nextMap, PlayLevelScreenState state, boolean keepPlayer) {
        loadMap(nextMap, state, keepPlayer,
                nextMap.getPlayerStartPosition().x, nextMap.getPlayerStartPosition().y);
    }

    private void loadMap(Map nextMap, PlayLevelScreenState state, boolean keepPlayer,
                         float spawnX, float spawnY) {
        nextMap.setFlagManager(flagManager);
        if (!keepPlayer) {
            player = new Cat(spawnX, spawnY, 3);
        }
        player.moveToMap(nextMap, spawnX, spawnY);
        map = nextMap;
        playLevelScreenState = state;

        // Preserve the existing textbox, listener, and script setup on every map.
        map.getTextbox().setInteractKey(player.getInteractKey());
        map.addListener(this);
        map.preloadScripts();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                if (itemInspectionScreen.isOpen()) {
                    itemInspectionScreen.draw(graphicsHandler);
                    break;
                }
                // draws the inventory screen only when there is no textboxes (avoids blocking out cutscenes and stuff)
                if (!map.getTextbox().isActive()) {
                    inventoryScreen.draw(graphicsHandler);
                }
                break;
            case LEVEL_COMPLETED:
                winScreen.draw(graphicsHandler);
                break;
            case DARK:
                darkScreen1.draw(graphicsHandler);
                break;
            case DARK2:
                darkScreen2.draw(graphicsHandler);
                break;
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        // this screen is about to be thrown away, so its music has to be shut off now or it keeps playing over the menu
        musicDirector.stop();
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, DARK, DARK2
    }
}
