package com.kirkkd.evolution.rendering;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class DisplayManager {
    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SAMPLES, 4);
    }

    public static void destroy() {
        glfwTerminate();
        try (GLFWErrorCallback errorCallback = glfwSetErrorCallback(null)) {
            if (errorCallback != null) errorCallback.free();
        }
    }

    public static void createCapabilities() {
        GL.createCapabilities();
    }
}
