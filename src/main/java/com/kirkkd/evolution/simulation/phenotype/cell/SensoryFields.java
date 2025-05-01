package com.kirkkd.evolution.simulation.phenotype.cell;

public class SensoryFields {
    public static final int SIZE = 1;
    double energy;

    double[] toArray() {
        return new double[] {
                energy
        };
    }
}
