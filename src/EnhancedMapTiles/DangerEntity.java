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

        private float speed = 10;
        private Direction direction;

        private boolean hasStarted = false;

        private Point lastPlayerPos;

        private NodeBase[][] nodes;
        private List<NodeBase> path;
        private NodeBase nextNode;
        private int indexInPath = 1;

    public DangerEntity(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Dinosaur.png"), 16, 16), TileType.PASSABLE);
    }

    public void start(Player player) {
        nodes = map.getNodes();
        //System.out.println(nodes[5][5].getX());
        AStar aStar = new AStar(nodes);
        //Point playerStart = map.getplayer.getLocation();
        //Point entityStart = getLocation();
        Point playerStart = map.getTileIndexByPosition(player.getLocation().x, player.getLocation().y);
        lastPlayerPos = playerStart;
        Point entityStart = map.getTileIndexByPosition(getLocation().x, getLocation().y);
    //    System.out.println("EntityStart: " + entityStart.x + " " + entityStart.y);
   //     System.out.println("PlayerEnd: " + playerStart.x + " " + playerStart.y);
     //   System.out.println("Size: " + nodes.length + " " + nodes[0].length);
        path = aStar.findPath(nodes[(int)entityStart.x][(int)entityStart.y], nodes[(int)playerStart.x][(int)playerStart.y]);
        nextNode = path.get(indexInPath);
     //   System.out.println("NextNode: " + nextNode.getX() + "  " + nextNode.getY());
    //  for(int i = 0; i < path.size(); i++) {
    //     System.out.println("Node: " + path.get(i).getX() + " " + path.get(i).getY());
    //  }
    System.out.println("should walk on + " + nodes[73][99].getWalkable());
    }

    @Override
    public void update(Player player) {
        if (hasStarted == false) {
            start(player);
        }
        else {
            super.update(player);

            timer--;

            // Attack the player if touching them
            if (player.touching(this) == true && canAttack() == true) {
                player.DecreaseHealth(1);
                System.out.println("Attack");
            }

            // Get the current grid position of the entity and player
            Point entityPos = map.getTileIndexByPosition(
                getLocation().x,
                getLocation().y
            );

            Point playerPos = map.getTileIndexByPosition(
                player.getLocation().x,
                player.getLocation().y
            );

            // Make sure the entity has a valid path
            if (path != null && path.size() > 1) {

                // Make sure indexInPath is still valid
                if (indexInPath >= path.size()) {
                    indexInPath = path.size() - 1;
                }

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

                    // Move to the next node in the path
                    indexInPath++;

                    // If we reached the end of the path,
                    // calculate a new path to the player's current position
                    if (indexInPath >= path.size()) {
                        updatePath(entityPos, playerPos);
                    }
                }
            }
            else {
                // There is no valid path to the player
                // Try to find one again
                updatePath(entityPos, playerPos);
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

        if (newPath.size() > 1) {
            path = newPath;
            indexInPath = 1;
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
