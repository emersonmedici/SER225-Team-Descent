package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.PlayerState;
import Level.TileType;
import Utils.Point;

public class MudObject extends EnhancedMapTile {
   // private Player player;

    public MudObject(Point location) {
        super(
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Mud.png"), 16, 16),
            TileType.PASSABLE
        );
        // Passable pickups can overlap the player when interacted with.
        //setIsUncollidable(true);
    }

   // @Override
    /*public boolean mudCondition(Player player) {
      //  super();
        boolean walkSlower = false;
        
        if (player.touching(this)){ //&& player.getPlayerState() == PlayerState.WALKING) {
            walkSlower = true;
            //player.walkSpeed = 1.0f
        } else {
            walkSlower = false;
            //player.walkSpeed = 2.3f;
        }
        return walkSlower;
    }*/

    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
            .withScale(3)
            .build();

        return new GameObject(x, y, frame);
    }
}