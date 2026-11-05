package Scripts.TestMap;

import java.util.ArrayList;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;

// Walking into the exit explains item use, but never changes the level.
public class Level1ExitHintScript extends Script {
    private boolean hintShown;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        ConditionalScriptAction hint = new ConditionalScriptAction();
        ConditionalScriptActionGroup firstVisit = new ConditionalScriptActionGroup();
        firstVisit.addRequirement(new CustomRequirement() {
            @Override
            public boolean isRequirementMet() { return !hintShown; }
        });
        firstVisit.addScriptAction(new LockPlayerScriptAction());
        firstVisit.addScriptAction(new TextboxScriptAction(
                "This exit needs a keycard.\nEquip it with its number key, then left-click here to use it."));
        firstVisit.addScriptAction(new ScriptAction() {
            @Override
            public ScriptState execute() {
                hintShown = true;
                return ScriptState.COMPLETED;
            }
        });
        firstVisit.addScriptAction(new UnlockPlayerScriptAction());
        hint.addConditionalScriptActionGroup(firstVisit);
        actions.add(hint);
        return actions;
    }
}
