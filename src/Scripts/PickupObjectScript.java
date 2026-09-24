package Scripts;

import java.util.ArrayList;
import Level.Script;
import ScriptActions.PickupObjectScriptAction;
import ScriptActions.ScriptAction;

public class PickupObjectScript extends Script {
    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new PickupObjectScriptAction());
        return actions;
    }
}
