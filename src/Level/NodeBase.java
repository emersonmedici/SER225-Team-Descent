package Level;

public class NodeBase {
    private int x;
    private int y;

    private boolean walkable;

    private int gCost;
    private int hCost;

    private NodeBase parent;

    public NodeBase(int x, int y, boolean walkable) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
    }

    public void setX(int amount) {
        this.x = amount;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int amount) {
        this.y = amount;
    }

    public int getY() {
        return this.y;
    }


    public void setWalkable(boolean shouldWalk) {
        this.walkable = shouldWalk;
    }

    public boolean getWalkable() {
        return this.walkable;
    }

    public void setGCost(int amount) {
        this.gCost = amount;
    }

    public int getGCost() {
        return this.gCost;
    }

    public void setHCost(int amount) {
        this.hCost = amount;
    }

    public int getHCost() {
        return this.hCost;
    }

    public int getFCost() {
        return this.hCost + this.gCost;
    }

    public void setParent(NodeBase parent) {
        this.parent = parent;
    }

    public NodeBase getParent() {
        return this.parent;
    }
}
