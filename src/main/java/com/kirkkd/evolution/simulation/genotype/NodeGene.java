package com.kirkkd.evolution.simulation.genotype;

import com.kirkkd.evolution.simulation.activation.IActivationFunction;

public class NodeGene extends Gene {
    final int id;

    public int getId() {
        return id;
    }

    final NodeType type;

    public NodeType getType() {
        return type;
    }

    final IActivationFunction activation;

    public IActivationFunction getActivation() {
        return activation;
    }

    final double bias;

    public double getBias() {
        return bias;
    }

    public NodeGene(int id, NodeType type, IActivationFunction activation, double bias) {
        this.id = id;
        this.type = type;
        this.activation = activation;
        this.bias = bias;
    }
}
