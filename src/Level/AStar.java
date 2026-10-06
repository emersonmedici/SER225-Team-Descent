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

        ArrayList<NodeBase> openList = new ArrayList<>();
        ArrayList<NodeBase> closedList = new ArrayList<>();

        // Start the search at the starting node
        openList.add(startNode);

        // Continue searching while there are nodes to investigate
        while (!openList.isEmpty()) {

            // Find the node with the lowest F cost
            NodeBase currentNode = openList.get(0);

            for (NodeBase node : openList) {
                if (node.getFCost() < currentNode.getFCost() ||
                    (node.getFCost() == currentNode.getFCost()
                    && node.getHCost() < currentNode.getHCost())) {

                    currentNode = node;
                }
            }

            // We found the goal
            if (currentNode == goalNode) {
                return reconstructPath(startNode, goalNode);
            }

            // Move current node from open list to closed list
            openList.remove(currentNode);
            closedList.add(currentNode);

            // Check all neighboring nodes
            for (NodeBase neighbor : getNeighbors(currentNode)) {

                // Ignore walls and nodes we've already investigated
                if (!neighbor.getWalkable() || closedList.contains(neighbor)) {
                    continue;
                }

                // Distance from start to this neighbor
                int newGCost =
                        currentNode.getGCost() + getDistance(currentNode, neighbor);

                // If this is a better path to the neighbor
                if (newGCost < neighbor.getGCost() ||
                    !openList.contains(neighbor)) {

                    neighbor.setGCost(newGCost);
                    neighbor.setHCost(getDistance(neighbor, goalNode));
                    neighbor.setParent(currentNode);

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // No path exists
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
        if (y + 1 < nodes.length) {
            neighbors.add(nodes[x][y + 1]);
        }

        // Left
        if (x - 1 >= 0) {
            neighbors.add(nodes[x - 1][y]);
        }

        // Right
        if (x + 1 < nodes[0].length) {
            neighbors.add(nodes[x + 1][y]);
        }

        // up left
        if (y - 1 >= 0 && x - 1 >= 0) {
            neighbors.add(nodes[x - 1][y - 1]);
        }

        // up right
        if (y - 1 >= 0 && x + 1 < nodes[0].length) {
            neighbors.add(nodes[x + 1][y - 1]);
        }

        // down left
        if (y + 1 < nodes.length && x - 1 >= 0) {
            neighbors.add(nodes[x - 1][y + 1]);
        }

        // downright
        if (y + 1 < nodes.length && x + 1 < nodes[0].length) {
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
