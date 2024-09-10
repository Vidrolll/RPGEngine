package com.rpg.main.waveFunctionCollapse;

// Holds data about a cell in the grid
public class WFC_Cell implements Cloneable{

    WFC_Tile[] possibleTiles;
    private boolean collapsed = false;

    /**
    * Cells are initialized in WFC_Grid
    *
    * @param tileSet get tileSet for grid to initialize possible cells
    * */
    public WFC_Cell(WFC_Tile[] tileSet){

        possibleTiles = tileSet;

    }

    /**
     * @return returns the number of possible tiles this cell has, if it is collapsed it returns -1
     * */
    public int getEntropy(){

        return possibleTiles.length;

    }

    /**
     * @return returns whether this cell is collapsed
     * */
    public boolean isCollapsed(){

        return collapsed;

    }

    public void setCollapsed(){

        this.collapsed = true;

    }

    /**
    *
    * @param surroundingCells surrounding cells in URDL order
    * */
    public boolean[][] checkSockets(WFC_Cell[] surroundingCells){

        boolean[][] out = new boolean[4][];

        for (int i = 0; i < 4; i++){

            out[i] = socketMatch(i, surroundingCells[i]);

        }

        return out;

    }

    /**get which tile(s) this cell could be by checking against another cells possibilities
     *
     * @param direction
     * @param Cell
     * @return
     */
    protected boolean[] socketMatch(int direction, WFC_Cell Cell){

        boolean[] keepPossibility = new boolean[this.getEntropy()];

        // for every possible tile
        for (int i = 0; i < this.getEntropy(); i++){

            keepPossibility[i] = true;
            // if not checking against null tile
            if (Cell != null && Cell.isCollapsed()) {

                // go over evert possible tile in other cell and make sure sockets are the same
                for (int j = 0; j < Cell.getEntropy(); j++) {

                    WFC_Tile tile = this.possibleTiles[i];
                    WFC_Tile compareTile = Cell.possibleTiles[j];
                    keepPossibility[i] ^= (tile.getSocket(direction) != compareTile.getSocket((direction+2)%4));

                }

            }

        }

        return keepPossibility;

    }

    public WFC_Cell clone() {

        try {

            WFC_Cell copy = (WFC_Cell) super.clone();

            copy.possibleTiles = this.possibleTiles.clone();
            copy.collapsed = this.collapsed;

            return copy;

        }

        catch (CloneNotSupportedException e){

            return null;

        }

    }


    public WFC_Cell copy() {

        return this.clone();

    }
}
