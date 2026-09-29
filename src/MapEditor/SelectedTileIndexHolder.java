package MapEditor;

public class SelectedTileIndexHolder {
    private int selectedTileIndex;
    private int rotation;

    public int getSelectedTileIndex() {
        return selectedTileIndex;
    }

    public void setSelectedTileIndex(int selectedTileIndex) {
        this.selectedTileIndex = selectedTileIndex;
    }

    public int getSelectedTileRotation(){
        return rotation;
    }

    public void setSelectedTileRotation(int rotation){
        this.rotation = rotation;
    }
}
