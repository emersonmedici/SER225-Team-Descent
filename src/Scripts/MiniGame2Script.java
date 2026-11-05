package Scripts;

import java.util.ArrayList;
import Level.Script;
import ScriptActions.ChangeFlagScriptAction;
import ScriptActions.MiniGame2ScriptAction;
import ScriptActions.ScriptAction;

public class MiniGame2Script extends Script{
    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new MiniGame2ScriptAction());

        actions.add(new ChangeFlagScriptAction("hasCheckedLock", true));

        return actions;
    }
}