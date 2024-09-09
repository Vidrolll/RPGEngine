package com.rpg.main.waveFunctionCollapse;

// reference:
// https://github.com/mxgmn/WaveFunctionCollapse

import com.rpg.main.gui.MainWindow;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

// hold the logic behind wave function collapse
public class WFC {

    public static void collapse(){



    }

    /*
    *
    * @param grid grid which to pull elements from
    * @param x top left corner x
    * @param y top left corner y
    * @param areaW width to draw the whole grid
    * @param areaH height to draw the whole grid
    * */
    public static void draw(Graphics2D g, WFC_Grid grid, int x, int y, int areaW, int areaH){

        double w, h; // width and height of drawn cells, respectively
        w = areaW / ((double)grid.width+1);
        h = areaH / ((double)grid.height+1);

        // instantiate random object
        Random rng = new Random(10);

        // loop through each grid element
        for (int i = 0; i < grid.width; i++) {

            for (int j = 0; j < grid.height; j++) {

                // get a random tile
                WFC_Tile randTile = grid.TileSet[rng.nextInt(grid.TileSet.length)];

                // get its image and find the width and height (uses ImageObserver... whatever that is)
                Image image = randTile.renderImage;
                double imgw = image.getWidth((img, infoflags, x1, y1, width, height) -> {return true;});
                double imgh = image.getHeight((img, infoflags, x1, y1, width, height) -> {return true;});

                // get image rotation
                double theta = randTile.getRotation();

                // setup image transformation
                AffineTransform trans = new AffineTransform(w/imgw,0,0,h/imgh,x+i*w,y+j*h);
                trans.rotate(theta, x+imgw/2, y+imgh/2);

                // draw image
                g.drawImage(randTile.renderImage, trans, (img, infoflags, x1, y1, width, height) -> {return true;});

            }

        }

    }

    public static void draw(Graphics2D g, WFC_Grid grid){

        int smallestDim = Math.min(MainWindow.getSize().height, MainWindow.getSize().width);
        draw(g, grid, 0, 0, smallestDim, smallestDim);

    }

}
