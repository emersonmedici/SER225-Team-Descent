package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class LockedDialogueScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Tyson: This thing is locked tight, no way I'm getting \nthrough there.");
            addText("Tyson: Maybe there's some way to unlock it...");
            //addText("Maybe Walrus has seen it.");
        }});

        scriptActions.add(new ChangeFlagScriptAction("hasCheckedLock", true));

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
