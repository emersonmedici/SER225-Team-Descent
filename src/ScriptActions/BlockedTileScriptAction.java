/*package ScriptActions;

import Level.EnhancedMapTile;
import Level.ScriptState;
import java.util.ArrayList;
import EnhancedMapTiles.BlockedWallTile;

public class BlockedTileScriptAction extends ScriptAction{
    @Override 
    public ScriptState execute() {
        //@Override
       // public ArrayList<ScriptAction> loadScriptActions() {
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
           // }});

            scriptActions.add(new UnlockPlayerScriptAction());
            return ScriptState.COMPLETED;
        }  
    }
}      */