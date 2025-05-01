package com.rpg.main.math.noise.waveFunctionCollapse;

public class NoTileFitsCell extends RuntimeException {
    /**when a cell has no possible tiles
     *
     * @param message
     */
    public NoTileFitsCell(String message) {
        super("WFC - No Tile Fits: " + message);
    }
}
