package com.kirkkd.evolution.simulation.phenotype.cell;

import com.kirkkd.evolution.rendering.IRenderAction;
import com.kirkkd.evolution.rendering.PrimitiveShapes;
import com.kirkkd.evolution.rendering.TextTexture;
import com.kirkkd.evolution.simulation.Simulation;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.TraitGene;
import com.kirkkd.evolution.simulation.phenotype.neural.NeuralNetwork;
import com.kirkkd.evolution.util.Color;
import com.kirkkd.evolution.util.Vec2d;
import com.kirkkd.evolution.window.IUpdateAction;
import com.kirkkd.evolution.window.Window;

public class Cell implements IRenderAction, IUpdateAction {
    private final Genome genome;
    private final NeuralNetwork neuralNetwork;
    private final SensoryFields sensoryFields;
    private final OutputFields outputFields;

    private Vec2d position;
    private final EnergyManager energy;
    private boolean alive = true;

    private final TextTexture text = new TextTexture(100, 32);

    private Cell(Genome genome, Vec2d initialPosition) {
        this.position = initialPosition;

        this.genome = genome;
        this.neuralNetwork = new NeuralNetwork(genome);

        System.out.println(genome);

        this.sensoryFields = new SensoryFields();
        this.outputFields = new OutputFields();

        this.energy = new EnergyManager(this, 0.75);

        Window.getInstance().UPDATE_ACTIONS.add(this);
        Window.getInstance().WORLD_SPACE_RENDER_ACTIONS.add(this);
    }

    @Override
    public void update() {
        updateNeuralNet();

        energy.deduct();
        if (energy.value <= 0) {
            alive = false;
        }

        Vec2d deltaPosition = new Vec2d(outputFields.moveX - 0.5, outputFields.moveY - 0.5)
                .normalize()
                .multiply(10 * Simulation.getInstance().getDeltaTime());
        position = position.add(deltaPosition);

        text.updateText(String.format("E: %.1f", energy.value), new Color(255, 255, 255));
    }

    @Override
    public void render() {
        if (alive) {
            PrimitiveShapes.circle(
                    new PrimitiveShapes.Context(
                            position,
                            new Color(250, 250, 250, 255),
                            new Color(genome.getTrait(TraitGene.TraitType.COLOR_RGBA).getValue())
                    ), 50
            );
            text.render(position);
        }
    }

    private void updateNeuralNet() {
        sensoryFields.energy = energy.value;

        double[] input = sensoryFields.toArray();
        double[] output = neuralNetwork.activate(input);

        outputFields.fromArray(output);
    }

    public static Cell spawn(Vec2d position) {
        return new Cell(Genome.generateRandomGenome(SensoryFields.SIZE, 10, OutputFields.SIZE, 0.3), position);
    }

    private static class EnergyManager {
        private final OutputFields fields;
        private final Genome genome;
        double value;

        EnergyManager(Cell cell, double initialValue) {
            this.fields = cell.outputFields;
            this.genome = cell.genome;
            this.value = initialValue;
        }

        void deduct() {
            double dt = Simulation.getInstance().getDeltaTime();

            // Movement
            value -= dt
                    * fields.movePower
                    * genome.getTrait(TraitGene.TraitType.MOVEMENT_COST_PER_POWER_PER_SEC).getValue()
                    * genome.getTrait(TraitGene.TraitType.MOVEMENT_POWER_MULTIPLIER).getValue();

            // Maintenance
            value -= dt
                    * genome.getTrait(TraitGene.TraitType.METABOLIC_COST_PER_SEC).getValue();
        }
    }
}
