package main;

import java.util.Random;

class PheromoneEvaporationEvent extends Event {
    Edge edge;

    public PheromoneEvaporationEvent(double time, Edge edge, InputTreater input) {
        super(time);
        this.setTime(time + getEvaporationTime(input));
        this.edge = edge;
    }

    public double getEvaporationTime(InputTreater input) {
        double eta = input.getEta();

        double meanTraversalTime = eta;
        double uniform = new Random().nextDouble(); // Generate a random number from a uniform distribution [0, 1)
        double lambda = 1 / meanTraversalTime;
        double exponential = -Math.log(1 - uniform) / lambda; // Transform to exponential distribution
        return exponential;
    }
}
/*
 * 
 * /*@Override
 * //public void processEvent() {
 * 
 * // Logic for pheromone evaporation event
 * 
 * System.out.println("Pheromone evaporation on edge " + edge + " at time " +
 * getTime());
 * }
 * }
 */
