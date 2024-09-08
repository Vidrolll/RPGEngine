package com.rpg.main.waveFunctionCollapse;

import java.awt.*;
import java.awt.image.ImageFilter;

public class WFC_Tile {

    public Boolean symmetrical;
    public String[] sockets;
    private int rotations = 0;
    public Image renderImage;
    /*
    *
    * @param symmetrical specify whether the tile  is symmetrical
    * @param sockets sockets the tile uses to collapse the wave function in URDL order
    * */
    public WFC_Tile(Boolean symmetrical, String[] sockets, Image img){

        this.symmetrical = symmetrical;
        this.sockets = sockets;
        this.rotations = 0;
        renderImage = img;

    }

    public void setRotation(int rot){

        this.rotations = rot;

    }

    public double getRotation(){

        return (360 - this.rotations * 90) % 360;

    }

    /*
    * CCW rotation of a tile
    *
    * @param number of times to rotate tile
    * */
    public WFC_Tile rotate(int times){

        String[] newSockets = new String[4];
        for (int i = 0; i < 4; i++) {
            newSockets[i] = this.sockets[i + times % 4];
        }
        WFC_Tile rotTile = new WFC_Tile(symmetrical, newSockets, renderImage);
        rotTile.setRotation(times);

        return rotTile;

    }

}
