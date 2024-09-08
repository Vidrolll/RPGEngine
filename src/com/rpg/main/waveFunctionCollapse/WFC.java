package com.rpg.main.waveFunctionCollapse;

// reference:
// https://github.com/mxgmn/WaveFunctionCollapse

import com.rpg.main.gui.MainWindow;

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
    public static void draw(WFC_Grid grid, int x, int y, int areaW, int areaH){

        int w, h; // width and height of drawn cells, respectively
        w = areaW / grid.width;
        h = areaH / grid.height;

    }

    public static void draw(WFC_Grid grid){

        int smallestDim = Math.min(MainWindow.getSize().height, MainWindow.getSize().width);
        draw(grid, 0, 0, smallestDim, smallestDim);

    }

}
