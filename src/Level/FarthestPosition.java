package Level;

import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;


public class FarthestPosition {
    NodeBase[][] nodes;

    Queue<NodeBase> visitedTiles = new LinkedList<NodeBase>();

    public FarthestPosition(NodeBase[][] nodes) {
        this.nodes = nodes;
    }

    public ArrayList<NodeBase> findFarthest(NodeBase startNode, int maxDistance) {

        ArrayList<NodeBase> farthestNodes = new ArrayList<NodeBase>();
        int bestDistance = 0;
        resetNodes();
        visitedTiles.clear();

        startNode.setGCost(0);
        visitedTiles.add(startNode);

        while(visitedTiles.isEmpty() == false) {
            NodeBase currentNode = visitedTiles.poll();
            if(currentNode.getGCost() == maxDistance) {
                continue;
            }

            ArrayList<NodeBase> neighbors = getNeighbors(currentNode);
            for(NodeBase neighbor : neighbors) {
                if(neighbor.getWalkable() == false || neighbor.getGCost() != Integer.MAX_VALUE) {
                    continue;
                }

                neighbor.setGCost(currentNode.getGCost() +1);
                neighbor.setParent(currentNode);
                visitedTiles.add(neighbor);
                if(neighbor.getGCost() > bestDistance) {
                    farthestNodes.clear();
                    farthestNodes.add(neighbor);
                    bestDistance = neighbor.getGCost();
                }
                else if(neighbor.getGCost() == bestDistance) {
                    farthestNodes.add(neighbor);
                }
            }
            

        }

        return farthestNodes;
    }

    private void resetNodes() {

        for(int j = 0; j < nodes[0].length; j++) {
            for(int i = 0; i < nodes.length; i++) {
                nodes[i][j].setGCost(Integer.MAX_VALUE);
                nodes[i][j].setParent(null);
            }
        }

    }

    private ArrayList<NodeBase> getNeighbors(NodeBase node) {

        ArrayList<NodeBase> neighbors = new ArrayList<>();

        int x = node.getX();
        int y = node.getY();

        if(x > 0) {
            neighbors.add(nodes[x - 1][y]);
        }

        if(x < nodes.length - 1) {
            neighbors.add(nodes[x + 1][y]);
        }

        if(y > 0) {
            neighbors.add(nodes[x][y - 1]);
        }

        if(y < nodes[0].length - 1) {
            neighbors.add(nodes[x][y + 1]);
        }


        return neighbors;
    }
}