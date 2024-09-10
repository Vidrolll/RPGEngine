package com.rpg.main.waveFunctionCollapse;

public class NoTileFitsCell extends RuntimeException {
    public NoTileFitsCell(String message) {
        super("WFC - No Tile Fits: " + message);
    }
}
