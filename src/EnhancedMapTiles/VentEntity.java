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
import Level.MapTile;

import Utils.Direction;
import Utils.Point;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;


public class VentEntity extends EnhancedMapTile {

        private float timeToAttack = 2;
        private float attackTimer = timeToAttack * 60;

        private float maxTimeInVent = 5;
        private float ventTimer = 0;
        private float respawnDelay = 2;

        private float speed = 2.3f;
        private Direction direction;

        private boolean hasStarted = false;

        private Point lastPlayerPos;

        private NodeBase[][] nodes;
        private List<NodeBase> path;
        private int indexInPath = 1;

        private boolean spawnedIn = false;
        private boolean despawnedOut = true;


    public VentEntity(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Dinosaur.png"), 14, 17), TileType.PASSABLE);
    }

    public void start(Player player) {
        nodes = map.getVentEntityNodes();

        boolean useDiagonal = false;
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

            attackTimer--;

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

            if(player.isPlayerInVent() == true) {
                ventTimer++;
                if(ventTimer > maxTimeInVent * 60 + 120) {
                    ventTimer = maxTimeInVent * 60 + 120;
                }
            }
            else {
                ventTimer--;
                if(ventTimer <= 0) {
                    ventTimer = 0;
                }
            }


            if (ventTimer > maxTimeInVent * 60 && spawnedIn == false) {
                spawnIntoVent(playerPos);
            }
            else if (ventTimer <= maxTimeInVent * 60) {
                spawnedIn = false;
                setLocation(0, 0);
                clearPath();
            }

            if (spawnedIn == true) {

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

                    boolean foundPath = updatePath(getPathStartNode(entityPos), playerPos);
                    lastPlayerPos = playerPos;

                    // Player is in a vent this one can't reach (a different vent), so respawn there
                    if (foundPath == false && player.isPlayerInVent() == true) {
                        ventTimer = maxTimeInVent * 60 - respawnDelay * 60;
                    }
                }

                // Make sure there is a valid path
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

                    if (dx < 0) {
                        setAnimation("WALK_LEFT");
                    }
                    else if (dx > 0) {
                        setAnimation("WALK_RIGHT");
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


    private boolean updatePath(NodeBase startNode, Point playerPos) {

        boolean useDiagonal = false;
        AStar aStar = new AStar(nodes, useDiagonal);

        List<NodeBase> newPath = aStar.findPath(
            startNode,
            nodes[(int)playerPos.x][(int)playerPos.y]
        );

        if (!newPath.isEmpty()) {
            path = newPath;
            indexInPath = 0;
            return true;
        }
        return false;
    }

    private boolean canAttack() {
        if(attackTimer > 0) {   
                return false;
        }
        attackTimer = timeToAttack * 60;
        return true;
    }

    // The node to start a new path from: the tile the entity is currently
    // walking toward, or the tile it's standing on if it isn't moving
    private NodeBase getPathStartNode(Point entityPos) {
        if (path != null && indexInPath < path.size()) {
            return path.get(indexInPath);
        }
        return nodes[(int)entityPos.x][(int)entityPos.y];
    }

    // Forget the old path so the next search starts from where the entity actually is
    private void clearPath() {
        path = null;
        indexInPath = 0;
    }

    // Teleport to the vent opening nearest the player and path from there
    private void spawnIntoVent(Point playerPos) {
        spawnedIn = true;

        Point startPos = findStartPosition(playerPos);
        setLocation(startPos.x, startPos.y);
        clearPath();

        Point entityTile = map.getTileIndexByPosition(
            getX() + getWidth() / 2.0f,
            getY() + getHeight() / 2.0f
        );
        updatePath(nodes[(int)entityTile.x][(int)entityTile.y], playerPos);
        lastPlayerPos = playerPos;
    }

    private Point findStartPosition(Point playerPos) {
        NodeBase endpoint = findVentEndpoint((int)playerPos.x, (int)playerPos.y);
        if (endpoint == null) {
            return getLocation(); // or keep it hidden
        }
        int tileW = map.getTileset().getScaledSpriteWidth();
        int tileH = map.getTileset().getScaledSpriteHeight();
        return new Point(endpoint.getX() * tileW, endpoint.getY() * tileH);
    }

    private NodeBase findVentEndpoint(int startX, int startY) {
        boolean[][] visited = new boolean[nodes.length][nodes[0].length];
        ArrayDeque<NodeBase> queue = new ArrayDeque<>();

        queue.add(nodes[startX][startY]);
        visited[startX][startY] = true;

        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

        while (!queue.isEmpty()) {
            NodeBase current = queue.poll();

            for (int[] d : directions) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];

                MapTile neighborTile = map.getMapTile(nx, ny);
                if (neighborTile == null || visited[nx][ny]) {
                    continue;
                }

                // Vent tile next to a walkable tile = vent opening
                if (neighborTile.getTileType() == TileType.PASSABLE) {
                    return current;
                }

                // Keep searching only through vent tiles
                if (neighborTile.getTileType() == TileType.VENT) {
                    visited[nx][ny] = true;
                    queue.add(nodes[nx][ny]);
                }
            }
        }
        return null; // no opening found
    }


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });
            put("STAND_RIGHT", new Frame[] {
                   new FrameBuilder(spriteSheet.getSprite(0, 0))
                           .withScale(3)
                           .withBounds(4, 5, 5, 10)
                           .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                           .build()
           });

            put("WALK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withBounds(4, 5, 5, 10)
                            .build()
            });

            put("WALK_RIGHT", new Frame[]{
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
