package Scripts;

import java.util.ArrayList;

import EnhancedMapTiles.CarriableObject;
import Level.Script;
import Level.ScriptState;
import ScriptActions.ScriptAction;

public class PickupObjectScript extends Script {
    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();

        actions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                if (entity instanceof CarriableObject) {
                    player.pickUpObject((CarriableObject) entity);
                }

                return ScriptState.COMPLETED;
            }
        });

        return actions;
    }
}
