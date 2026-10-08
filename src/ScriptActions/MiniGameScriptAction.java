package ScriptActions;

import Level.ScriptState;
import Screens.MiniGame;


public class MiniGameScriptAction extends ScriptAction{
    @Override 
    public ScriptState execute() {
        new MiniGame("Minigame 1", 500, 500);

        return ScriptState.COMPLETED;
    }
}
