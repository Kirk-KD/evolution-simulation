package com.kirkkd.evolution.util;

public record Vec2d(double x, double y) {
    public static Vec2d zero() {
        return new Vec2d(0, 0);
    }

    public Vec2d normalize() {
        double length = Math.sqrt(x * x + y * y);
        if (length == 0) return Vec2d.zero();
        return new Vec2d(x / length, y / length);
    }

    public Vec2d add(Vec2d v) {
        return new Vec2d(this.x + v.x, this.y + v.y);
    }

    public Vec2d multiply(Vec2d v) {
        return new Vec2d(this.x * v.x, this.y * v.y);
    }

    public Vec2d multiply(double m) {
        return new Vec2d(this.x * m, this.y * m);
    }
}
