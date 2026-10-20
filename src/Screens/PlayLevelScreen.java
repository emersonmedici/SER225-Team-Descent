package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.Cutscene1Map;
import Maps.Level1Map;
import Maps.NothingMap;
import Maps.ProlougeMap;
import Players.Cat;
import Utils.Direction;

// This class is for when the RPG game is actually being played
public class PlayLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected DarkScreen1 darkScreen1;
    protected InventoryScreen inventoryScreen;
    protected FlagManager flagManager;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
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

        // define/setup map
        map = new ProlougeMap();
        //map = new Level1Map();
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
        inventoryScreen = new InventoryScreen(this);
        inventoryScreen.initialize();
    }

    public void update() {
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
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
        loadMap(new Level1Map(), PlayLevelScreenState.RUNNING, false);
    }

    @Override
    public void onPrologueCompleted() {
        System.out.println("Level completed!");
        loadMap(new Cutscene1Map(), PlayLevelScreenState.RUNNING, true);
    }

    @Override
    public void onElevatorDrop() {
        System.out.println("Level completed!");
        loadMap(new NothingMap(), PlayLevelScreenState.DARK, true);
        darkScreen1.initialize();
    }

    @Override
    public void onCutscene1Completed() {
        System.out.println("Level completed!");
        loadMap(new Level1Map(), PlayLevelScreenState.RUNNING, true);
    }

    @Override
    public void onLevel1Completed() {
        System.out.println("Level completed!");
        // Keep the current placeholder destination until Level 2 is ready.
        loadMap(new ProlougeMap(), PlayLevelScreenState.RUNNING, true);
    }

    private void loadMap(Map nextMap, PlayLevelScreenState state, boolean keepPlayer) {
        nextMap.setFlagManager(flagManager);
        if (!keepPlayer) {
            player = new Cat(nextMap.getPlayerStartPosition().x,
                    nextMap.getPlayerStartPosition().y, 3);
        }
        player.moveToMap(nextMap);
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
        }
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, DARK
    }
}
