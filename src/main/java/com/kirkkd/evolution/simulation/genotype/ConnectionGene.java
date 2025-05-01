package com.kirkkd.evolution.simulation.genotype;

public class ConnectionGene extends Gene {
    int from;

    public int getFrom() {
        return from;
    }

    int to;

    public int getTo() {
        return to;
    }

    double weight;

    public double getWeight() {
        return weight;
    }

    int innovation;

    public ConnectionGene(int from, int to, double weight, boolean enabled, int innovation) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.enabled = enabled;
        this.innovation = innovation;
    }
}
