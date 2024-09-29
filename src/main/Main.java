package main;

public class Main {
    public static void main(String[] args) {
        InputTreater inputTreater = new InputTreater();
        Graph graph = null; // Initialize graph with null value
        if (inputTreater.processInput(args)) {
            int n = inputTreater.getN();
            int n1 = inputTreater.getN1();
            int a = inputTreater.getA();
            int[][] weights = inputTreater.getWeights();

            if (a > 0) {
                // Create the graph instance using -r option
                graph = Graph.getGraphr(n, n1, a);
                System.out.println("Generated graph: " + graph);
                // Process the generated graph or perform any other actions based on your
                // requirements
            } else if (weights != null) {
                // Create the graph instance using -f option
                graph = new Graph(n, n1);

                // Generate and add nodes to the graph
                for (int i = 1; i <= n; i++) {
                    Node node = new Node(i);
                    graph.addNode(node);
                }

                // Add edges to the graph based on the weights
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int weight = weights[i][j];
                        if (weight != 0) {
                            Node source = graph.getNodeById(i + 1);
                            Node target = graph.getNodeById(j + 1);
                            Edge edge = new Edge(source, target, weight);// adicionar ao graph.java
                            graph.addEdge(edge);
                        }
                    }
                }

                // Process the generated graph or perform any other actions based on your
                // requirements
                System.out.println("Generated graph: " + graph);

            }
            // print adjancent nodes of all nodes:

            int numAnts = inputTreater.getNu();
            AntColony antColony = new AntColony(numAnts, graph, inputTreater);
            antColony.simulate(inputTreater.getTau());

        }

    }

}
