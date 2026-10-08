package ScriptActions;

import Game.GameState;
import Game.ScreenCoordinator;
import Maps.Level1Map;
import Level.*;
import Level.GameListener;
//this is a scrapped script!

public class ScreenChangeScriptAction extends ScriptAction {
    //protected ScreenCoordinator screenCoordinator;
    //protected Map map;

    

    @Override
    public ScriptState execute() {
       //screenCoordinator.setGameState(GameState.CREDITS);
       //map.setGameState(GameState.CREDITS);
        //map = new Level1Map();
        //setMap(map);
        //setMap(new Level1Map());
        for (GameListener listener: listeners) {
            listener.onLevelCompleted();
        }

        return ScriptState.COMPLETED;
    }
}
//nvm