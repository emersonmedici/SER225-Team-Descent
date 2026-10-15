package Scripts.TestMap;

import java.util.ArrayList;
import EnhancedMapTiles.Keycard1;
import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;

// The Level 1 exit requires the keycard to be selected above the cat.
public class Level1Script2 extends Script {
    private boolean hintShown;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        ConditionalScriptAction exit = new ConditionalScriptAction();
        ConditionalScriptActionGroup unlocked = new ConditionalScriptActionGroup();
        unlocked.addRequirement(new CustomRequirement() {
            @Override
            public boolean isRequirementMet() {
                return player.getCarriedObject() instanceof Keycard1;
            }
        });
        unlocked.addScriptAction(new ScriptAction() {
            @Override
            public ScriptState execute() {
                for (GameListener listener : listeners) {
                    listener.onLevel1Completed();
                }
                return ScriptState.COMPLETED;
            }
        });
        exit.addConditionalScriptActionGroup(unlocked);

        ConditionalScriptActionGroup locked = new ConditionalScriptActionGroup();
        locked.addRequirement(new CustomRequirement() {
            @Override
            public boolean isRequirementMet() {
                return !hintShown;
            }
        });
        locked.addScriptAction(new LockPlayerScriptAction());
        locked.addScriptAction(new TextboxScriptAction(
                "This exit needs a keycard.\nSelect it with its number key, then approach the exit."));
        locked.addScriptAction(new ScriptAction() {
            @Override
            public ScriptState execute() {
                // Avoid reopening dialogue every frame while touching the exit.
                hintShown = true;
                return ScriptState.COMPLETED;
            }
        });
        locked.addScriptAction(new UnlockPlayerScriptAction());
        exit.addConditionalScriptActionGroup(locked);
        actions.add(exit);
        return actions;
    }
}
