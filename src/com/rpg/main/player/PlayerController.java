package com.rpg.main.player;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.entity.Entity;
import com.rpg.main.math.Polygon;

public class PlayerController {
    //Variables for the player controller.
    Entity player;

    /**
     * Keyboard input to feed into the player controller.
     * @param e (KeyEvent) The inputted key event.
     */
    boolean[] keyDown = {false, false,false,false};

    /**
     * Creates a new player controller used to control the inputted entity.
     * @param player (Entity) The inputted entity to control.
     */
    public PlayerController(Entity player) {
        this.player = player;
        Game.CAMERA.lockOn(player);
    }

    /**
     * Cause the player controller to collide with an inputted polygon.
     *
     * @param poly (Polygon) The polygon to collide with.
     */
    public void collide(Polygon poly) {
        player.collide(poly);
    }

    /**
     * Updates the player.
     */
    public void update() {
        player.update();
    }

    /**
     * Renders the player.
     *
     * @param gl (GL2) The OpenGL drawable object to render the player to.
     */
    public void render(GL2 gl) {
        player.render(gl);
    }

    public void input(KeyEvent e) {
        if (e.getEventType() == KeyEvent.EVENT_KEY_PRESSED && (InputEvent.AUTOREPEAT_MASK & e.getModifiers()) == 0) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                keyDown[2] = true;
                player.setVelX(-5);
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                keyDown[3] = true;
                player.setVelX(5);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) player.setVelY(-20);
        }
        if (e.getEventType() == KeyEvent.EVENT_KEY_RELEASED && (InputEvent.AUTOREPEAT_MASK & e.getModifiers()) == 0) {
            if (e.getKeyCode() == KeyEvent.VK_A) keyDown[2] = false;
            if (e.getKeyCode() == KeyEvent.VK_D) keyDown[3] = false;
            if (!(keyDown[2] || keyDown[3])) player.setVelX(0);
        }
    }

    /**
     * Mouse input to feed into the player controller.
     * @param e (MouseEvent) The inputted key event.
     */
    public void input(MouseEvent e) {

    }
}
