package ScriptActions;

import Game.GameState;
import Game.ScreenCoordinator;
import Level.ScriptState;
import Maps.Level1Map;
import Level.*;


public class ScreenChangeScriptAction extends ScriptAction {
    protected ScreenCoordinator screenCoordinator;
    //protected Map map;

    @Override
    public ScriptState execute() {
       screenCoordinator.setGameState(GameState.CREDITS);
       //map.setGameState(GameState.CREDITS);
        //map = new Level1Map();
        //setMap(map);
        //setMap(new Level1Map());
        return ScriptState.COMPLETED;
    }
}
//nvm