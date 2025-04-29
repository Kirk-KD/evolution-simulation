package com.kirkkd.evolution.rendering;

import com.kirkkd.evolution.util.Color;
import com.kirkkd.evolution.util.Vec2f;

import static org.lwjgl.opengl.GL11.*;

public class PrimitiveShapes {
    public static class Context {
        public final Color border;
        public final Color fill;
        public final Vec2f center;

        public Context(Vec2f center, Color border, Color fill) {
            this.center = center;
            this.border = border;
            this.fill = fill;
        }

        public Context(Vec2f center, Color fill) {
            this(center, fill, fill);
        }
    }

    public static void square(Context ctx, float size) {
        rectangle(ctx, size, size);
    }

    public static void rectangle(Context ctx, float width, float height) {
        float halfW = width * 0.5f;
        float halfH = height * 0.5f;
        float x1 = ctx.center.getX() - halfW;
        float x2 = ctx.center.getX() + halfW;
        float y1 = ctx.center.getY() - halfH;
        float y2 = ctx.center.getY() + halfH;

        ctx.fill.apply();
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();

        ctx.border.apply();
        glLineWidth(1f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
    }

    public static void circle(Context ctx, float radius) {
        int segments = 32;
        float cx = ctx.center.getX();
        float cy = ctx.center.getY();

        ctx.fill.apply();
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(cx, cy);
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            glVertex2f(
                    cx + (float) Math.cos(angle) * radius,
                    cy + (float) Math.sin(angle) * radius
            );
        }
        glEnd();

        ctx.border.apply();
        glLineWidth(1f);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            glVertex2f(
                    cx + (float) Math.cos(angle) * radius,
                    cy + (float) Math.sin(angle) * radius
            );
        }
        glEnd();
    }
}
