/*package Scripts.TestMap;

import java.util.ArrayList;
import Level.Script;
import ScriptActions.*;
import ScriptActions.ScriptAction;

public class BlockedTileScript extends Script{
    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new BlockedTileScriptAction());
        return actions;
    }
}

/*public class BlockedTileScript extends Script{
        @Override
        public ArrayList<ScriptAction> loadScriptActions() {
            ArrayList<ScriptAction> scriptActions = new ArrayList<>();
            scriptActions.add(new LockPlayerScriptAction());
            
            scriptActions.add(new ConditionalScriptAction() {{
                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                    addRequirement(new FlagRequirement("hasAlreadyWent to wall", false));
                    addScriptAction(new TextboxScriptAction() {{
                        addText("Hmm, looks like I can't go this way.");
                        addText("Maybe there's a way to unlock this area??...I should probaly look around");                }});
                    addScriptAction(new ChangeFlagScriptAction("hasTalkedToWalrus", true));
                }});

                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                    addRequirement(new FlagRequirement("hasTalkedToWalrus", true));
                    addScriptAction(new TextboxScriptAction("I sure love doing walrus things!"));
                }});
            }});

            scriptActions.add(new UnlockPlayerScriptAction());
            return scriptActions;
        }  
}      */