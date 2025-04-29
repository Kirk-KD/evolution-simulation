package com.kirkkd.evolution.window;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import com.kirkkd.evolution.rendering.Camera;
import com.kirkkd.evolution.rendering.IRenderAction;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private static Window instance;

    public static Window getInstance() {
        return instance;
    }

    public static void create(int width, int height, String title) {
        long id = glfwCreateWindow(width, height, title, 0, 0);
        if (id == 0) throw new RuntimeException("Failed to create GLFW com.kirkkd.evolution.window");

        glfwSetKeyCallback(id, Keyboard::callback);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(id, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode != null) glfwSetWindowPos(
                    id,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(id);

        glfwSwapInterval(1);

        instance = new Window(id, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getId() {
        return id;
    }

    private final int width, height;
    private final long id;

    public final List<IRenderAction> SCREEN_SPACE_RENDER_ACTIONS = new ArrayList<>();
    public final List<IRenderAction> WORLD_SPACE_RENDER_ACTIONS = new ArrayList<>();

    public final List<IUpdateAction> UPDATE_ACTIONS = new ArrayList<>();

    protected Window(long id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void show() {
        glfwShowWindow(id);
    }

    public void destroy() {
        glfwFreeCallbacks(id);
        glfwDestroyWindow(id);
    }

    public void loop() {
        Camera camera = Camera.getInstance();

        while (!glfwWindowShouldClose(id)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            UPDATE_ACTIONS.forEach(IUpdateAction::update);

            camera.renderAllWithTranslation(WORLD_SPACE_RENDER_ACTIONS);
            SCREEN_SPACE_RENDER_ACTIONS.forEach(IRenderAction::render);

            glfwSwapBuffers(id);
            glfwPollEvents();
        }
    }
}
