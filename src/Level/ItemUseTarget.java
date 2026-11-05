package Level;

import EnhancedMapTiles.CarriableObject;

// A target activated by using an equipped item, rather than by walking into it.
public class ItemUseTarget extends Trigger {
    private final Class<? extends CarriableObject> acceptedItemType;

    public ItemUseTarget(float x, float y, int width, int height,
                         Class<? extends CarriableObject> acceptedItemType,
                         Script useScript, String existenceFlag) {
        super(x, y, width, height, useScript, existenceFlag);
        this.acceptedItemType = acceptedItemType;
        useScript.setMapEntity(this);
        // Movement collision checks skip this target; item use checks it explicitly.
        setIsUncollidable(true);
    }

    public boolean accepts(CarriableObject item) {
        return acceptedItemType.isInstance(item);
    }
}
