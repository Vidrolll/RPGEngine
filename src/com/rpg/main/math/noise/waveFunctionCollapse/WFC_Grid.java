package com.rpg.main.math.noise.waveFunctionCollapse;

import com.rpg.main.math.vector.Vector2;
import java.util.Objects;

// Grid to hold Cells
public class WFC_Grid implements Cloneable {

    public final int width, height;
    public int tileSockets;
    private final int size;
    public WFC_Cell[] Cells;
    public WFC_Tile[] TileSet;
    private boolean canBeDrawn;
    private boolean collapsed = false;
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

        this.canBeDrawn = true;
        // determine whether this grid can be drawn
        for (int i = 0; i < this.TileSet.length; i++){

            if (!Objects.isNull(this.TileSet[i])){

                if (!this.TileSet[i].canRender()){

                    this.canBeDrawn = false;
                    break;

                }

            }

        }

    }

    public boolean CanBeDrawn(){

        return this.canBeDrawn;

    }

    /**get the ids of tiles and returns them as an array
     *
     * @return returns null if the grid isn't collapsed
     */
    public int[] getIdGrid1D(){

        if (!this.collapsed) return null;

        int[] ret = new int[this.size];
        for (int i = 0; i < this.size; i++){

            ret[i] = this.Cells[i].possibleTiles[0].getId();

        }
        return ret;

    }

    /**get the ids of tiles and returns them as a 2d array
     *
     * @return returns null if the grid isn't collapsed
     */
    public int[][] getIdGrid2D(){

        if (!this.collapsed) return null;

        int[][] ret = new int[this.height][this.width];
        for (int i = 0; i < this.height; i++){

            for (int j = 0; j < this.width; j++){

                ret[i][j] = this.getCell(j, i).possibleTiles[0].getId();

            }

        }

        return ret;

    }

    /**get the index for a cell in the 1d array
     *
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
     * @return a cell from the grid
     */
    public WFC_Cell getCell(int x,int y){

        int index = indexFromXY(x, y);
        if (index >= 0 && index < this.size) {
            return this.Cells[index];
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
     * @return returns an array of cells
     */
    public WFC_Cell[] getSurrounding(int x, int y){

        return new WFC_Cell[]{
                getCell(x, y-1),
                getCell(x+1, y),
                getCell(x, y+1),
                getCell(x-1, y)};

    }

    /**
     * sets the state of the grid to collapsed
     * DOES NOT CHECK WHETHER THAT IS TRUE
     */
    public void makeCollapsed() {
        this.collapsed = true;
    }

    /**clones the object
     *
     * @return the clone
     */
    public WFC_Grid copy(){

        return this.clone();

    }

    @Override
    protected WFC_Grid clone(){

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

    /**gets the number of elements in this grid
     *
     * @return number of elements in this grid
     */
    public int getSize() {
        return this.size;
    }
}
