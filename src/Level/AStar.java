package Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStar {

    private NodeBase[][] nodes; // grid 

    public AStar(NodeBase[][] nodes) {
        this.nodes = nodes;
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

            for (NodeBase neighbor : getNeighbors(currentNode)) {

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


    private ArrayList<NodeBase> getNeighbors(NodeBase node) {

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

    // Up-left
    if (y - 1 >= 0 && x - 1 >= 0) {
        neighbors.add(nodes[x - 1][y - 1]);
    }

    // Up-right
    if (y - 1 >= 0 && x + 1 < nodes.length) {
        neighbors.add(nodes[x + 1][y - 1]);
    }

    // Down-left
    if (y + 1 < nodes[0].length && x - 1 >= 0) {
        neighbors.add(nodes[x - 1][y + 1]);
    }

    // Down-right
    if (y + 1 < nodes[0].length && x + 1 < nodes.length) {
        neighbors.add(nodes[x + 1][y + 1]);
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
