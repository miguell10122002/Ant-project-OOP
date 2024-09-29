package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimulationData {
    int mevents;
    int eevents;
    List<Cycle> cycles;

    public SimulationData() {
        mevents = 0;
        eevents = 0;
        cycles = new ArrayList<>();
    }

    public void increaseMEvents() {
        mevents++;
    }

    public void increaseEEvents() {
        eevents++;
    }

    public void addCycle(Cycle cycle) {
        System.out.println("Adding this cycle to data: " + cycle);
        if (isCycleAlreadyPresent(cycle) == true){
            System.out.println("Cycle already present");
            return;
        }


        if (cycles.size() < 6) {
            cycles.add(new Cycle(cycle));
        } else {
            int highestCostIndex = 0;
            for (int i = 1; i < cycles.size(); i++) {
                if (cycles.get(i).getCost() > cycles.get(highestCostIndex).getCost()) {
                    highestCostIndex = i;
                }
            }
            if (cycle.getCost() < cycles.get(highestCostIndex).getCost()) {
                cycles.remove(highestCostIndex);
                cycles.add(new Cycle(cycle));
            }
        }
        orderCyclesByCost();
        System.out.println("Top 6 cycles are: ");
        for (Cycle c : cycles) {
            System.out.println(c);
        }
    }

    public int getMEvents() {
        return mevents;
    }

    public int getEEvents() {
        return eevents;
    }

    public Cycle getBest() {
        if (cycles.isEmpty()) {
            return null;
        }

        Cycle bestCycle = cycles.get(0);
        for (int i = 1; i < cycles.size(); i++) {
            Cycle currentCycle = cycles.get(i);
            if (currentCycle.getCost() < bestCycle.getCost()) {
                bestCycle = currentCycle;
            }
        }

        return bestCycle;
    }

    public List<Cycle> getTop5() {
        if (cycles.size() <= 1) {
            return new ArrayList<>();
        }

        List<Cycle> top5Cycles = new ArrayList<>(cycles);
        top5Cycles.remove(getBest());

        return top5Cycles;
    }

    public void orderCyclesByCost() {
        Collections.sort(cycles, Comparator.comparingDouble(Cycle::getCost));
    }

    public boolean isCycleAlreadyPresent(Cycle newCycle) {
        for (Cycle cycle : cycles) {
            if (cycle.equals(newCycle)) {
                return true;
            }
        }
        return false;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }
}
