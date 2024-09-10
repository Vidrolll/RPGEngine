package com.rpg.main.waveFunctionCollapse;

import com.rpg.main.math.Vector2;

// Grid to hold Cells
public class WFC_Grid implements Cloneable{

    public int width, height;
    public int tileSockets;
    private int size;
    private WFC_Cell[] Cells;
    public WFC_Tile[] TileSet;
    /** Class that hold WFC_Cell in a grid
     *
     * @param w width of grid
     * @param h height of grid
     * @param socketCount number of sockets per side on Tiles
     * @param tileSet tiles the grid cells use
    * */
    public WFC_Grid(int w, int h, int socketCount, WFC_Tile[] tileSet){

        this.width = w;
        this.height = h;
        this.size = w * h;
        this.tileSockets = socketCount;
        this.TileSet = tileSet;

        // initialize Cell Grid
        this.Cells = new WFC_Cell[this.size];

        for (int i = 0; i < this.size; i++){

            this.Cells[i] = new WFC_Cell(this.TileSet);

        }

    }

    public int indexFromXY(int x, int y){

        return x + y * this.width;

    }

    public Vector2 XYFromIndex(int index){

        int x = index % this.width;
        int y = (index - x) / this.width;
        return new Vector2(x, y);

    }

    public WFC_Cell getCell(int x,int y){

        int index = indexFromXY(x, y);
        if (index >= 0 && index < this.size) {
            WFC_Cell cell = this.Cells[index];
            return cell;
        }
        else {

            return null;

        }

    }

    /** replaces a Cell in the grid
     *
     * @param x x position of cell in grid
     * @param y y position of cell in grid
     * @param cell new cell to replace old cell
    * */
    public void changeCell(int x, int y, WFC_Cell cell){

        int index = indexFromXY(x, y);
        this.Cells[index] = cell;

    }

    public WFC_Cell[] getSurrounding(int x, int y){

        return new WFC_Cell[]{
                getCell(x, y-1),
                getCell(x+1, y),
                getCell(x, y+1),
                getCell(x-1, y)};

    }

    @Override
    public WFC_Grid clone() {

        try {

            return (WFC_Grid) super.clone();

        }

        catch (CloneNotSupportedException e){

            return null;

        }

    }

}
