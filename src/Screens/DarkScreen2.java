package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the dark screen when the elevator first drops
public class DarkScreen2 extends Screen {
    protected SpriteFont text;
    protected SpriteFont text2;

    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;

    public DarkScreen2(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        text = new SpriteFont("Tyson: Hang in there, Dave. I'm gonna get you to the", 35, 472, "Arial", 30, Color.black);
        text2 = new SpriteFont("hospital!", 35, 510, "Arial", 30, Color.black);
        keyLocker.lockKey(Key.SPACE);
        //keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        /*if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }*/

        // if space is pressed, reset level. if escape is pressed, go back to main menu
        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            playLevelScreen.onPrologueCutscene();
        } /*else if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            playLevelScreen.onCutscene1Completed();
        }*/
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        graphicsHandler.drawFilledRectangle(22, 460, 750, 100, Color.white);
        text.draw(graphicsHandler);
        text2.draw(graphicsHandler);
        //instructions.draw(graphicsHandler);
    }
}

    


    

