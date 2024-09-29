package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    private int id;

    private List<Node> adjacentNodes;

    public Node(int id) {
        this.id = id;
        this.adjacentNodes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void addAdjacentNode(Node node) {
        adjacentNodes.add(node);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node otherNode = (Node) obj;
        return id == otherNode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Node ID: " + id;
    }
}
