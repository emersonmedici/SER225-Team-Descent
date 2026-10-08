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

import Utils.Direction;
import Utils.Point;


import java.util.HashMap;
import java.util.List;


public class DangerEntity extends EnhancedMapTile {

        private float timeToAttack = 2;
        private float timer = timeToAttack * 60;

        private float speed = 2.3f;
        private Direction direction;

        private boolean hasStarted = false;

        private Point lastPlayerPos;

        private NodeBase[][] nodes;
        private List<NodeBase> path;
        private NodeBase nextNode;
        private int indexInPath = 1;


    public DangerEntity(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Dinosaur.png"), 14, 17), TileType.PASSABLE);
    }

    public void start(Player player) {
        nodes = map.getNodes();

        AStar aStar = new AStar(nodes);

        Point playerStart = map.getTileIndexByPosition(player.getLocation().x, player.getLocation().y);
        lastPlayerPos = playerStart;
        Point entityStart = map.getTileIndexByPosition(getLocation().x, getLocation().y);

        path = aStar.findPath(nodes[(int)entityStart.x][(int)entityStart.y], nodes[(int)playerStart.x][(int)playerStart.y]);
        
        if (path.size() > 1) {
            indexInPath = 1;
            nextNode = path.get(indexInPath);
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

            // Get the current grid position of the entity and player
            // Point entityPos = map.getTileIndexByPosition(
            //     getLocation().x,
            //     getLocation().y
            // );

            // Point playerPos = map.getTileIndexByPosition(
            //     player.getLocation().x,
            //     player.getLocation().y
            // );

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

            // System.out.println(
            //     "Entity Node X: " + entityPos.x +
            //     " | Entity Node Y: " + entityPos.y + 
            //     " | Player Node X: " + playerPos.x +
            //     " | Player Node Y: " + playerPos.y
            // );

            // Recalculate the path when the player enters a new tile
            if (lastPlayerPos == null ||
                playerPos.x != lastPlayerPos.x ||
                playerPos.y != lastPlayerPos.y) {

                updatePath(entityPos, playerPos);

                lastPlayerPos = playerPos;
            }

            // Make sure there is a valid path
            if (path != null && path.size() > 1 && indexInPath < path.size()) {

                // Get the direction toward the next node
                direction = getMoveDirection(
                    nodes[(int)entityPos.x][(int)entityPos.y],
                    path.get(indexInPath)
                );

                // Move toward the next node
                if (direction == Direction.UP) {
                    moveY(-speed);
                }
                else if (direction == Direction.DOWN) {
                    moveY(speed);
                }
                else if (direction == Direction.LEFT) {
                    moveX(-speed);
                }
                else if (direction == Direction.RIGHT) {
                    moveX(speed);
                }
                else if (direction == Direction.UPLEFT) {
                    moveX(-speed);
                    moveY(-speed);
                }
                else if (direction == Direction.UPRIGHT) {
                    moveX(speed);
                    moveY(-speed);
                }
                else if (direction == Direction.DOWNLEFT) {
                    moveX(-speed);
                    moveY(speed);
                }
                else if (direction == Direction.DOWNRIGHT) {
                    moveX(speed);
                    moveY(speed);
                }

                // Check if the entity reached the next node
                if (entityPos.x == path.get(indexInPath).getX()
                        && entityPos.y == path.get(indexInPath).getY()) {

                    indexInPath++;

                    // If we reached the end of the path,
                    // get a new path to the player's current position
                    if (indexInPath >= path.size()) {
                        updatePath(entityPos, playerPos);
                    }
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


    private void updatePath(Point entityPos, Point playerPos) {

        AStar aStar = new AStar(nodes);

        List<NodeBase> newPath = aStar.findPath(
            nodes[(int)entityPos.x][(int)entityPos.y],
            nodes[(int)playerPos.x][(int)playerPos.y]
        );
        // System.out.println("PATH:");

        // for (NodeBase node : newPath) {
        //     System.out.println(
        //         "(" + node.getX() + ", " + node.getY() + ")"
        //     );
        // }
        

   //     System.out.println("New path size: " + newPath.size());

        if (newPath.size() > 1) {
            path = newPath;
            indexInPath = 1;

         //   System.out.println("New path found!");
        }
        else {
         //   System.out.println("NO PATH FOUND!");
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
