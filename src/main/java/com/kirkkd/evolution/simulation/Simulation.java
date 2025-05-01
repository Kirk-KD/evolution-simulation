package com.kirkkd.evolution.simulation;

import com.kirkkd.evolution.rendering.Camera;
import com.kirkkd.evolution.simulation.phenotype.cell.Cell;
import com.kirkkd.evolution.util.Vec2d;
import com.kirkkd.evolution.window.Keyboard;
import com.kirkkd.evolution.window.IUpdateAction;
import com.kirkkd.evolution.window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Simulation implements IUpdateAction {
    private static final double CAMERA_SPEED = 100f;

    private static Simulation instance;

    public static Simulation getInstance() {
        return instance;
    }

    public static void create() {
        instance = new Simulation();
    }

    private long lastTime = -1;
    private double deltaTime = 0;

    public double getDeltaTime() {
        return deltaTime;
    }

    protected Simulation() {
        Window window = Window.getInstance();
        window.UPDATE_ACTIONS.add(this);
        window.WORLD_SPACE_RENDER_ACTIONS.add(this::renderWorldSpace);
        window.SCREEN_SPACE_RENDER_ACTIONS.add(this::renderScreenSpace);

        // TEMPORARY TESTING CODE
        Cell.spawn(new Vec2d(200, 200));
        Cell.spawn(new Vec2d(700, 700));
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
        Vec2d direction = Vec2d.zero();

        if (Keyboard.isKeyDown(GLFW_KEY_W)) direction = direction.add(new Vec2d(0, -1));
        if (Keyboard.isKeyDown(GLFW_KEY_S)) direction = direction.add(new Vec2d(0, 1));
        if (Keyboard.isKeyDown(GLFW_KEY_A)) direction = direction.add(new Vec2d(-1, 0));
        if (Keyboard.isKeyDown(GLFW_KEY_D)) direction = direction.add(new Vec2d(1, 0));

        Camera.getInstance().setDeltaPosition(direction.normalize().multiply(CAMERA_SPEED * deltaTime));
    }

    private void updateDeltaTime() {
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - (lastTime == -1 ? currentTime : lastTime)) / 1000000000.0;
        lastTime = currentTime;
    }
}
