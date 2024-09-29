package main;

import java.util.ArrayList;
import java.util.List;

public class Ant {

    int antId;
    List<Node> path;
    Node destination;
    Cycle hamiltonian;

    public Ant(int antId) {
        this.antId = antId;
        this.path = new ArrayList<>();
    }

    public void setDestination(Node newDestination) {
        destination = newDestination;
    }

    public int getAntId() {
        return antId;
    }

    public List<Node> getPath() {
        return path;
    }

    public Node getCurrentNode() {
        return path.get(path.size() - 1);
    }

    public List<Node> clearPath() {
        return path.subList(0, 1);
    }

    public void addToPath(Node node) {
        path.add(node);
    }

    // Method to check if a Hamiltonian cycle is found
    public boolean hasHamiltonianCycle(Graph graph) {
        // Check if the path forms a cycle that visits all nodes exactly once

        return (path.size() > 2 && path.get(0).getId() == path.get(path.size() - 1).getId()
                && (path.size() - 1) == graph.getNodes().size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ant ID: ").append(antId).append("\n");
        sb.append("Destination: ").append(destination).append("\n");

        return sb.toString();
    }
}