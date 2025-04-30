package com.kirkkd.evolution.simulation.activation;

public class Sigmoid implements IActivationFunction {
    public double activate(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
}
