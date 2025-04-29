package com.kirkkd.evolution.window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {
    private static final boolean[] keys = new boolean[GLFW_KEY_LAST];

    public static void callback(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key < keys.length) keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }
}
