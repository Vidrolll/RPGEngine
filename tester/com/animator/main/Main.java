package com.animator.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main extends Game {
    public Main() {

    }

    @Override
    public void draw(GL2 gl) {
        gl.glColor3f(0.5f,0.5f,0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0,0);
        gl.glVertex2f(1920,0);
        gl.glVertex2f(1920,1080);
        gl.glVertex2f(0,1080);
        gl.glEnd();
        gl.glFlush();
    }
    @Override
    public void update() {

    }
    @Override
    public void input(KeyEvent e) {
        if(e.getEventType() == KeyEvent.EVENT_KEY_PRESSED&&e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }
    @Override
    public void input(MouseEvent e) {

    }

    public static void main(String[] args) {
        new Main();
    }
}
