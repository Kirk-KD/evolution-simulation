package com.kirkkd.evolution.rendering;

import com.kirkkd.evolution.util.Vec2f;
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

    private final Vec2f position = Vec2f.zero();
    private Vec2f deltaPosition = Vec2f.zero();

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
        glTranslatef(-position.getX(), -position.getY(), 0);
        actions.forEach(IRenderAction::render);
        glPopMatrix();
    }

    public void setDeltaPosition(Vec2f deltaPosition) {
        this.deltaPosition = deltaPosition;
    }

    @Override
    public void update() {
        position.addInPlace(deltaPosition);
        deltaPosition = Vec2f.zero();
    }
}
