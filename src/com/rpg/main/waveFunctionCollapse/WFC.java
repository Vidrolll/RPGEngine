package com.rpg.main.waveFunctionCollapse;

// reference:
// https://github.com/mxgmn/WaveFunctionCollapse

import com.rpg.main.gui.MainWindow;
import com.rpg.main.math.Vector2;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// hold the logic behind wave function collapse
public class WFC {

    public static WFC_Grid collapse() throws ExecutionControl.NotImplementedException {

        throw new ExecutionControl.NotImplementedException("Full collapse function not yet implemented");

    }

    public static WFC_Grid collapseStep(WFC_Grid grid){

        // copy grid
        WFC_Grid gridCopy = grid.clone();

        // get entropy
        int[] Entropies = new int[grid.width * grid.height];

        // loop through each cell
        calculateNewWFC(grid, gridCopy, Entropies);

        // find minimum entropy and which cells have minimum entropy
        int minEntropy = grid.TileSet.length + 1;
        List<Integer> minEntropyIndicies = new ArrayList<Integer>();

        for (int i = 0; i < Entropies.length; i++){
            System.out.println(Entropies[i] + "  " + minEntropy);

            if (Entropies[i] < minEntropy && Entropies[i] != 1){

                minEntropy = Entropies[i];
                minEntropyIndicies.clear();
                minEntropyIndicies.add(i);

            }
            else if (Entropies[i] == minEntropy){

                minEntropyIndicies.add(i);

            }

        }

        System.out.println(minEntropyIndicies);

        // pick a cell to collapse
        Random rng = new Random();
        int index = minEntropyIndicies.get(rng.nextInt(minEntropyIndicies.size()));
        Vector2 gridPos = grid.XYFromIndex(index);

        // collapse that cell
        WFC_Cell cell = gridCopy.getCell((int)gridPos.getX(), (int)gridPos.getY());
        WFC_Tile[] possibleTiles = cell.possibleTiles;
        cell.possibleTiles = new WFC_Tile[]{possibleTiles[rng.nextInt(possibleTiles.length)]};
        cell.setCollapsed();
        gridCopy.changeCell((int)gridPos.getX(), (int)gridPos.getY(), cell);

        calculateNewWFC(gridCopy);

        return gridCopy;

    }

    private static void calculateNewWFC(WFC_Grid grid, WFC_Grid gridCopy, int[] Entropies) {
        for (int i = 0; i < grid.width; i++) {

            for (int j = 0; j < grid.height; j++) {

                // get cell and surrounding cells
                WFC_Cell cell = grid.getCell(i, j);
                WFC_Cell[] surroundingCells = grid.getSurrounding(i, j);

                // calculate the tiles that can fit
                boolean[][] socketCheckRes = cell.checkSockets(surroundingCells);
                boolean[] possibilities = new boolean[cell.getEntropy()];
                int remainingPossiblities = 0;

                // get ne possibilities
                for (int k = 0; k < cell.getEntropy(); k++){

                    possibilities[k] = socketCheckRes[0][k] && socketCheckRes[1][k] && socketCheckRes[2][k] && socketCheckRes[3][k];
                    if (possibilities[k]){

                        remainingPossiblities += 1;

                    }

                }

                // get mew possible tiles
                WFC_Tile[] newPossibilities = new WFC_Tile[remainingPossiblities];
                int index = 0;

                // update cells possible tiles
                for (int k = 0; k < cell.getEntropy(); k++){

                    if (possibilities[k]) {
                        newPossibilities[index] = cell.possibleTiles[k];
                        index++;
                    }

                }

                System.out.println(cell.possibleTiles.length);
                System.out.println(newPossibilities.length);
                System.out.print("---\n");
                cell.possibleTiles = newPossibilities;
                gridCopy.changeCell(i, j, cell);

                // get entropy and store it
                Entropies[gridCopy.indexFromXY(i, j)] = cell.getEntropy();

            }

        }
    }
    public static void calculateNewWFC(WFC_Grid grid){

        calculateNewWFC(grid.clone(), grid, new int[grid.height * grid.width]);

    }

    /**
    *
    * @param grid grid which to pull elements from
    * @param x top left corner x
    * @param y top left corner y
    * @param areaW width to draw the whole grid
    * @param areaH height to draw the whole grid
    * */
    public static void draw(Graphics2D g, WFC_Grid grid, int x, int y, int areaW, int areaH){

        float w, h; // width and height of drawn cells, respectively
        w = areaW / ((float)grid.width+1);
        h = areaH / ((float)grid.height+1);

        // instantiate random object
        Random rng = new Random(10);

        // loop through each grid element
        for (int i = 0; i < grid.width; i++) {

            for (int j = 0; j < grid.height; j++) {

                // get a random tile
                WFC_Cell cell = grid.getCell(i, j);
                if (cell.isCollapsed()) {

                    WFC_Tile tile = cell.possibleTiles[0];

                    // get its image and find the width and height (uses ImageObserver... whatever that is)
                    Image image = tile.renderImage;
                    double imgw = image.getWidth((img, infoflags, x1, y1, width, height) -> {
                        return true;
                    });
                    double imgh = image.getHeight((img, infoflags, x1, y1, width, height) -> {
                        return true;
                    });

                    // get image rotation
                    double theta = tile.getRotation();

                    // setup image transformation
                    AffineTransform trans = new AffineTransform(w / imgw, 0, 0, h / imgh, x + i * w, y + j * h);
                    trans.rotate(theta, x + imgw / 2, y + imgh / 2);

                    // draw image
                    g.drawImage(tile.renderImage, trans, (img, infoflags, x1, y1, width, height) -> {
                        return true;
                    });

                }

                // draw entropy text
                g.setColor(Color.MAGENTA);
                g.drawString(String.valueOf(grid.getCell(i, j).getEntropy()), i*w, j*h+h/4);

            }

        }

    }

    public static void draw(Graphics2D g, WFC_Grid grid){

        int smallestDim = Math.min(MainWindow.getSize().height, MainWindow.getSize().width);
        draw(g, grid, 0, 0, smallestDim, smallestDim);

    }

}
