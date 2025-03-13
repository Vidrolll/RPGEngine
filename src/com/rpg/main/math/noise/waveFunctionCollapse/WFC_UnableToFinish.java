package com.rpg.main.math.noise.waveFunctionCollapse;

public class WFC_UnableToFinish extends RuntimeException {
    /**When the wave function collapse is unable to finish due to not having enough iterations
     *
     * @param grid grid being collapsed at the time
     * @param limit the current limit for the collapse function
     */
    public WFC_UnableToFinish(WFC_Grid grid, int limit) {
        super("WFC-Unable to finish size " + grid.getSize() + " grid with " + limit + " iterations");
    }
}
