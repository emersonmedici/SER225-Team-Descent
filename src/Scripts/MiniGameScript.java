package Scripts;

import java.util.ArrayList;
import Level.Script;
import ScriptActions.MiniGameScriptAction;
import ScriptActions.ScriptAction;

public class MiniGameScript extends Script{
    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new MiniGameScriptAction());
        return actions;
    }
}
