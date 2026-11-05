package ScriptActions;
import Engine.GameWindow;
import Level.ScriptState;
import Screens.MiniGame;


public class MiniGameScriptAction extends ScriptAction{
    @Override 
    public ScriptState execute() {
        MiniGame miniGame = new MiniGame("Minigame 1", 500, 500);
        GameWindow gameWindow = GameWindow.getGameWindow();
        gameWindow.displayMinigame(miniGame.getPanel());
        return ScriptState.COMPLETED;
    }
}
