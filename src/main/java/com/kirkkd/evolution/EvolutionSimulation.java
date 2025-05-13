package com.kirkkd.evolution;

import com.kirkkd.evolution.rendering.Camera;
import com.kirkkd.evolution.rendering.DisplayManager;
import com.kirkkd.evolution.simulation.Simulation;
import com.kirkkd.evolution.window.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class EvolutionSimulation {
    private Window window;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        DisplayManager.init();

        Window.create(1000, 1000, "Evolution Simulation");
        window = Window.getInstance();
        window.show();

        DisplayManager.createCapabilities();
        glEnable(GL_BLEND);
        glEnable(GL_MULTISAMPLE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Camera.create();
        Simulation.create();
    }

    private void loop() {
        window.loop();
    }

    private void cleanup() {
        window.destroy();
        DisplayManager.destroy();
    }

    public static void main(String[] args) {
        new EvolutionSimulation().run();
    }
}
