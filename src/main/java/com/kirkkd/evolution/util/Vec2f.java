package com.kirkkd.evolution.util;

public class Vec2f {
    public static Vec2f zero() {
        return new Vec2f(0, 0);
    }

    private float x;

    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void normalizeInPlace() {
        float magnitude = (float) Math.sqrt(x * x + y * y);
        if (magnitude != 0) {
            x /= magnitude;
            y /= magnitude;
        }
    }

    public void addInPlace(Vec2f v) {
        x += v.x;
        y += v.y;
    }

    public void multiplyInPlace(Vec2f v) {
        x *= v.x;
        y *= v.y;
    }

    public void multiplyInPlace(float f) {
        x *= f;
        y *= f;
    }

    public void negateInPlace() {
        x = -x;
        y = -y;
    }
}
