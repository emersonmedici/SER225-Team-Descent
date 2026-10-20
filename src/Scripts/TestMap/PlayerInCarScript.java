package Scripts.TestMap;

import java.util.ArrayList;

import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class PlayerInCarScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new WaitScriptAction(180));
        

        scriptActions.add(new TextboxScriptAction() {{
            addText("Tyson: What the heck?");
            addText("Tyson: No, come on, drive!!");
            addText("Tyson: Dang it! The car won't start! Why break down \nnow of all times?!");
        }});

        //scriptActions.add(new ChangeFlagScriptAction("hasEnteredBuilding", true));

        //scriptActions.add(new UnlockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                for (GameListener listener: listeners) {
                    listener.onPrologueGameplayStarted();
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}
