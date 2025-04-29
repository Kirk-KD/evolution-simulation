package com.kirkkd.evolution.simulation;

import com.kirkkd.evolution.rendering.Camera;
import com.kirkkd.evolution.simulation.genotype.*;
import com.kirkkd.evolution.simulation.genotype.mutation.CompositeMutationStrategy;
import com.kirkkd.evolution.simulation.genotype.mutation.GaussianPerturbationMutationStrategy;
import com.kirkkd.evolution.simulation.genotype.mutation.RandomResetMutationStrategy;
import com.kirkkd.evolution.simulation.organism.Cell;
import com.kirkkd.evolution.util.Vec2f;
import com.kirkkd.evolution.window.Keyboard;
import com.kirkkd.evolution.window.IUpdateAction;
import com.kirkkd.evolution.window.Window;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Simulation implements IUpdateAction {
    private static final float CAMERA_SPEED = 100f;

    private static Simulation instance;

    public static Simulation getInstance() {
        return instance;
    }

    public static void create() {
        instance = new Simulation();
    }

    private long lastTime;
    private float deltaTime;

    protected Simulation() {
        Window window = Window.getInstance();
        window.UPDATE_ACTIONS.add(this);
        window.WORLD_SPACE_RENDER_ACTIONS.add(this::renderWorldSpace);
        window.SCREEN_SPACE_RENDER_ACTIONS.add(this::renderScreenSpace);

        // TEMPORARY TESTING CODE
        for (int i = 0; i < 1; i++) {
            List<String> chromIds = List.of("Chr1", "Chr2", "Chr3", "Chr4");
            Genome genome = GenomeFactory.createRandomGenome(
                    chromIds,
                    Gene.locusSpecs()
            );
            Cell c = new Cell(genome);

            System.out.println("Original " + i + " " + c);

            for (int j = 0; j < 200; j++) {
                c = c.mitosis(new CompositeMutationStrategy(
                        new GaussianPerturbationMutationStrategy(10, 0.01),
                        new RandomResetMutationStrategy(0.001)
                ));
            }
        }
    }

    @Override
    public void update() {
        updateDeltaTime();
        updateCamera();
    }

    private void renderWorldSpace() {

    }

    private void renderScreenSpace() {

    }

    private void updateCamera() {
        Vec2f direction = Vec2f.zero();

        if (Keyboard.isKeyDown(GLFW_KEY_W)) direction.addInPlace(new Vec2f(0, -1));
        if (Keyboard.isKeyDown(GLFW_KEY_S)) direction.addInPlace(new Vec2f(0, 1));
        if (Keyboard.isKeyDown(GLFW_KEY_A)) direction.addInPlace(new Vec2f(-1, 0));
        if (Keyboard.isKeyDown(GLFW_KEY_D)) direction.addInPlace(new Vec2f(1, 0));

        direction.normalizeInPlace();
        direction.multiplyInPlace(CAMERA_SPEED * deltaTime);

        Camera.getInstance().setDeltaPosition(direction);
    }

    private void updateDeltaTime() {
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastTime) / 1000000000.0f;
        lastTime = currentTime;
    }
}
