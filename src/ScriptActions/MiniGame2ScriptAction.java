package ScriptActions;

import Level.ScriptState;
import Screens.MiniGame2;


public class MiniGame2ScriptAction extends ScriptAction{
    @Override 
    public ScriptState execute() {
        new MiniGame2("Minigame 2", 500, 500);
       
        return ScriptState.COMPLETED;
    }
}
