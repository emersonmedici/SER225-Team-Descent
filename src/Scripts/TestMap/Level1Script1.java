package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class Level1Script1 extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Oof! What the heck?");
            addText("Crap! The elevator dropped, how am I going to get \nback up?");
            addText("I need to find a way out of here...");
        }});

        scriptActions.add(new ChangeFlagScriptAction("hasLanded", true));

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
