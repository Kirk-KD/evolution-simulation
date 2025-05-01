package com.kirkkd.evolution.rendering;

import com.kirkkd.evolution.util.Color;
import com.kirkkd.evolution.util.Vec2d;

import static org.lwjgl.opengl.GL11.*;

public class PrimitiveShapes {
    public static class Context {
        public final Color border;
        public final Color fill;
        public final Vec2d center;

        public Context(Vec2d center, Color border, Color fill) {
            this.center = center;
            this.border = border;
            this.fill = fill;
        }

        public Context(Vec2d center, Color fill) {
            this(center, fill, fill);
        }
    }

    public static void square(Context ctx, double size) {
        rectangle(ctx, size, size);
    }

    public static void rectangle(Context ctx, double width, double height) {
        double halfW = width * 0.5f;
        double halfH = height * 0.5f;
        double x1 = ctx.center.x() - halfW;
        double x2 = ctx.center.x() + halfW;
        double y1 = ctx.center.y() - halfH;
        double y2 = ctx.center.y() + halfH;

        ctx.fill.apply();
        glBegin(GL_QUADS);
        glVertex2d(x1, y1);
        glVertex2d(x2, y1);
        glVertex2d(x2, y2);
        glVertex2d(x1, y2);
        glEnd();

        ctx.border.apply();
        glLineWidth(1f);
        glBegin(GL_LINE_LOOP);
        glVertex2d(x1, y1);
        glVertex2d(x2, y1);
        glVertex2d(x2, y2);
        glVertex2d(x1, y2);
        glEnd();
    }

    public static void circle(Context ctx, double radius) {
        int segments = 32;
        double cx = ctx.center.x();
        double cy = ctx.center.y();

        ctx.fill.apply();
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(cx, cy);
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            glVertex2d(
                    cx + Math.cos(angle) * radius,
                    cy + Math.sin(angle) * radius
            );
        }
        glEnd();

        ctx.border.apply();
        glLineWidth(1f);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            glVertex2d(
                    cx + Math.cos(angle) * radius,
                    cy + Math.sin(angle) * radius
            );
        }
        glEnd();
    }
}
