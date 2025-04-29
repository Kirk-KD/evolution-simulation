package com.kirkkd.evolution.util;

import static org.lwjgl.opengl.GL11.glColor4f;

public class Color {
    public final int r, g, b, a;

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public void apply() {
        glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}
