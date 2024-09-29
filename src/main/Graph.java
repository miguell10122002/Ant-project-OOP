package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;
    private int totalWeight;
    private Map<Node, List<Node>> adjacencyMap;

    private int n1;

    public Graph(int n, int n1, int a) {

        this.n1 = n1;

        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyMap = new HashMap<>();

    }

    public Graph(int n, int n1) {

        this.n1 = n1;
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyMap = new HashMap<>();
    }

    public Graph() {

    }

    public Node getNest() {
        Node node = new Node(n1);
        return node;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);

        Node source = edge.getSource();
        Node target = edge.getTarget();

        // Update adjacency map for source node
        List<Node> sourceAdjacentNodes = adjacencyMap.getOrDefault(source, new ArrayList<>());
        sourceAdjacentNodes.add(target);
        adjacencyMap.put(source, sourceAdjacentNodes);

    }

    public boolean hasEdge(Node source, Node target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public List<Node> getAdjacentNodes(Graph graph, Node node) {

        List<Node> adjacentNodes = new ArrayList<>();
        // System.out.println(node);
        adjacentNodes = graph.adjacencyMap.get(node);

        return adjacentNodes;
    }

    public Node getNodeById(int id) {
        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null; // Return null if the node with the given ID is not found
    }

    public static Graph getGraphr(int n, int n1, int a) {

        Graph graph = new Graph(n, n1, a);

        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Node node = new Node(i);

            graph.addNode(node);
            nodes.add(node);
        }

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            Node source = nodes.get(i);
            Node target = nodes.get((i + 1) % n);
            int weight = random.nextInt(a) + 1;
            Edge edge = new Edge(source, target, weight);
            graph.addEdge(edge);
        }

        int numExtraEdges = random.nextInt(n * (n - 1) / 2 - n + 1);
        while (numExtraEdges > 0) {
            Node source = nodes.get(random.nextInt(n));
            Node target = nodes.get(random.nextInt(n));
            if (source != target && !graph.hasEdge(source, target)) {
                int weight = random.nextInt(a) + 1;
                Edge edge = new Edge(source, target, weight);
                graph.addEdge(edge);
                numExtraEdges--;
            }
        }

        return graph;
    }

    public List<Node> getNonVisitedAdjacentNodes(Node node, List<Node> path) {
        List<Node> nonVisitedNodes = new ArrayList<>(adjacencyMap.getOrDefault(node, new ArrayList<>()));

        for (int i = 0; i < path.size(); i++) {
            nonVisitedNodes.remove(path.get(i));
        }

        return nonVisitedNodes;

    }

    public Edge getEdge(Node source, Node target) {
        Edge result = null;
        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                result = edge;
            }
        }
        return result;
    }

    public int getEdgeWeight(Node source, Node target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                return edge.weight;
            }
        }
        return 0; // Return 0 or any other appropriate default value if the edge is not found
    }

    public int getTotalWeight() {
        if(totalWeight ==0)
        {
            for (Edge edge : edges) {
                totalWeight += edge.weight;
            }
            totalWeight = totalWeight/2;
        }
        return this.totalWeight;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Nodes: ").append(nodes).append("\n");
        sb.append("Edges: ").append("\n");
        for (Edge edge : edges) {
            sb.append(edge).append("\n");
        }
        return sb.toString();
    }
}
