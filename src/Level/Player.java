package Level;

import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import EnhancedMapTiles.CarriableObject;
import GameObject.GameObject;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Lighting.Light;
import Lighting.PointLight;
import Lighting.SpotLight;
import Utils.Direction;

import java.util.ArrayList;

public abstract class Player extends GameObject {
    // values that affect player movement
    // these should be set in a subclass
    protected float walkSpeed = 0;
    protected int interactionRange = 1;
    protected Direction currentWalkingXDirection;
    protected Direction currentWalkingYDirection;
    protected Direction lastWalkingXDirection;
    protected Direction lastWalkingYDirection;

    // values used to handle player movement
    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;

    // values used to keep track of player's current state
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected Direction lastMovementDirection;

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key MOVE_UP_KEY = Key.UP;
    protected Key MOVE_DOWN_KEY = Key.DOWN;
    protected Key INTERACT_KEY = Key.SPACE;
    protected Key FLASHLIGHT_KEY = Key.F;

    // flashlight: a cone that follows the player and points where they last walked
    // the glow is a small circle of spill light so the player's own sprite isn't pitch black
    protected SpotLight flashlight = new SpotLight(0, 0, 288, 30);   // 6 tiles long, 60 degrees wide
    protected PointLight flashlightGlow = new PointLight(0, 0, 60);
    protected boolean flashlightOn = false;
    // how far in front of the player's center the beam starts (world px)
    protected float flashlightOffset = 12;

    protected boolean isLocked = false;

    // values for player pick up and drop

    protected CarriableObject carriedObject = null;

    // Player health
    protected int health;

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName, int health) {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        this.affectedByTriggers = true;
        this.health = health;

        flashlight.setSteps(24);
        flashlightGlow.setIntensity(0.6f);
        flashlightGlow.setSteps(24);;  
    }

    public void update() {
        if (!isLocked) {
            moveAmountX = 0;
            moveAmountY = 0;

            // if player is currently playing through level (has not won or lost)
            // update player's state and current actions, which includes things like determining how much it should move each frame and if its walking or jumping
            do {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);

            // move player with respect to map collisions based on how much player needs to move this frame
            lastAmountMovedY = super.moveYHandleCollision(moveAmountY);
            lastAmountMovedX = super.moveXHandleCollision(moveAmountX);

            handleFlashlightToggle();
        }

        handlePlayerAnimation();

        // after movement, so the light is where the player ended up this frame
        updateFlashlight();

        updateLockedKeys();

        // update player's animation
        super.update();
    }

    // based on player's current state, call appropriate player state handling method
    protected void handlePlayerState() {
        switch (playerState) {
            case STANDING:
                playerStanding();
                break;
            case WALKING:
                playerWalking();
                break;
        }
    }

    // player STANDING state logic
    protected void playerStanding() {
        if (!keyLocker.isKeyLocked(INTERACT_KEY) && Keyboard.isKeyDown(INTERACT_KEY)) {
            keyLocker.lockKey(INTERACT_KEY);
            interactOrDrop();
        }

        // if a walk key is pressed, player enters WALKING state
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(MOVE_UP_KEY) || Keyboard.isKeyDown(MOVE_DOWN_KEY)) {
            playerState = PlayerState.WALKING;
        }
    }

    // player WALKING state logic
    protected void playerWalking() {
        if (!keyLocker.isKeyLocked(INTERACT_KEY) && Keyboard.isKeyDown(INTERACT_KEY)) {
            keyLocker.lockKey(INTERACT_KEY);
            interactOrDrop();
        }

        if (Keyboard.isKeyDown(Key.SHIFT) == true) {
            walkSpeed = 5f;
        } else {
            walkSpeed = 2.3f;
        }

        // if walk left key is pressed, move player to the left
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY)) {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
            currentWalkingXDirection = Direction.LEFT;
            lastWalkingXDirection = Direction.LEFT;
        }

        // if walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
            moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
            currentWalkingXDirection = Direction.RIGHT;
            lastWalkingXDirection = Direction.RIGHT;
        }
        else {
            currentWalkingXDirection = Direction.NONE;
        }

        if (Keyboard.isKeyDown(MOVE_UP_KEY)) {
            moveAmountY -= walkSpeed;
            currentWalkingYDirection = Direction.UP;
            lastWalkingYDirection = Direction.UP;
        }
        else if (Keyboard.isKeyDown(MOVE_DOWN_KEY)) {
            moveAmountY += walkSpeed;
            currentWalkingYDirection = Direction.DOWN;
            lastWalkingYDirection = Direction.DOWN;
        }
        else {
            currentWalkingYDirection = Direction.NONE;
        }

        if ((currentWalkingXDirection == Direction.RIGHT || currentWalkingXDirection == Direction.LEFT) && currentWalkingYDirection == Direction.NONE) {
            lastWalkingYDirection = Direction.NONE;
        }

        if ((currentWalkingYDirection == Direction.UP || currentWalkingYDirection == Direction.DOWN) && currentWalkingXDirection == Direction.NONE) {
            lastWalkingXDirection = Direction.NONE;
        }

        if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY) && Keyboard.isKeyUp(MOVE_UP_KEY) && Keyboard.isKeyUp(MOVE_DOWN_KEY)) {
            playerState = PlayerState.STANDING;
        }
    }

       protected void updateLockedKeys() {
        if (Keyboard.isKeyUp(INTERACT_KEY) && !isLocked) {
            keyLocker.unlockKey(INTERACT_KEY);
        }

        if (Keyboard.isKeyUp(FLASHLIGHT_KEY)) {
            keyLocker.unlockKey(FLASHLIGHT_KEY);
        }
    }

    // pressing F flips the flashlight; the key lock makes one press = one flip, even though update runs 60 times a second
    protected void handleFlashlightToggle() {
        if (Keyboard.isKeyDown(FLASHLIGHT_KEY) && !keyLocker.isKeyLocked(FLASHLIGHT_KEY)) {
            flashlightOn = !flashlightOn;
            keyLocker.lockKey(FLASHLIGHT_KEY);
        }
    }

    // points the flashlight where the player last walked and moves it to the player
    protected void updateFlashlight() {
        // last walking directions give 8-way aim; they're null until the player first moves
        float aimX = lastWalkingXDirection != null ? lastWalkingXDirection.getVelocityX() : 0;
        float aimY = lastWalkingYDirection != null ? lastWalkingYDirection.getVelocityY() : 0;
        if (aimX == 0 && aimY == 0) {
            aimX = facingDirection.getVelocityX();
        }
        flashlight.setDirection(aimX, aimY);

        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;
        flashlightGlow.setPosition(centerX, centerY);
        // start the beam a little in front of the player, like it's held out in their hand
        flashlight.setPosition(centerX + flashlight.getDirX() * flashlightOffset, centerY + flashlight.getDirY() * flashlightOffset);
    }

    // lights the player is carrying this frame (the map adds these to its own lights when drawing)
    public ArrayList<Light> getLights() {
        ArrayList<Light> playerLights = new ArrayList<>();
        if (flashlightOn) {
            playerLights.add(flashlight);
            playerLights.add(flashlightGlow);
        }
        return playerLights;
    }

    public boolean isFlashlightOn() { return flashlightOn; }
    public void setFlashlightOn(boolean flashlightOn) { this.flashlightOn = flashlightOn; }
    public SpotLight getFlashlight() { return flashlight; }
    // anything extra the player should do based on interactions can be handled here
    protected void handlePlayerAnimation() {
        if (playerState == PlayerState.STANDING) {
            // sets animation to a STAND animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
        }
        else if (playerState == PlayerState.WALKING) {
            // sets animation to a WALK animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, GameObject entityCollidedWith) { }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, GameObject entityCollidedWith) { }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public Rectangle getInteractionRange() {
        return new Rectangle(
                getBounds().getX1() - interactionRange,
                getBounds().getY1() - interactionRange,
                getBounds().getWidth() + (interactionRange * 2),
                getBounds().getHeight() + (interactionRange * 2));
    }

    public Key getInteractKey() { return INTERACT_KEY; }
    public Direction getCurrentWalkingXDirection() { return currentWalkingXDirection; }
    public Direction getCurrentWalkingYDirection() { return currentWalkingYDirection; }
    public Direction getLastWalkingXDirection() { return lastWalkingXDirection; }
    public Direction getLastWalkingYDirection() { return lastWalkingYDirection; }

    
    public void lock() {
        isLocked = true;
        playerState = PlayerState.STANDING;
        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
    }

    public void unlock() {
        isLocked = false;
        playerState = PlayerState.STANDING;
        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
    }

    // used by other files or scripts to force player to stand
    public void stand(Direction direction) {
        playerState = PlayerState.STANDING;
        facingDirection = direction;
        if (direction == Direction.RIGHT) {
            this.currentAnimationName = "STAND_RIGHT";
        }
        else if (direction == Direction.LEFT) {
            this.currentAnimationName = "STAND_LEFT";
        }
    }

    // used by other files or scripts to force player to walk
    public void walk(Direction direction, float speed) {
        playerState = PlayerState.WALKING;
        facingDirection = direction;
        if (direction == Direction.RIGHT) {
            this.currentAnimationName = "WALK_RIGHT";
        }
        else if (direction == Direction.LEFT) {
            this.currentAnimationName = "WALK_LEFT";
        }
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
    }
    
    // A held item gets priority over nearby interaction scripts.
    private void interactOrDrop() {
        if (carriedObject != null) {
            dropCarriedObject();
        } else {
            map.entityInteract(this);
        }
    }

    public boolean dropCarriedObject() {
        if (carriedObject == null || isLocked) {
            return false;
        }

        Rectangle playerBounds = getBounds();
        Rectangle itemBounds = carriedObject.getBounds();
        // Position the item's collision bounds immediately beside the cat.
        float boundsX = facingDirection == Direction.LEFT
                ? playerBounds.getX1() - itemBounds.getWidth()
                : playerBounds.getX2() + 1;
        float boundsY = playerBounds.getY1()
                + (playerBounds.getHeight() - itemBounds.getHeight()) / 2f;
        float dropX = boundsX - (itemBounds.getX1() - carriedObject.getX());
        float dropY = boundsY - (itemBounds.getY1() - carriedObject.getY());

        if (!carriedObject.canDropAt(dropX, dropY)) {
            return false;
        }

        carriedObject.dropAt(dropX, dropY);
        carriedObject = null;
        return true;
    }

    //pickup objects interaction
    public boolean pickUpObject(CarriableObject object) {
    if (carriedObject != null || object == null) {
        return false;
    }

    if (!object.pickUp(this)) {
        return false;
    }

    carriedObject = object;
    return true;
}

    public void decreaseHealth(int amount) {
        this.health -= amount;
    }

    public void setHealth(int amount) {
        this.health = amount;
    }

    public int getHealth() {
        return this.health;
    }

    // Uncomment this to have game draw player's bounds to make it easier to visualize
    /*
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        drawBounds(graphicsHandler, new Color(255, 0, 0, 100));
    }
    */
}
