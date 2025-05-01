package com.rpg.main.math.noise.waveFunctionCollapse;

import java.awt.*;

public class WFC_Tile {

    private boolean canRender = true;
    private int id;
    public Boolean symmetrical;
    public String[] sockets;
    private int rotations = 0;
    public Image renderImage;
    /** create a tile without an id but can be drawn
    *
    * @param symmetrical specify whether the tile  is symmetrical
    * @param sockets sockets the tile uses to collapse the wave function in URDL order
    * @param img image to draw when rendered in grid
    * */
    public WFC_Tile(Boolean symmetrical, String[] sockets, Image img){

        this.symmetrical = symmetrical;
        this.sockets = sockets;
        this.renderImage = img;
        this.id = -1;

    }

    /**create Tile with id that CANNOT be drawn
     *
     * @param symmetrical specify whether the tile  is symmetrical
     * @param sockets sockets the tile uses to collapse the wave function in URDL order
     * @param id id of the tile
     */
    public WFC_Tile(Boolean symmetrical, String[] sockets, int id) {

        this.symmetrical = symmetrical;
        this.sockets = sockets;
        this.renderImage = null;
        this.canRender = false;
        this.id = id;

    }

    /**create a tile with an id and an image to draw
     *
     * @param symmetrical specify whether the tile  is symmetrical
     * @param sockets sockets the tile uses to collapse the wave function in URDL order
     * @param id id of the tile
     * @param img image to draw when rendered in grid
     */
    public WFC_Tile(Boolean symmetrical, String[] sockets, int id, Image img) {

        this.symmetrical = symmetrical;
        this.sockets = sockets;
        this.renderImage = img;
        this.id = id;

    }

    /** gets id assigned in constructor
     *
     */
    public int getId(){

        return this.id;

    }

    public boolean canRender(){

        return this.canRender;

    }

    /**
    * Set the tile rotation
    *
    * @param rot angle in increments of 90 degrees (rot * -90)
    * */
    public void setRotation(int rot){

        this.rotations = rot;

    }

    /**
    * Get the rotation needed to draw image
    *
    * @return angle in radians
    * */
    public double getRotation(){

        return (Math.PI*2 - this.rotations * Math.PI/4) % Math.PI*2;

    }

    public String getSocket(int dir){

        return this.sockets[dir];

    }

    /**
    * CCW rotation of a tile
    *
    * @param times number of times to rotate tile
    * @param id id of new tile
    * @return a rotated instance of WFC_Tile
    * */
    public WFC_Tile rotate(int times, int id) {

        String[] newSockets = new String[4];
        for (int i = 0; i < 4; i++) {
            newSockets[i] = this.sockets[(i + times) % 4];
        }
        WFC_Tile rotTile = new WFC_Tile(symmetrical, newSockets, renderImage);
        rotTile.setRotation(times);

        rotTile.id = id;
        return rotTile;

    }

    /**
     * CCW rotation of a tile
     *
     * @param times number of times to rotate tile
     * @return a rotated instance of WFC_Tile
     * */
    public WFC_Tile rotate(int times) {

        return this.rotate(times, -1);

    }

}
