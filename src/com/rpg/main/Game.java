package com.rpg.main;

import com.rpg.main.gui.MainWindow;
import com.rpg.main.util.InputHandler;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    boolean running = false;
    Thread thread;

    public Game() {
        new MainWindow(this);
        this.addKeyListener(new InputHandler(this));
        this.addMouseListener(new InputHandler(this));
        this.addMouseMotionListener(new InputHandler(this));
        this.addMouseWheelListener(new InputHandler(this));
        this.addFocusListener(new InputHandler(this));
    }

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

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {

    }
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

    public void draw(Graphics2D g) {

    }

    public void input(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
    }
    public void input(MouseEvent e, int x, int y) {

    }
    public void input(FocusEvent e) {

    }
}