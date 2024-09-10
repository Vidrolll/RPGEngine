package com.rpg.main.waveFunctionCollapse;

public class WFC_UnableToFinish extends RuntimeException {
    public WFC_UnableToFinish(WFC_Grid grid, int limit) {
        super("WFC-Unable to finish size " + grid.getSize() + " grid with " + limit + " iterations");
    }
}
