package dev.swirlingskies.scene;

import dev.swirlingskies.tile.Tile;

import java.util.ArrayList;

public class Scene {
    public final static int SCENE_SIZE = 512;
    public final static int TILE_SIZE = 16;
    private final Tile[] tiles = new Tile[SCENE_SIZE*SCENE_SIZE];

    public void addTile(Tile tile) {
        tiles[tile.getPosition().y*SCENE_SIZE+tile.getPosition().x] = tile;
    }
    public void removeTile(int x, int y) {
        tiles[y*SCENE_SIZE+x] = null;
    }

    public Tile getTile(int x, int y) {
        return tiles[y*SCENE_SIZE+x];
    }

    public void update() {
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i] != null) tiles[i].update();
        }
    }
    public void render() {
        for (Tile tile : tiles) {
            if (tile != null) tile.render();
        }
    }
}
