package main;

import java.util.List;
import java.util.ArrayList;

class Cycle {
    List<Node> path;
    int cost;

    public Cycle(List<Node> path, Graph graph) {
        this.path = new ArrayList<>(path);
        this.cost = calculateWeight(path, graph);
    }

    public Cycle(Cycle other) { // copy constructor
        this.path = new ArrayList<>(other.path);
        this.cost = other.cost;
    }

    public int calculateWeight(List<Node> path, Graph graph) {
        // the paht is storing nodes. As example 1,2,3,4,5. Use graph getEdgeWeight(Node
        // source, Node target) to get the edge weight and sum all the weights
        int weight = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            weight += graph.getEdgeWeight(path.get(i), path.get(i + 1));
        }
        // weight += graph.getEdgeWeight(path.get(path.size()-1), path.get(0));
        return weight;
    }

    public List<Node> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

    public int getTotalWeight() {
        return cost;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < path.size() - 1; i++) {
            sb.append(path.get(i).getId());
            if (i < path.size() - 2) {
                sb.append(",");
            }
        }
        sb.append("}:");
        sb.append(cost);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cycle other = (Cycle) obj;
        return path.equals(other.path) && cost == other.cost;
    }

    /*
     * public String toString() {
     * StringBuilder sb = new StringBuilder();
     * sb.append("{");
     * for (Node node : path) {
     * sb.append(node.getId());
     * sb.append(",");
     * }
     * //sb.deleteCharAt(sb.length() - 1); // eleminate the last comma
     * sb.append("}:");
     * sb.append(cost);
     * return sb.toString();
     * }
     */
}