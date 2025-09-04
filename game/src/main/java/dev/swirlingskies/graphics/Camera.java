package dev.swirlingskies.graphics;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Camera {
    private final Vector2f position = new Vector2f();
    private float rotationDeg = 0f;   // clockwise screen-space rotation
    private float zoom = 1f;          // 1 = 100%
    private int fbWidth = 1920, fbHeight = 1080;

    /** Call on startup and whenever the framebuffer size changes. */
    public Camera setViewport(int framebufferWidth, int framebufferHeight) {
        this.fbWidth = Math.max(1, framebufferWidth);
        this.fbHeight = Math.max(1, framebufferHeight);
        return this;
    }

    // --- State setters/getters ---
    public Camera setPosition(float x, float y) { this.position.set(x, y); return this; }
    public Vector2f getPosition() { return position; }

    public Camera setRotationDeg(float deg) { this.rotationDeg = deg; return this; }
    public float getRotationDeg() { return rotationDeg; }

    public Camera setZoom(float z) { this.zoom = Math.max(0.01f, z); return this; }
    public float getZoom() { return zoom; }

    // --- World rendering (with camera) ---
    /** Pushes current PROJECTION/MODELVIEW, sets ortho + camera transform (Y-down). */
    public void begin() {
        // PROJECTION: pixel-space ortho, origin top-left, Y down.
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, fbWidth, fbHeight, 0, -1, 1);

        // MODELVIEW: inverse of camera transform → scale, rotate, translate(-pos)
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glTranslatef(1920/2f, 1080/2f, 0f);
        glScalef(zoom, zoom, 1f);
        glTranslatef(-1920/2f, -1080/2f, 0f);
        glRotatef(-rotationDeg, 0f, 0f, 1f);
        glTranslatef(-position.x, -position.y, 0f);
    }

    /** Restores previous PROJECTION/MODELVIEW. Call after drawing world. */
    public void end() {
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
    }

    // --- UI rendering (no camera) ---
    /** Pushes matrices and sets a pure pixel-space ortho (no camera). */
    public void beginUi() {
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, fbWidth, fbHeight, 0, -1, 1);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
    }

    /** Pops matrices after drawing UI/HUD. */
    public void endUi() {
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
    }
}
