package com.kirkkd.evolution.simulation;

import com.kirkkd.evolution.rendering.Camera;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.phenotype.NeuralNetwork;
import com.kirkkd.evolution.util.Vec2f;
import com.kirkkd.evolution.window.Keyboard;
import com.kirkkd.evolution.window.IUpdateAction;
import com.kirkkd.evolution.window.Window;

import java.util.Arrays;

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
        Genome g = Genome.generateRandomGenome(1, 1, 2);
        System.out.println(g);
        NeuralNetwork n = new NeuralNetwork(g);
        System.out.println(Arrays.toString(n.activate(new double[]{4.2})));
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
