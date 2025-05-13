package com.kirkkd.evolution.simulation.genotype;

import com.kirkkd.evolution.simulation.activation.IActivationFunction;
import com.kirkkd.evolution.simulation.activation.Sigmoid;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Genome {
    List<NodeGene> nodes = new ArrayList<>();

    public List<NodeGene> getNodes() {
        return nodes;
    }

    List<ConnectionGene> connections = new ArrayList<>();

    public List<ConnectionGene> getConnections() {
        return connections;
    }

    Map<TraitGene.TraitType, TraitGene> traits = new HashMap<>();

    public void addTrait(TraitGene trait) {
        traits.put(trait.getType(), trait);
    }

    public TraitGene getTrait(TraitGene.TraitType traitType) {
        return traits.get(traitType);
    }

    public void addNode(NodeGene node) {
        nodes.add(node);
    }

    public void addConnection(ConnectionGene connection) {
        for (ConnectionGene cg : connections) {
            if (cg.from == connection.from && cg.to == connection.to) {
                return;
            }
        }
        if (!createsCycle(connection.from, connection.to)) {
            connections.add(connection);
        }
    }

    private boolean createsCycle(int fromId, int toId) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (ConnectionGene cg : connections) {
            if (cg.enabled) {
                graph.computeIfAbsent(cg.from, k -> new ArrayList<>()).add(cg.to);
            }
        }
        graph.computeIfAbsent(fromId, k -> new ArrayList<>()).add(toId);
        return hasCycle(graph);
    }

    private boolean hasCycle(Map<Integer, List<Integer>> graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> stack = new HashSet<>();

        for (Integer node : graph.keySet()) {
            if (dfsCycle(node, graph, visited, stack)) return true;
        }
        return false;
    }

    private boolean dfsCycle(int node, Map<Integer, List<Integer>> graph, Set<Integer> visited, Set<Integer> stack) {
        if (stack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        stack.add(node);
        for (int neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (dfsCycle(neighbor, graph, visited, stack)) return true;
        }
        stack.remove(node);
        return false;
    }

    public static Genome generateRandomGenome(int numInputs, int numHidden, int numOutputs, double traitEnableChance) {
        Genome genome = new Genome();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        IActivationFunction actFn = new Sigmoid();

        int id = 0;
        for (int i = 0; i < numInputs; i++) {
            genome.addNode(new NodeGene(id++, NodeType.INPUT, x -> x, 0.0));
        }

        for (int i = 0; i < numHidden; i++) {
            genome.addNode(new NodeGene(id++, NodeType.HIDDEN, actFn, rand.nextDouble() * 2 - 1));
        }

        for (int i = 0; i < numOutputs; i++) {
            genome.addNode(new NodeGene(id++, NodeType.OUTPUT, actFn, rand.nextDouble() * 2 - 1));
        }

        int innovation = 0;
        List<Integer> potentialFrom = new ArrayList<>();
        List<Integer> potentialTo = new ArrayList<>();

        for (NodeGene node : genome.nodes) {
            if (node.type != NodeType.OUTPUT) potentialFrom.add(node.id);
            if (node.type != NodeType.INPUT) potentialTo.add(node.id);
        }

        for (int i = 0; i < 2 * (numInputs + numHidden + numOutputs); i++) {
            int from = potentialFrom.get(rand.nextInt(potentialFrom.size()));
            int to = potentialTo.get(rand.nextInt(potentialTo.size()));
            if (from != to) {
                genome.addConnection(new ConnectionGene(from, to, rand.nextDouble() * 2 - 1, true, innovation++));
            }
        }

        for (TraitGene.TraitType traitType : TraitGene.TraitType.values()) {
            genome.addTrait(new TraitGene(traitType, traitType.randomValue(), rand.nextDouble() < traitEnableChance));
        }

        return genome;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Nodes (%s):\n", nodes.size()));
        for (NodeGene n : nodes) {
            sb.append(String.format("  ID %d | Type %s | Bias %.3f | Enabled %b\n", n.id, n.type, n.bias, n.enabled));
        }

        sb.append(String.format("Connections (%s):\n", connections.size()));
        connections.stream()
                .sorted(Comparator.comparingInt(c -> c.from))
                .forEach(c -> sb.append(String.format("  %d -> %d | Weight %.3f | Enabled %b | Innovation %d\n",
                        c.from, c.to, c.weight, c.enabled, c.innovation)));

        sb.append(String.format("Traits (%s):\n", traits.size()));
        traits.values().stream()
                .sorted(Comparator.comparing(TraitGene::isEnabled).reversed())
                .forEach(trait -> sb.append(String.format("  %s | Value %.3f | Enabled %b\n",
                        trait.getType(),
                        trait.getValue(),
                        trait.isEnabled())));

        return sb.toString();
    }
}
