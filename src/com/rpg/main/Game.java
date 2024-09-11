package com.rpg.main;

import com.rpg.main.gui.MainWindow;
import com.rpg.main.util.InputHandler;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

/**
 * Creating a new game utilizing this engine should be done by making a
 * main class that extends this class, and creating a new instance of that class.
 */

public abstract class Game extends Canvas implements Runnable {
    //Whether the engine is running.
    boolean running = false;
    //Thread to run the game on.
    Thread thread;

    /**
     * Constructor used by the engine, this method should be supered in the extended class for proper functionality.
     */
    public Game() {
        new MainWindow(this);
        this.addKeyListener(new InputHandler(this));
        this.addMouseListener(new InputHandler(this));
        this.addMouseMotionListener(new InputHandler(this));
        this.addMouseWheelListener(new InputHandler(this));
        this.addFocusListener(new InputHandler(this));
    }

    /**
     * Run method used by the engine, this method should not be tampered with.
     */
    @Override
    public void run() {
        this.requestFocus();
        double ns = 1000000000 / 60.0;
        long lastTime = System.nanoTime();
        double delta = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime = now;
            while(delta>=1) {
                update();
                render();
                delta--;
            }
        }
        stop();
    }

    /**
     * Start method used by the engine, this method should not be tampered with.
     */
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    /**
     * Stop method used by the engine, this method should not be tampered with.
     */
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Render method used by the engine, this method should not be tampered with.
     */
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(2);
            return;
        }
        Graphics2D g = (Graphics2D)bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,10000,10000);
        draw(g);
        g.dispose();
        bs.show();
    }

    //The draw function of the game, locked to the framerate.
    public abstract void draw(Graphics2D g);
    //The update function of the game, locked to the framerate.
    public abstract void update();

    //Keyboard input to the game.
    public abstract void input(KeyEvent e);
    //Mouse input to the game. X and Y values are not scaled to game resolution.
    public abstract void input(MouseEvent e, int x, int y);
    //Focus input to the game.
    public abstract void input(FocusEvent e);
}