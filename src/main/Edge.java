package main;

public class Edge extends Graph {
    Node source;
    Node target;
    int weight;
    double pheromoneLevel;

    public Edge(Node source, Node target, int weight) {

        this.source = source;
        this.target = target;
        this.weight = weight;
        this.pheromoneLevel = 0;

    }

    public double getPheromoneLevel() {
        return pheromoneLevel;
    }

    public Node getSource() {
        return source;
    }

    public int getWeight() {
        return weight;
    }

    public void setPheromoneLevel(InputTreater inputTreater, int cycleWeight, int GraphWeight) {
        double gamma = inputTreater.getGamma();
        double pheromoneLevel = (gamma * GraphWeight) / cycleWeight;
        this.pheromoneLevel += pheromoneLevel;
        System.out.println("Added " + String.format("%.2f", this.pheromoneLevel) + " pheromone on edge "
                + this.source.getId() + " -> "
                + this.target.getId());
        System.out.println("Current level " + String.format("%.2f", this.pheromoneLevel));
    }

    public void evaporatePheromones(InputTreater inputTreater) {
        double rho = inputTreater.getRho();
        this.pheromoneLevel -= rho;
        if (this.pheromoneLevel < 0) {
            this.pheromoneLevel = 0;
        }
        System.out.println("Evaporated " + rho + " pheromone on edge " + this.source.getId() + " -> "
                + this.target.getId());
        System.out.println("Current level " + String.format("%.2f", this.pheromoneLevel));
    }

    public Node getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "Edge: " + source.getId() + " -> " + target.getId() + ", Weight: " + weight;
    }

}
