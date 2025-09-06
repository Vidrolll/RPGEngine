package dev.swirlingskies.tile;

import static dev.swirlingskies.scene.Scene.*;

public class TileMask {
    public final int[] TILE_MASK_ID = {
            0, 14, 0, 14, 28, 15, 28, 36, 0, 14, 0, 14, 28, 15, 28, 36,
            7, 8, 7, 8, 1, 32, 1, 25, 7, 8, 7, 8, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x, x, x, x, x, x, x, x,
    };

    public boolean same(Tile[] tiles, int x, int y, int nx, int ny) {
        if (ny < 0 || ny >= SCENE_SIZE || nx < 0 || nx >= SCENE_SIZE) return false;
        return tiles[ny*SCENE_SIZE+nx] == tiles[y*SCENE_SIZE+x];
    }

    public int mask8(Tile[] tiles, int x, int y) {
        int m = 0;
        if (same(tiles,x,y,x,y-1)) m |= 1;            // N
        if (same(tiles,x,y,x+1,y-1)) m |= 1<<1;   // NE
        if (same(tiles,x,y,x+1,y)) m |= 1<<2;         // E
        if (same(tiles,x,y,x+1,y+1)) m |= 1<<3;   // SE
        if (same(tiles,x,y,x,y+1)) m |= 1<<4;         // S
        if (same(tiles,x,y,x-1,y+1)) m |= 1<<5;   // SW
        if (same(tiles,x,y,x-1,y)) m |= 1<<6;         // W
        if (same(tiles,x,y,x-1,y-1)) m |= 1<<7;   // NW
        return m;
    }
}
