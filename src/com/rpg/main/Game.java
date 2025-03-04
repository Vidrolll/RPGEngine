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
    //Camera for rendering the location of the screen.
    private Camera camera = new Camera();

    /**
     * Constructor used by the engine, this method should be supered in the extended class for proper functionality.
     */
    public Game() {
        Renderer.init(this);
    }

//    @Override
//    public void run() {
//        double ns = 1000000000/60.0D;
//        long lastTime = System.nanoTime();
//        while(running) {
//            long now = System.nanoTime();
//            Time.deltaTime = (now-lastTime)/ns;
//            lastTime = now;
//            Renderer.display();
//        }
//        stop();
//    }


    public Camera getCamera() {
        return camera;
    }

    public void camera(GL2 gl) {
        camera.update();
        gl.glPushMatrix();
        gl.glTranslatef(-camera.getPosition().getX(), -camera.getPosition().getY(), 0.0f);
        gl.glScalef(camera.getScale().getX(), camera.getScale().getY(), 1.0f);
    }

    //The draw function of the game, locked to the framerate.
    public abstract void draw(GL2 gl);
    //The update function of the game, locked to the framerate.
    public abstract void update();
    //Key event function of the game
    public abstract void input(KeyEvent e);
    //Mouse event function of the game
    public abstract void input(MouseEvent e);
}