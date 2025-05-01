package com.kirkkd.evolution.rendering;

import com.kirkkd.evolution.util.Vec2d;
import com.kirkkd.evolution.window.IUpdateAction;
import com.kirkkd.evolution.window.Window;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Camera implements IUpdateAction {
    private static Camera instance;

    public static Camera getInstance() {
        return instance;
    }

    public static void create() {
        instance = new Camera();
    }

    private Vec2d position = Vec2d.zero();
    private Vec2d deltaPosition = Vec2d.zero();

    protected Camera() {
        Window window = Window.getInstance();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        window.UPDATE_ACTIONS.add(this);
    }

    public void renderAllWithTranslation(List<IRenderAction> actions) {
        glPushMatrix();
        glTranslated(-position.x(), -position.y(), 0);
        actions.forEach(IRenderAction::render);
        glPopMatrix();
    }

    public void setDeltaPosition(Vec2d deltaPosition) {
        this.deltaPosition = deltaPosition;
    }

    @Override
    public void update() {
        position = position.add(deltaPosition);
        deltaPosition = Vec2d.zero();
    }
}
