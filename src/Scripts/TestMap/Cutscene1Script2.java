package Scripts.TestMap;

import java.util.ArrayList;

import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class Cutscene1Script2 extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("It's so dark, I can't see...");
            addText("...an elevator?");
            addText("Let me see what I can--");
        }});

        //scriptActions.add(new ChangeFlagScriptAction("hasEnteredBuilding", true));

        //scriptActions.add(new UnlockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                for (GameListener listener: listeners) {
                    listener.onElevatorDrop();
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}
