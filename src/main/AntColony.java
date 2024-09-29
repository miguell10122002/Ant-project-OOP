package main;

import java.util.ArrayList;
import java.util.List;

public class AntColony {
    private int numAnts;
    private List<Ant> ants;
    private Graph graph;
    private InputTreater parameters;
    private SimulationData simulationData;

    public AntColony(int numAnts, Graph graph, InputTreater inputTreater) {
        this.numAnts = numAnts;
        this.ants = new ArrayList<>();
        this.graph = graph;
        this.parameters = inputTreater;
        initialize();
    }

    public void initialize() {
        // Create ants and add them to the colony
        //numAnts = 1; /////FORCE
        for (int i = 0; i < numAnts; i++) {
            Ant ant = new Ant(i);
            ant.addToPath(graph.getNest());
            ant.antId = i + 1;
            ants.add(ant);
        }
    }

    public void simulate(Double time) {
        PEC eventQueue = new PEC();
        double clockTime = 0;
        double scheduledTime = 0;
        int n = 0;
        // setup Ants and define first Series of Events

        for (Ant ant : ants) {
            AntMoveEvent antMoveEvent = new AntMoveEvent(clockTime, ant, graph);

            scheduledTime = antMoveEvent.buildEvent(clockTime, parameters, eventQueue);// creates new move Event
            eventQueue.scheduleEvent(new AntMoveEvent(scheduledTime, ant, graph));

           // System.out.println("The Time is " + clockTime + " will move to " + ant.destination);
        }

        // setup Observation events
        for (int i = 1; i <= 20; i++) {
            double eventTime = i * parameters.getTau() / 20;
            ObservationEvent newObs = new ObservationEvent(eventTime, i);
            System.out.println("Observation No." + newObs.seqNumber + " created for t = " + newObs.getTime());
            eventQueue.scheduleEvent(newObs);
        }
        // setup ObservationEvents
        simulationData = new SimulationData();

        do {
            if (!eventQueue.isEmpty()) {
                Event event = eventQueue.getNextEvent(); // pulls next event from queue
                clockTime = event.getTime(); // updates clock time

                if (event instanceof AntMoveEvent) {
                    AntMoveEvent antMoveEvent = (AntMoveEvent) event;
                    System.out.printf("\n--> [%.2f] %s <--\n", clockTime, "\"Ant Move Event\"");
                    simulationData.increaseMEvents();
                    // antMoveEvent.ant.hasHamiltonianCycle();
                    // Update the current clock time
                    antMoveEvent.processEvent(simulationData);

                    // se tivermos encontrado um hamiltonian cycle dar ahndle disto
                    if (antMoveEvent.ant.hamiltonian != null) {

                        scheduledTime = antMoveEvent.buildEvent(clockTime, parameters, eventQueue);// creates new move
                                                                                                   // Event
                        // antMoveEvent.ant.hamiltonian.path.remove(0);
                        if (scheduledTime < time) {
                            eventQueue.scheduleEvent(new AntMoveEvent(scheduledTime, antMoveEvent.ant, graph));
                        }

                    } else {
                        // Aqui vou ter que remover o ciclo que se acaab de criar se for esse o caso

                        // has arrived to destination, so add to
                        //System.out.printf("Path of Ant %d:", antMoveEvent.ant.antId);
                        //System.out.println(antMoveEvent.ant.path);

                        // build event
                        scheduledTime = antMoveEvent.buildEvent(clockTime, parameters, eventQueue);// creates new move
                                                                                                   // Event
                        if (scheduledTime < time) {
                            eventQueue.scheduleEvent(new AntMoveEvent(scheduledTime, antMoveEvent.ant, graph));
                        }
                        // schedule new ant move event if its less than the sim time
                        // Process the event and schedule a new ant move evnet
                        //System.out.println("The Ant " + antMoveEvent.ant.antId + " arrived "
                        //        + antMoveEvent.ant.getCurrentNode());
                        //System.out.println("The Ant " + antMoveEvent.ant.antId + " is going to "
                        //        + antMoveEvent.ant.destination);
                    }
                }

                else if (event instanceof PheromoneEvaporationEvent) {
                    PheromoneEvaporationEvent evaporationEvent = (PheromoneEvaporationEvent) event;
                    System.out.printf("\n--> [%.2f] %s <--\n", clockTime, "\"Evaporation Event\"");
                    //System.out.println("Pheromone evaporation on edge " + evaporationEvent.edge.source.getId() + " -> "
                    //        + evaporationEvent.edge.target.getId());
                    simulationData.increaseEEvents();

                    evaporationEvent.edge.evaporatePheromones(parameters);
                    if (evaporationEvent.edge.getPheromoneLevel() > 0) { // Arranjar forma de não marcar o evento na PEC
                                                                         // se exceder o tempo de simulação
                        PheromoneEvaporationEvent event1 = new PheromoneEvaporationEvent(clockTime,
                                evaporationEvent.edge, parameters);
                        if (event1.time < time) {
                            eventQueue.scheduleEvent(event1);
                        }

                    }
                }

                else if (event instanceof ObservationEvent) {
                    ObservationEvent obsEvent = (ObservationEvent) event;
                    System.out.printf("\n--> [%.2f] %s <--\n", clockTime, "\"Observation Event\"");
                    obsEvent.printObservations(simulationData);

                }
            }

            // Run the simulation
            n++;
        } while (!eventQueue.isEmpty()); // Run the simulation
    }
    // Other methods specific to the ant colony...
}