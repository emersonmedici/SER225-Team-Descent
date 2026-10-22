package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.ImageEffect;
import GameObject.Rectangle;
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

        private static final int HIT_FRAME = 3;   // frame of ATTACK_LEFT/RIGHT where the weapon lands
        private boolean isAttacking = false;
        private boolean hasHitThisSwing = false;
        private int lastAttackFrameIndex = 0;
        private static final float SWORD_REACH = 48;   // weapon reaches 16 sprite pixels past the body, x3 scale


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

            // Follow the swing that's already playing
            if (isAttacking) {
                int frame = bottomLayer.getCurrentFrameIndex();

                // Deal damage once, on the frame where the weapon lands
                if (frame == HIT_FRAME && hasHitThisSwing == false) {
                    hasHitThisSwing = true;
                    if (isPlayerInReach(player) == true) {
                        player.decreaseHealth(1);
                        System.out.println("ATTACK");
                    }
                }

                // Frame index went backwards = the animation looped, so the swing is over
                if (frame < lastAttackFrameIndex) {
                    isAttacking = false;
                }
                lastAttackFrameIndex = frame;
            }

            // Start a new swing when the player is close and the cooldown is over
            if (isAttacking == false && isPlayerInReach(player) == true && canAttack() == true) {
                startAttack(player);
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

            // Close enough to swing: stand still facing the player instead of walking into them
            boolean inReach = isPlayerInReach(player);
            if (inReach == true && isAttacking == false) {
                if (getBodyCenterX() > getCenterX(player)) {
                    setAnimation("STAND_LEFT");
                }
                else {
                    setAnimation("STAND_RIGHT");
                }
            }

            // Make sure there is a valid path
            if (path != null && indexInPath < path.size() && inReach == false && isAttacking == false) {

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



    private void updatePath(NodeBase startNode, Point playerPos) {

        boolean useDiagonal = true;   // keep false in VentEntity
        AStar aStar = new AStar(nodes, useDiagonal);

        List<NodeBase> newPath = aStar.findPath(
            startNode,
            getGoalNode(playerPos)
        );

        if (!newPath.isEmpty()) {
            path = newPath;
            indexInPath = 0;
        }
    }

    // The tile to walk to: beside the player (left or right) so the sword can reach them,
    // preferring the side the entity is already on
    private NodeBase getGoalNode(Point playerPos) {
        int playerTileX = (int)playerPos.x;
        int playerTileY = (int)playerPos.y;

        int tileWidth = map.getTileset().getScaledSpriteWidth();
        float playerTileCenterX = playerTileX * tileWidth + tileWidth / 2.0f;
        int nearSide = (getBodyCenterX() < playerTileCenterX) ? -1 : 1;

        int[] sides = { nearSide, -nearSide };
        for (int side : sides) {
            int x = playerTileX + side;
            if (x >= 0 && x < nodes.length && nodes[x][playerTileY].getWalkable()) {
                return nodes[x][playerTileY];
            }
        }

        // No room on either side, so go straight for the player
        return nodes[playerTileX][playerTileY];
    }

    // True if the player is within sword range: level with the body and no further
    // away than the weapon sticks out (16 sprite pixels past the body, x3 scale)
    private boolean isPlayerInReach(Player player) {
        if (player.touching(this) == true) {
            return true;
        }

        Rectangle body = getBounds();
        Rectangle target = player.getBounds();

        // horizontal gap between the two boxes (0 or less means they overlap)
        float gapX = Math.max(target.getX1() - body.getX2(), body.getX1() - target.getX2());
        boolean levelWithBody = target.getY1() <= body.getY2() && target.getY2() >= body.getY1();

        return gapX <= SWORD_REACH && levelWithBody;
    }

    private float getBodyCenterX() {
        Rectangle body = getBounds();
        return body.getX1() + body.getWidth() / 2.0f;
    }

    private float getCenterX(Player player) {
        Rectangle target = player.getBounds();
        return target.getX1() + target.getWidth() / 2.0f;
    }

    private boolean canAttack() {
        return timer <= 0;
    }

    private void startAttack(Player player) {
        if (getBodyCenterX() > getCenterX(player)) {
            setAnimation("ATTACK_LEFT");
        }
        else {
            setAnimation("ATTACK_RIGHT");
        }
        timer = timeToAttack * 60;
        isAttacking = true;
        hasHitThisSwing = false;
        lastAttackFrameIndex = 0;
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
                            .withBounds(14, 5, 16, 27)
                            .build()
            });
            put("STAND_RIGHT", new Frame[] {
                   new FrameBuilder(spriteSheet.getSprite(0, 0))
                           .withScale(3)
                           .withBounds(14, 5, 16, 27)
                           .build()
           });

            put("WALK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(14, 5, 16, 27)
                            .build()
            });

            put("WALK_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withBounds(14, 5, 16, 27)
                            .build()
            });

            put("ATTACK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 7)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 7)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 5, 45, 26)
                            .build()
            });

            put("ATTACK_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 14)
                            .withScale(3)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 1), 14)
                            .withScale(3)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 7)
                            .withScale(3)
                            .withBounds(14, 5, 16, 27)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 7)
                            .withScale(3)
                            .withBounds(4, 5, 45, 26)
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
