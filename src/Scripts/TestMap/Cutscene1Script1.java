package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class Cutscene1Script1 extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("There's nothing here!");
            addText("Is this place abandoned?");
            addText("I have to go in further, there's got to be something \nthat can help Dave...");
        }});

        scriptActions.add(new ChangeFlagScriptAction("hasEnteredBuilding", true));

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
