package Level;

// Determines where a Decoration is drawn relative to map tiles and map entities (player, npcs, etc.)
public enum DecorationLayer {
    // drawn right on top of the map tiles' bottom layer, underneath everything else (rugs, flowers, cracks, puddles)
    GROUND("Ground"),

    // depth sorted with the player and npcs based on where the bottom of the decoration is
    // player walking "behind" it gets covered by it, player walking "in front of" it covers it (barrels, signs, lamp posts)
    DEPTH_SORTED("Depth sorted"),

    // drawn over everything, including the player and the map tiles' top layer (tree canopies, roofs, clouds)
    OVERHEAD("Overhead");

    private final String displayName;

    DecorationLayer(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
