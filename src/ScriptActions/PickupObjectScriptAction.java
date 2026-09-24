package ScriptActions;

import EnhancedMapTiles.CarriableObject;
import Level.ScriptState;

public class PickupObjectScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        if (entity instanceof CarriableObject) {
            player.pickUpObject((CarriableObject) entity);
        }

        return ScriptState.COMPLETED;
    }
}
