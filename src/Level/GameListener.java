package Level;

public interface GameListener {
    void onWin();
    void onRestart();
    void onPrologueCutscene();
    void onPrologueGameplayStarted();
    void onPrologueCompleted();
    void onElevatorDrop();
    void onCutscene1Completed();
    void onLevel1Completed();
    void onLevel2Completed();

}
