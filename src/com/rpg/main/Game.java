package com.rpg.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.opengl.Renderer;
import com.rpg.main.util.InputHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Creating a new game utilizing this engine should be done by making a
 * main class that extends this class, and creating a new instance of that class.
 */

public abstract class Game {
    /**
     * Constructor used by the engine, this method should be supered in the extended class for proper functionality.
     */
    public Game() {
        Renderer.init(this);
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