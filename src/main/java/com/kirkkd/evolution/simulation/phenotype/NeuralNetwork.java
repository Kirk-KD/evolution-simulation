package com.kirkkd.evolution.simulation.phenotype;

import com.kirkkd.evolution.simulation.activation.IActivationFunction;
import com.kirkkd.evolution.simulation.genotype.ConnectionGene;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.NodeGene;
import com.kirkkd.evolution.simulation.genotype.NodeType;

import java.util.*;

public class NeuralNetwork {
    static class Node {
        int id;
        double value;
        IActivationFunction activation;
        double bias;
        List<Connection> inputs = new ArrayList<>();

        public Node(int id, IActivationFunction activation, double bias) {
            this.id = id;
            this.activation = activation;
            this.bias = bias;
        }

        public void compute() {
            double sum = bias;
            for (Connection conn : inputs) {
                sum += conn.weight * conn.from.value;
            }
            value = activation.activate(sum);
        }
    }

    static class Connection {
        Node from;
        double weight;

        public Connection(Node from, double weight) {
            this.from = from;
            this.weight = weight;
        }
    }

    Map<Integer, Node> nodeMap = new HashMap<>();
    List<Node> inputNodes = new ArrayList<>();
    List<Node> outputNodes = new ArrayList<>();
    List<Node> computeOrder = new ArrayList<>();

    public NeuralNetwork(Genome genome) {
        for (NodeGene gene : genome.getNodes()) {
            Node node = new Node(gene.getId(), gene.getActivation(), gene.getBias());
            nodeMap.put(gene.getId(), node);
            if (gene.getType() == NodeType.INPUT) inputNodes.add(node);
            else if (gene.getType() == NodeType.OUTPUT) outputNodes.add(node);
        }
        for (ConnectionGene cg : genome.getConnections()) {
            if (!cg.isEnabled()) continue;
            Node from = nodeMap.get(cg.getFrom());
            Node to = nodeMap.get(cg.getTo());
            to.inputs.add(new Connection(from, cg.getWeight()));
        }
        computeOrder = topologicalSort();
    }

    public double[] activate(double[] inputs) {
        if (inputs.length != inputNodes.size())
            throw new IllegalArgumentException("Input size mismatch");

        for (int i = 0; i < inputs.length; i++) {
            inputNodes.get(i).value = inputs[i];
        }

        for (Node node : computeOrder) {
            if (!inputNodes.contains(node)) {
                node.compute();
            }
        }

        double[] outputs = new double[outputNodes.size()];
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = outputNodes.get(i).value;
        }
        return outputs;
    }

    private List<Node> topologicalSort() {
        List<Node> sorted = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        for (Node node : nodeMap.values()) {
            dfsSort(node, visited, sorted);
        }
        return sorted;
    }

    private void dfsSort(Node node, Set<Node> visited, List<Node> sorted) {
        if (visited.contains(node)) return;
        visited.add(node);
        for (Connection conn : node.inputs) {
            dfsSort(conn.from, visited, sorted);
        }
        sorted.add(node);
    }
}
