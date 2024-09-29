package main;

import java.util.List;

class ObservationEvent extends Event {
    int seqNumber;

    public ObservationEvent(double time, int seqNumber) {
        super(time);
        this.seqNumber = seqNumber;
        // setObservationEvents(finalInstant, 20);
    }

    public void printObservations(SimulationData data) {
        System.out.println("Observation " + seqNumber + " :");
        System.out.println("\t Present instant: " + this.time);
        System.out.println("\t Number of move events: " + data.getMEvents());
        System.out.println("\t Number of evaporation events: " + data.getEEvents());

        System.out.println("\t Top candidate cycles:");
        List<Cycle> topCycles = data.getTop5();
        if (topCycles.isEmpty()) {
            System.out.println("\t\t{}");
        } else {
            for (Cycle cycle : topCycles) {
                System.out.println("\t\t" + cycle);
            }
        }

        System.out.println("\t Best Hamiltonian cycle:");
        Cycle bestCycle = data.getBest();
        if (bestCycle == null) {
            System.out.println("\t\t{}");
        } else {
            System.out.println("\t\t" + bestCycle);
        }
    }
}
