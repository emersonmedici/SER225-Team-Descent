package Scripts.TestMap;

import java.util.ArrayList;

import Level.FlagManager;
import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.*;



// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class Level1Script2 extends Script {
    //protected FlagManager fM;
    //protected Map m;

    /*public Level1Script2(FlagManager flagManager) {
        fM = flagManager;
    }*/

    /*public Level1Script2(Map map) {
        m = map;
    }*/

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        //if(fM.isFlagSet("hasLanded")) {
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                for (GameListener listener: listeners) {
                  
                        listener.onLevel1Completed();
                    
                }
                return ScriptState.COMPLETED;
            }
        });
        //}

        /*scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 0;
                        //return fM.isFlagSet("hasLanded");
                    }
                });

                addScriptAction(new TextboxScriptAction() {{
                    addText("Hurry...");
                    //addText("I'm going to let you in on a little secret...\nYou can push some rocks out of the way.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 1;
                        return !(fM.isFlagSet("hasLanded"));
                    }
                });
                
                addScriptAction(new TextboxScriptAction("Tyson, do something!!"));
            }});
        }});

        */
        return scriptActions;
    }
}