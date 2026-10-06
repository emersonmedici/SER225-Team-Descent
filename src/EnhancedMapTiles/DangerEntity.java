package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.NPC;
import Level.Player;
import Level.PlayerState;
import Level.TileType;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;


public class DangerEntity extends EnhancedMapTile {

        private float timeToAttack = 2;
        private float timer = timeToAttack * 60;
        
    public DangerEntity(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Dinosaur.png"), 16, 16), TileType.PASSABLE);
    }

    @Override
    public void update(Player player) {
        super.update(player);
        timer--;
        if(player.touching(this) == true && canAttack() == true) {
                player.DecreaseHealth(1);
                System.out.println("Attack");
        }


    }

    private boolean canAttack() {
        if(timer > 0) {   
                return false;
        }
        timer = timeToAttack * 60;
        return true;
    }

    
    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
        Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
                .withScale(3)
                .build();
        return new GameObject(x, y, frame);
    }
}
