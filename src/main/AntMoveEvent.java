package main;

import java.util.List;
import java.util.Random;

class AntMoveEvent extends Event {

    Ant ant;
    private Graph graph;

    public AntMoveEvent(Double time, Ant ant, Graph graph) {
        super(time);
        this.ant = ant;
        this.graph = graph;
    }

    public double getTravelTime(Node current, Double delta) {

        double edgeWeight = graph.getEdgeWeight(current, ant.destination);

        double meanTraversalTime = delta * edgeWeight;
        double uniform = new Random().nextDouble(); // Generate a random number from a uniform distribution [0, 1)
        double lambda = 1 / meanTraversalTime;
        double exponential = -Math.log(1 - uniform) / lambda; // Transform to exponential distribution
        return exponential;
    }

    public double buildEvent(double currentTime, InputTreater parameters, PEC eventQueue) {
        // System.out.println("Vou buscar o current node aqui: " + ant.path);
        // System.out.println("O hamiltonian path é111: " + ant.hamiltonian.path);[Node
        // ID: 1]
        Node currentNode = ant.getCurrentNode();
        List<Node> nonVisitedNodes = graph.getNonVisitedAdjacentNodes(currentNode, ant.getPath());
        double delta = parameters.getDelta();
        // If has still neighbor nodes nonvisited
        if (ant.hamiltonian != null) {
            if (!ant.hamiltonian.path.isEmpty()) { // If has a Hamiltonian Cycle -> repeat the path
                //System.out.println("O hamiltonian path é: " + ant.hamiltonian.path);
                Node nextNode = ant.hamiltonian.path.get(0);

                if (ant.hamiltonian.path.get(0).getId() != graph.getNest().getId()) {
                    ant.setDestination(nextNode);
                    ant.hamiltonian.path.remove(0);
                    // System.out.println("Current path is " + ant.path);
                    Edge edge = graph.getEdge(ant.getCurrentNode(), nextNode);
                    Double scheduledTime = currentTime + getTravelTime(currentNode, delta);
                    // dar schedule do PEE se PL == 0
                    if (edge.getPheromoneLevel() == 0) {
                        PheromoneEvaporationEvent evaporationEvent = new PheromoneEvaporationEvent(currentTime, edge,
                                parameters);
                        if (evaporationEvent.getTime() < parameters.getTau()) {
                            eventQueue.scheduleEvent(evaporationEvent);
                        }
                    }
                    // cagar a feromona aqui
                    edge.setPheromoneLevel(parameters, ant.hamiltonian.cost, graph.getTotalWeight());
                    ant.path.remove(0);
                    return scheduledTime;
                }
                Double scheduledTime = currentTime + getTravelTime(currentNode, delta);
                ant.setDestination(nextNode);
                ant.path.remove(0);
                ant.hamiltonian.path.remove(0);
                return scheduledTime;
            }

        }
        if (!nonVisitedNodes.isEmpty()) {
            // Calculate probabilities for choosing the next node
            double[] probabilities = calculateProbabilities(currentNode, nonVisitedNodes);

            // Select the next node based on the probabilities
            Node nextNode = selectNextNode(nonVisitedNodes, probabilities);

            // Set the next destination of the ant
            ant.setDestination(nextNode);

            // Calculate the time event is going to be scheduled

            double scheduledTime = currentTime + getTravelTime(currentNode, delta);

            // Update the pheromone level if a Hamiltonian cycle is completed
            /*
             * if (ant.getPath().size() == edges.size() && nextNode == ant.getNest()) {
             * updatePheromoneLevel();
             * }
             */
            return scheduledTime;
        }
        // If no more nonvisited neighbours. -> Check if the cycle has finished.
        // -> If yes, start the way back and drop feromones
        // -> If not, select a random (normal distribution) neighbour node to go.
        else {
            // Randomly select any adjacent node with a uniform distribution
            // System.out.println("Current node is " + currentNode);
            List<Node> adjacentNodes = graph.getAdjacentNodes(graph, currentNode);
            // List<Node> adjacentNodes = graph.getAdjacentNodes(graph, currentNode);
            // System.out.println("Adjacent Nodes are " + adjacentNodes);
            Node nextNode = adjacentNodes.get(new Random().nextInt(adjacentNodes.size()));

            // Update the ant destination
            ant.setDestination(nextNode);

            // removeCycleFromPath(ant);

            double scheduledTime = currentTime + getTravelTime(currentNode, delta);

            return scheduledTime;
        }

    }

    public void processEvent(SimulationData data) {
        ant.addToPath(ant.destination);

        if (ant.hasHamiltonianCycle(graph)) {
            // Fazer os procedimentos do Hamilotnian Cycle
            ant.hamiltonian = new Cycle(ant.path, graph);
            data.addCycle(ant.hamiltonian);
            System.out.println("\n!\n!\n!\n!\nHamiltonian Cycle found:" + ant.hamiltonian);
            ant.hamiltonian.path.remove(0);

        }
        if (ant.getPath().size() > 2) {
            int lastIndex = ant.getPath().size() - 1;
            List<Node> previousPath = ant.getPath().subList(0, lastIndex);
            Node lastNode = previousPath.get((previousPath.size() - 1));
            if (graph.getNonVisitedAdjacentNodes(lastNode, previousPath).isEmpty()) {
                removeCycleFromPath(ant);
                // System.out.println("Removi o ciclo e fico com: " + ant.path);
            }

        }

    }

    // Get the list of non-visited adjacent nodes for the given node

    // Calculate the probabilities for choosing the next node based on the pheromone
    // level and edge weight
    private double[] calculateProbabilities(Node current, List<Node> nonVisitedNodes) {
        double total = 0.0;
        double[] probabilities = new double[nonVisitedNodes.size()];
        double alpha = 30.0;
        double beta = 40.0;
        for (int i = 0; i < nonVisitedNodes.size(); i++) {

            Node nextNode = nonVisitedNodes.get(i);
            double pheromoneLevel = 0;// getEdgePheromoneLevel(current, nextNode);
            double edgeWeight = graph.getEdgeWeight(current, nextNode);

            double numerator = (pheromoneLevel + alpha) / (beta + edgeWeight);
            double denominator = 0.0;

            // Calculate the denominator of the probability equation
            for (int j = 0; j < nonVisitedNodes.size(); j++) {
                Node otherNode = nonVisitedNodes.get(j);
                double otherPheromoneLevel = 0;// getEdgePheromoneLevel(current, otherNode);
                double otherEdgeWeight = graph.getEdgeWeight(current, otherNode);

                denominator += (otherPheromoneLevel + alpha) / (beta + otherEdgeWeight);
            }

            probabilities[i] = numerator / denominator;
            total += probabilities[i];
        }

        // Normalize the probabilities
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= total;
        }

        return probabilities;
    }

    // Select the next node based on the probabilities
    private Node selectNextNode(List<Node> nonVisitedNodes, double[] probabilities) {
        double randomValue = new Random().nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];

            if (randomValue <= cumulativeProbability) {
                return nonVisitedNodes.get(i);
            }
        }

        // Fallback option: select the last node
        return nonVisitedNodes.get(nonVisitedNodes.size() - 1);
    }

    // Update the pheromone level on the chosen edge
    /*
     * private void updatePheromoneLevel() {
     * List<Integer> path = ant.getPath();
     * double cycleWeight = getWeightOfCycle(path);
     * 
     * for (int i = 0; i < path.size() - 1; i++) {
     * // antes de fazer o update do pheromone level do edge verificar se este
     * // é igual a 0 devemos calendarizar um evaporation event para o edge e então
     * // depois dar update do nivel de feromonas
     * int node1 = path.get(i);
     * int node2 = path.get(i + 1);
     * Edge edge = findEdge(node1, node2);
     * 
     * if (edge != null) {
     * double pheromoneIncrement = (gamma * edge.getWeight()) / cycleWeight;
     * edge.setPheromoneLevel(edge.getPheromoneLevel() + pheromoneIncrement);
     * }
     * }
     * }
     */

    // Find the edge between the given nodes
    /*
     * private Edge findEdge(int node1, int node2) {
     * for (Edge edge : edges) {
     * if ((edge.getNode1() == node1 && edge.getNode2() == node2)
     * || (edge.getNode1() == node2 && edge.getNode2() == node1)) {
     * return edge;
     * }
     * }
     * return null;
     * }
     */

    // Get the pheromone level of the edge between the given nodes
    /*
     * private double getEdgePheromoneLevel(int node1, int node2) {
     * Edge edge = findEdge(node1, node2);
     * return (edge != null) ? edge.getPheromoneLevel() : 0.0;
     * }
     * 
     * // Get the weight of the edge between the given nodes
     * private double getEdgeWeight(int node1, int node2) {
     * Edge edge = findEdge(node1, node2);
     * return (edge != null) ? edge.getWeight() : Double.MAX_VALUE;
     * }
     * 
     * // Get the list of adjacent nodes for the given node
     * private List<Node> getAdjacentNodes(Node node) {
     * // Implement logic to retrieve the list of adjacent nodes
     * // ...
     * return node;
     * }
     */
    // Remove the cycle created in the last move from the path
    private void removeCycleFromPath(Ant ant) {
        // System.out.println("Ant: " + ant);
        Node lastNode = ant.getPath().get(ant.getPath().size() - 1);

        ant.path.remove(ant.getPath().size() - 1);
        for (int i = (ant.path.size() - 1); i > 0; i--) {
            if (ant.path.get(i) == lastNode) {
                break;
            }
            ant.path.remove(i);
        }
    }

    /*
     * // Get the weight of the cycle formed by the given path
     * private double getWeightOfCycle(List<Integer> path) {
     * double weight = 0.0;
     * 
     * for (int i = 0; i < path.size() - 1; i++) {
     * int node1 = path.get(i);
     * int node2 = path.get(i + 1);
     * weight += getEdgeWeight(node1, node2);
     * }
     * 
     * return weight;
     * }
     */
}