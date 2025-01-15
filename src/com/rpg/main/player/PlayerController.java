package com.rpg.main.player;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.rpg.main.entity.Entity;

public class PlayerController {
    //Variables for the player controller.
    Entity player;

    /**
     * Creates a new player controller used to control the inputted entity.
     * @param player (Entity) The inputted entity to control.
     */
    public PlayerController(Entity player) {
        this.player = player;
    }

    /**
     * Keyboard input to feed into the player controller.
     * @param e (KeyEvent) The inputted key event.
     */
    public void input(KeyEvent e) {

    }

    /**
     * Mouse input to feed into the player controller.
     * @param e (MouseEvent) The inputted key event.
     */
    public void input(MouseEvent e) {

    }
}
