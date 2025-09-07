package dev.swirlingskies.tile;

import static dev.swirlingskies.scene.Scene.*;

public class TileMask {
    public final int[] TILE_MASK_ID = {
            0,0,1,1,0,0,1,1,2,2,3,4,2,2,3,4,
            5,5,6,6,5,5,7,7,8,8,9,10,8,8,11,12,
            0,0,1,1,0,0,1,1,2,2,3,4,2,2,3,4,
            5,5,6,6,5,5,7,7,8,8,9,10,8,8,11,12,
            13,13,14,14,13,13,14,14,15,15,16,17,15,15,16,17,
            18,18,19,19,18,18,20,20,21,21,22,23,21,21,24,25,
            13,13,14,14,13,13,14,14,26,26,27,28,26,26,27,28,
            18,18,19,19,18,18,20,20,29,29,30,31,29,29,32,33,
            0,0,1,1,0,0,1,1,2,2,3,4,2,2,3,4,
            5,5,6,6,5,5,7,7,8,8,9,10,8,8,11,12,
            0,0,1,1,0,0,1,1,2,2,3,4,2,2,3,4,
            5,5,6,6,5,5,7,7,8,8,9,10,8,8,11,12,
            13,13,14,14,13,13,14,14,15,15,16,17,15,15,16,17,
            34,34,35,35,34,34,36,36,37,37,38,39,37,37,40,41,
            13,13,14,14,13,13,14,14,26,26,27,28,26,26,27,28,
            34,34,35,35,34,34,36,36,42,42,43,44,42,42,45,46
    };

    public boolean same(Tile[] tiles, int x, int y, int nx, int ny, Tile tileType) {
        if (ny < 0 || ny >= SCENE_SIZE || nx < 0 || nx >= SCENE_SIZE) return false;
        return tiles[ny*SCENE_SIZE+nx].getClass().equals(tileType.getClass());
    }


    public int mask8(Tile[] tiles, int x, int y, Tile tileType) {
        int m = 0;
        if (same(tiles,x,y,x-1,y-1,tileType)) m |= 1;      // NW
        if (same(tiles,x,y,x,y-1,tileType)) m |= 1<<1;         // N
        if (same(tiles,x,y,x+1,y-1,tileType)) m |= 1<<2;   // NE
        if (same(tiles,x,y,x-1,y,tileType)) m |= 1<<3;         // W
        if (same(tiles,x,y,x+1,y,tileType)) m |= 1<<4;         // E
        if (same(tiles,x,y,x-1,y+1,tileType)) m |= 1<<5;   // SW
        if (same(tiles,x,y,x,y+1,tileType)) m |= 1<<6;         // S
        if (same(tiles,x,y,x+1,y+1,tileType)) m |= 1<<7;   // SE
        return m;
    }
}
