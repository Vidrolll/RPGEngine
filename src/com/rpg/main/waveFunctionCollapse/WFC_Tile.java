package com.rpg.main.waveFunctionCollapse;

import java.awt.*;

public class WFC_Tile {

    public Boolean symmetrical;
    public String[] sockets;
    private int rotations = 0;
    public Image renderImage;
    /**
    *
    * @param symmetrical specify whether the tile  is symmetrical
    * @param sockets sockets the tile uses to collapse the wave function in URDL order
    * */
    public WFC_Tile(Boolean symmetrical, String[] sockets, Image img){

        this.symmetrical = symmetrical;
        this.sockets = sockets;
        this.renderImage = img;

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
    * @return a rotated instance of WFC_Tile
    * */
    public WFC_Tile rotate(int times){

        String[] newSockets = new String[4];
        for (int i = 0; i < 4; i++) {
            newSockets[i] = this.sockets[(i + times) % 4];
        }
        WFC_Tile rotTile = new WFC_Tile(symmetrical, newSockets, renderImage);
        rotTile.setRotation(times);

        return rotTile;

    }

}
