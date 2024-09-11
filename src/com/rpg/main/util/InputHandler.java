package com.rpg.main.util;

import com.rpg.main.Game;

import java.awt.event.*;

/**
 * All input feeds into the input functions of the Game class, this class should not be utilized outside of that context.
 * Any input handled should involve an override of the Game class input functions.
 */
public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener {
    Game main;

    public InputHandler(Game main) {
        this.main = main;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        main.input(e);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        main.input(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        main.input(e);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mousePressed(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mouseExited(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        main.input(e,e.getX(),e.getY());
    }
    @Override
    public void focusGained(FocusEvent e) {
        main.input(e);
    }
    @Override
    public void focusLost(FocusEvent e) {
        main.input(e);
    }
}
