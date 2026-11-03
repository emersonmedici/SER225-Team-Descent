package Scripts.TestMap;

import java.util.ArrayList;
import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.ScriptAction;

// Walking up to the elevator in the level 2 room sends the player on to the next level.
public class ElevatorScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                for (GameListener listener : listeners) {
                    listener.onLevel1Completed();
                }
                return ScriptState.COMPLETED;
            }
        });
        return actions;
    }
}
