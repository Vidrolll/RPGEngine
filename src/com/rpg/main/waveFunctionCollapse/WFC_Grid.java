package com.rpg.main.waveFunctionCollapse;

import com.rpg.main.math.Vector2;

import java.io.Serializable;

// Grid to hold Cells
public class WFC_Grid implements Cloneable {

    public int width, height;
    public int tileSockets;
    private int size;
    public WFC_Cell[] Cells;
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

    /**get the index for a cell in the 1d array
     *
     * @param x
     * @param y
     * @return index for a cell
     */
    public int indexFromXY(int x, int y){

        return x + y * this.width;

    }

    /**get the x and y position for a cell based on an index
     *
     * @param index index of the cell
     * @return Vector2 representing x and y of the cell in the grid
     */
    public Vector2 XYFromIndex(int index){

        int x = index % this.width;
        int y = (index - x) / this.width;
        return new Vector2(x, y);

    }

    /**gets a cell based of position in the grid
     *
     * @param x
     * @param y
     * @return a cell from the grid
     */
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

    /**gets cells in the cardinal directions from the specified position
     *
     * @param x
     * @param y
     * @return
     */
    public WFC_Cell[] getSurrounding(int x, int y){

        return new WFC_Cell[]{
                getCell(x, y-1),
                getCell(x+1, y),
                getCell(x, y+1),
                getCell(x-1, y)};

    }


    /**clones the object
     *
     * @return
     */
    public WFC_Grid copy(){

        return this.clone();

    }

    @Override
    protected WFC_Grid clone() {

        try {

            WFC_Grid copy = (WFC_Grid) super.clone();

            for (int i = 0; i < this.size; i++){

                copy.Cells[i] = this.Cells[i].clone();

            }

            return copy;

        }

        catch (CloneNotSupportedException e){

            return null;

        }

    }

    /**returns the number of elements in this grid
     *
     * @return
     */
    public int getSize() {
        return this.size;
    }
}
