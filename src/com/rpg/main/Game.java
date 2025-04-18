package com.rpg.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.graphics.Camera;
import com.rpg.main.graphics.opengl.Renderer;

/**
 * Creating a new game utilizing this engine should be done by making a
 * main class that extends this class, and creating a new instance of that class.
 */

public abstract class Game {
    /**
     * The current game camera object.
     */
    public static final Camera CAMERA = new Camera();

    /**
     * Constructor used by the engine, this method should be supered in the extended class for proper functionality.
     */
    public Game() {
        Renderer.init(this);
    }

    /**
     * Moves the screen position based on the position of the camera.
     *
     * @param gl (GL2) The current OpenGL drawable object.
     */
    public void camera(GL2 gl) {
        CAMERA.update();
        gl.glPushMatrix();
        gl.glTranslatef(-CAMERA.getPosition().getX(), -CAMERA.getPosition().getY(), 0.0f);
        gl.glScalef(CAMERA.getScale().getX(), CAMERA.getScale().getY(), 1.0f);
    }

    /**
     * The draw function of the game, locked to the framerate.
     */
    public abstract void draw(GL2 gl);

    /**
     * The update function of the game, locked to the framerate.
     */
    public abstract void update();

    /**
     * Key event function of the game.
     * @param e (KeyEvent) The key input detected.
     */
    public abstract void input(KeyEvent e);

    /**
     * Mouse event function of the game.
     * @param e (MouseEvent) The mouse input detected.
     */
    public abstract void input(MouseEvent e);
}