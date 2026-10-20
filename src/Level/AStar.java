package Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {

    private NodeBase[][] nodes; // grid 
    private boolean needDiagonal;

    public AStar(NodeBase[][] nodes, boolean needDiagonal) {
        this.nodes = nodes;
        this.needDiagonal = needDiagonal;
    }

    public List<NodeBase> findPath(NodeBase startNode, NodeBase goalNode) {

        // Reset all node pathfinding data
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[0].length; y++) {
                nodes[x][y].setGCost(Integer.MAX_VALUE);
                nodes[x][y].setHCost(0);
                nodes[x][y].setParent(null);
            }
        }

        // Start node must have a G cost of 0
        startNode.setGCost(0);

        ArrayList<NodeBase> openList = new ArrayList<>();
        ArrayList<NodeBase> closedList = new ArrayList<>();

        openList.add(startNode);

        while (!openList.isEmpty()) {
            NodeBase currentNode = openList.get(0);

            for (NodeBase node : openList) {
                if (node.getFCost() < currentNode.getFCost() ||
                    (node.getFCost() == currentNode.getFCost()
                    && node.getHCost() < currentNode.getHCost())) {

                    currentNode = node;
                }
            }

            if (currentNode == goalNode) {
                return reconstructPath(startNode, goalNode);
            }

            openList.remove(currentNode);
            closedList.add(currentNode);

            for (NodeBase neighbor : getNeighbors(currentNode, needDiagonal)) {

                if (!neighbor.getWalkable() || closedList.contains(neighbor)) {
                    continue;
                }

                int newGCost =
                        currentNode.getGCost()
                        + getDistance(currentNode, neighbor);

                if (newGCost < neighbor.getGCost()
                        || !openList.contains(neighbor)) {

                    neighbor.setGCost(newGCost);
                    neighbor.setHCost(
                        getDistance(neighbor, goalNode)
                    );
                    neighbor.setParent(currentNode);

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }


    private List<NodeBase> reconstructPath(
            NodeBase startNode,
            NodeBase goalNode) {

        ArrayList<NodeBase> path = new ArrayList<>();

        NodeBase currentNode = goalNode;

        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        path.add(startNode);

        // Currently Goal -> Start
        // Reverse it to Start -> Goal
        Collections.reverse(path);

        return path;
    }


        private ArrayList<NodeBase> getNeighbors(NodeBase node, boolean needDiagonal) {

        ArrayList<NodeBase> neighbors = new ArrayList<>();

        int x = node.getX();
        int y = node.getY();

        // Up
        if (y - 1 >= 0) {
            neighbors.add(nodes[x][y - 1]);
        }

        // Down
        if (y + 1 < nodes[0].length) {
            neighbors.add(nodes[x][y + 1]);
        }

        // Left
        if (x - 1 >= 0) {
            neighbors.add(nodes[x - 1][y]);
        }

        // Right
        if (x + 1 < nodes.length) {
            neighbors.add(nodes[x + 1][y]);
        }

        if (needDiagonal == true) {
            // Only allow a diagonal move if both tiles beside it are walkable,
            // so the entity can't squeeze between two walls at a corner
            boolean upWalkable    = y - 1 >= 0 && nodes[x][y - 1].getWalkable();
            boolean downWalkable  = y + 1 < nodes[0].length && nodes[x][y + 1].getWalkable();
            boolean leftWalkable  = x - 1 >= 0 && nodes[x - 1][y].getWalkable();
            boolean rightWalkable = x + 1 < nodes.length && nodes[x + 1][y].getWalkable();

            // Up-left
            if (upWalkable && leftWalkable) {
                neighbors.add(nodes[x - 1][y - 1]);
            }

            // Up-right
            if (upWalkable && rightWalkable) {
                neighbors.add(nodes[x + 1][y - 1]);
            }

            // Down-left
            if (downWalkable && leftWalkable) {
                neighbors.add(nodes[x - 1][y + 1]);
            }

            // Down-right
            if (downWalkable && rightWalkable) {
                neighbors.add(nodes[x + 1][y + 1]);
            }
        }

        return neighbors;
    }


    private int getDistance(NodeBase nodeA, NodeBase nodeB) {

        int distanceX = Math.abs(nodeA.getX() - nodeB.getX());
        int distanceY = Math.abs(nodeA.getY() - nodeB.getY());

        // Since we're only allowing horizontal and vertical movement,
        // Manhattan distance is appropriate.
        return distanceX + distanceY;
    }
}
