package Scripts.TestMap;

import java.util.ArrayList;
import EnhancedMapTiles.Keycard1;
import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;
import Sound.SoundEffect;
import Sound.SoundManager;

// Run by the exit's ItemUseTarget after a left-click with the correct item equipped.
public class Level1Script2 extends Script {

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
                // the keycard worked -- the exit door opens as the next map loads
                SoundManager.play(SoundEffect.DOOR_OPEN);
                for (GameListener listener : listeners) {
                    listener.onLevel2Completed();
                }
                return ScriptState.COMPLETED;
            }
        });
        exit.addConditionalScriptActionGroup(unlocked);

        actions.add(exit);
        return actions;
    }
}
