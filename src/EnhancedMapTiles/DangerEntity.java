package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.GameListener;
import Level.NPC;
import Level.Player;
import Level.PlayerState;
import Level.TileType;
import Level.Map;
import Level.NodeBase;
import Level.AStar;
import Level.StepCounter;
import Level.Threat;
import Sound.SoundEffect;
import Sound.SoundManager;

import Utils.Direction;
import Utils.Point;


import java.util.HashMap;
import java.util.List;


public class DangerEntity extends EnhancedMapTile implements Threat {

        private float timeToAttack = 2;
        private float timer = timeToAttack * 60;

        private float speed = 2.3f;
        private Direction direction;

        private boolean hasStarted = false;

        private Point lastPlayerPos;

        private NodeBase[][] nodes;
        private List<NodeBase> path;
        private int indexInPath = 1;

        // long stride = slow, heavy footsteps (about 2 a second) -- it doesn't need to hurry
        private StepCounter footsteps = new StepCounter(64);


    public DangerEntity(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("DangerEntity.png"), 46, 32), TileType.PASSABLE);
        
    }

    public void start(Player player) {
        nodes = map.getLargeEntityNodes();

        boolean useDiagonal = true;
        AStar aStar = new AStar(nodes, useDiagonal);

        Point playerStart = map.getTileIndexByPosition(player.getLocation().x, player.getLocation().y);
        lastPlayerPos = playerStart;
        Point entityStart = map.getTileIndexByPosition(getLocation().x, getLocation().y);

        path = aStar.findPath(nodes[(int)entityStart.x][(int)entityStart.y], nodes[(int)playerStart.x][(int)playerStart.y]);
        
        if (path.size() > 1) {
            indexInPath = 1;
        }

    }

    @Override
    public void update(Player player) {
        if (hasStarted == false) {
            start(player);
            player.setHealth(3);
        }
        else {
            super.update(player);

            timer--;

            // Attack the player if touching them
            if (player.touching(this) == true && canAttack() == true) {
                player.decreaseHealth(1);
                System.out.println("ATTACK");
            }

            if(player.getHealth() <= 0) {
                for (GameListener listener : map.getListeners()) {
                    listener.onRestart();
                    hasStarted = false;
                }
            }

            float playerCenterX =
                player.getX() + player.getWidth() / 2.0f;

            float playerCenterY =
                player.getY() + player.getHeight() / 2.0f;

            Point playerPos = map.getTileIndexByPosition(
                playerCenterX,
                playerCenterY
            );

            float entityCenterX =
                getX() + getWidth() / 2.0f;

            float entityCenterY =
                getY() + getHeight() / 2.0f;

            Point entityPos = map.getTileIndexByPosition(
                entityCenterX,
                entityCenterY
            );


            // Recalculate the path when the player enters a new tile
            if (lastPlayerPos == null ||
                playerPos.x != lastPlayerPos.x ||
                playerPos.y != lastPlayerPos.y) {

                updatePath(getPathStartNode(entityPos), playerPos);

                lastPlayerPos = playerPos;
            }

            // Make sure there is a valid path
            if (path != null && indexInPath < path.size()) {

                NodeBase nextNode = path.get(indexInPath);

                int tileWidth = map.getTileset().getScaledSpriteWidth();
                int tileHeight = map.getTileset().getScaledSpriteHeight();

                // Pixel position that centers the entity on the next tile
                float targetX = nextNode.getX() * tileWidth + (tileWidth - getWidth()) / 2.0f;
                float targetY = nextNode.getY() * tileHeight + (tileHeight - getHeight()) / 2.0f;

                float dx = targetX - getX();
                float dy = targetY - getY();
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance <= speed) {
                    // Close enough: snap exactly onto the tile, then aim for the next one
                    moveX(dx);
                    moveY(dy);
                    indexInPath++;

                    if (indexInPath >= path.size()) {
                        updatePath(getPathStartNode(entityPos), playerPos);
                    }
                }
                else {
                    // Move straight toward the target at a constant speed
                    moveX(dx / distance * speed);
                    moveY(dy / distance * speed);
                }

                // footsteps come from where the monster is, so you can hear how close it is and which side it's on
                if (footsteps.addDistance(Math.min(distance, speed))) {
                    Point center = getThreatCenter();
                    SoundManager.playAt(SoundEffect.MONSTER_STEP, center.x, center.y);
                }

                if (dx < 0) {
                    setAnimation("WALK_LEFT");
                }
                else if (dx > 0) {
                    setAnimation("WALK_RIGHT");
                }
            }
        }

        hasStarted = true;
    }

    private Direction getMoveDirection(NodeBase currentNode, NodeBase nextNode) {
        int xDifference = nextNode.getX() - currentNode.getX();
        int yDifference = nextNode.getY() - currentNode.getY();

        if (xDifference > 0 && yDifference < 0) {
            return Direction.UPRIGHT;
        }
        else if (xDifference < 0 && yDifference < 0) {
            return Direction.UPLEFT;
        }
        else if (xDifference > 0 && yDifference > 0) {
            return Direction.DOWNRIGHT;
        }
        else if (xDifference < 0 && yDifference > 0) {
            return Direction.DOWNLEFT;
        }
        else if (xDifference > 0) {
            return Direction.RIGHT;
        }
        else if (xDifference < 0) {
            return Direction.LEFT;
        }
        else if (yDifference > 0) {
            return Direction.DOWN;
        }
        else if (yDifference < 0) {
            return Direction.UP;
        }

        return null;
    }


    private void updatePath(NodeBase startNode, Point playerPos) {

        boolean useDiagonal = true;   // keep false in VentEntity
        AStar aStar = new AStar(nodes, useDiagonal);

        List<NodeBase> newPath = aStar.findPath(
            startNode,
            nodes[(int)playerPos.x][(int)playerPos.y]
        );

        if (!newPath.isEmpty()) {
            path = newPath;
            indexInPath = 0;
        }
    }

    private boolean canAttack() {
        if(timer > 0) {   
                return false;
        }
        timer = timeToAttack * 60;
        return true;
    }

    // Threat: this entity chases the player as soon as it has started, unless something has hidden it
    @Override
    public boolean isHunting() {
        return hasStarted && !isHidden();
    }

    @Override
    public Point getThreatCenter() {
        return new Point(getX() + getWidth() / 2.0f, getY() + getHeight() / 2.0f);
    }

    // The node to start a new path from: the tile the entity is currently
    // walking toward, or the tile it's standing on if it isn't moving
    private NodeBase getPathStartNode(Point entityPos) {
        if (path != null && indexInPath < path.size()) {
            return path.get(indexInPath);
        }
        return nodes[(int)entityPos.x][(int)entityPos.y];
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });
            put("STAND_RIGHT", new Frame[] {
                   new FrameBuilder(spriteSheet.getSprite(0, 0))
                           .withScale(3)
                           .withBounds(4, 5, 5, 10)
                           .build()
           });

            put("WALK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });

            put("WALK_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });

            put("ATTACK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });

            put("ATTACK_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });
        }};

        
    }

    private void setAnimation(String animationName) {
        bottomLayer.setCurrentAnimationName(animationName);
    }

    
    @Override
    protected GameObject loadBottomLayer(SpriteSheet spriteSheet) {
      //  Frame frame = new FrameBuilder(spriteSheet.getSubImage(0, 0))
          //      .withScale(3)
               // .build();
        return new GameObject(x, y, loadAnimations(spriteSheet), "WALK_LEFT");
    }
}
