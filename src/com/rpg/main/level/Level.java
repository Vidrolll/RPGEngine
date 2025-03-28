package com.rpg.main.level;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.math.Polygon;
import com.rpg.main.player.PlayerController;

import java.util.HashMap;

public class Level {
    /**
     * Holds the information of where each hitbox on the map is.
     */
    public HashMap<String, Polygon> map = new HashMap<>();

    /**
     * The controller the player will use to interact with the level.
     */
    PlayerController controller;

    /**
     * Creates a new Level object and takes in a PlayerController to be able to play the level with.
     *
     * @param controller (PlayerController) The inputted controller to play the level with.
     */
    public Level(PlayerController controller) {
        this.controller = controller;
    }

    /**
     * Run through a new update of the level.
     */
    public void update() {
        controller.update();
        for (Polygon poly : map.values()) controller.collide(poly);
    }

    /**
     * Render a new frame of the level.
     *
     * @param gl (GL2) The OpenGL drawable object to render to.
     */
    public void render(GL2 gl) {
        controller.render(gl);
        for (Polygon poly : map.values()) poly.renderPolygon(gl);
    }

    /**
     * Keyboard input to feed into the level.
     *
     * @param e (KeyEvent) The inputted key event.
     */
    public void input(KeyEvent e) {
        controller.input(e);
    }

    /**
     * Mouse input to feed into the level.
     *
     * @param e (MouseEvent) The inputted mouse event.
     */
    public void input(MouseEvent e) {
        controller.input(e);
    }
}
