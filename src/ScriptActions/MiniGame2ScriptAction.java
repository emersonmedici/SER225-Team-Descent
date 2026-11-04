package ScriptActions;

import Engine.GameWindow;
import Level.ScriptState;
import Screens.MiniGame2;


public class MiniGame2ScriptAction extends ScriptAction{
    @Override 
    public ScriptState execute() {
        MiniGame2 miniGame2 = new MiniGame2("Minigame 2", 500, 500);
        GameWindow gameWindow = GameWindow.getGameWindow();
        gameWindow.displayMinigame(miniGame2.getPanel());       
        return ScriptState.COMPLETED;
    }
}
