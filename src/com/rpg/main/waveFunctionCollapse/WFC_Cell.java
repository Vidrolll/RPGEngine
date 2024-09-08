package com.rpg.main.waveFunctionCollapse;

// Holds data about a cell in the grid
public class WFC_Cell{

    WFC_Tile[] possibleTiles;

    /*
    * Cells are initialized in WFC_Grid
    *
    * @param tileSet get tileSet for grid to initialize possible cells
    * */
    public WFC_Cell(WFC_Tile[] tileSet){

        possibleTiles = tileSet;

    }

    public int getEntropy(){

        return possibleTiles.length;

    }

    /*
    *
    * @param surroundingCells surrounding cells in URDL order
    * */
    public boolean checkSockets(WFC_Cell[] surroundingCells){

        for (int i = 0; i < 4; i++){



        }

        return false;

    }

    public boolean socketMatch(int direction, int socket){

        for (int i = 0; i < this.possibleTiles.length; i++){

            WFC_Tile tile = possibleTiles[i];

        }

        return false;

    }


}
