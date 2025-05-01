package com.kirkkd.evolution.simulation.phenotype.cell;

public class OutputFields {
    public static final int SIZE = 3;
    double moveX = 0, moveY = 0, movePower = 0;

    void fromArray(double[] output) {
        if (output.length != SIZE) throw new IllegalArgumentException("length != size");

        moveX = output[0];
        moveY = output[1];
        movePower = output[2];
    }
}
