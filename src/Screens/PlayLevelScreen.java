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
import Music.MusicDirector;
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
    }

    public void update() {
        // the music follows whichever map is loaded, so every map transition picks up its new track automatically
        // this goes BEFORE the switch: the win screen can call goBackToMenu() inside the switch, and running
        // the director after that would start the music right back up over the menu
        musicDirector.update(map, player);
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                player.update();
                map.update(player);
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
        onCutscene1Completed();
    }


    @Override
    public void onPrologueCompleted() {
        // when this method is called within the game, it signals the game has been "won"
        System.out.println("Level completed!");
        map = new Cutscene1Map();
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
    }

    @Override
    public void onElevatorDrop() {
        // when this method is called within the game, it signals the game has been "won"
        System.out.println("Level completed!");
        map = new NothingMap();
        map.setFlagManager(flagManager);

        //setup player
        int playerHealth = 3;
        player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y, playerHealth);
        player.setMap(map);
        playLevelScreenState = PlayLevelScreenState.DARK;
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

    }

    @Override
    public void onCutscene1Completed() {
        // when this method is called within the game, it signals the game has been "won"
        System.out.println("Level completed!");
        map = new Level1Map();
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
    }

    @Override
    public void onLevel1Completed() {
        // when this method is called within the game, it signals the game has been "won"
        System.out.println("Level completed!");
        //will put level 2 map here
        map = new ProlougeMap();
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
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
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
        // this screen is about to be thrown away, so its music has to be shut off now or it keeps playing over the menu
        musicDirector.stop();
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, DARK
    }
}
