package com.rpg.main.waveFunctionCollapse;

// Grid to hold Cells
public class WFC_Grid {

    public int width, height;
    public int tileSockets;
    private WFC_Cell[] Cells;
    public WFC_Tile[] TileSet;
    /*
    * Class that hold WFC_Cell in a grid
    *
    * @param w width of grid
    * @param h height of grid
    * @param socketCount number of sockets per side on Tiles
    * @param tileSet
    * */
    public WFC_Grid(int w, int h, int socketCount, WFC_Tile[] tileSet){

        this.width = w;
        this.height = h;
        this.tileSockets = socketCount;
        this.TileSet = tileSet;
        // initialize Cell Grid
        this.Cells = new WFC_Cell[this.width*this.height];

        for (int i = 0; i < this.Cells.length; i++){

            this.Cells[i] = new WFC_Cell(this.TileSet);

        }

    }

    public WFC_Cell getCell(int x,int y){

        return this.Cells[x + y * this.width];

    }

    public WFC_Cell[] getSurrounding(){

        return null;

    }
}
