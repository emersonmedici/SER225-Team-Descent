package Scripts.TestMap;

import java.util.ArrayList;

import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;
import Sound.SoundEffect;
import Sound.SoundManager;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class Cutscene1Script2 extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Tyson: Is this...");
            addText("Tyson: ...an elevator?");
            addText("Tyson: Let me see what I can--");
        }});

        //scriptActions.add(new ChangeFlagScriptAction("hasEnteredBuilding", true));

        //scriptActions.add(new UnlockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                // starts as the screen cuts to black, so the ride down is heard in the dark
                SoundManager.play(SoundEffect.ELEVATOR);
                for (GameListener listener: listeners) {
                    listener.onElevatorDrop();
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}
