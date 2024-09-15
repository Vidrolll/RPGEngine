package com.rpg.main.util;

import com.jogamp.newt.event.*;
import com.rpg.main.Game;

/**
 * All input feeds into the input functions of the Game class, this class should not be utilized outside of that context.
 * Any input handled should involve an override of the Game class input functions.
 */
public class InputHandler implements KeyListener, MouseListener {
    Game game;

    public InputHandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        game.input(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        game.input(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        game.input(e);
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        game.input(e);
    }
}
