package dev.swirlingskies.scene;

import dev.swirlingskies.tile.Tile;

import java.util.ArrayList;

public class Scene {
    private final int sceneSize = 512;
    private final Tile[] tiles = new Tile[sceneSize*sceneSize];

    public void addTile(Tile tile, int x, int y) {
        tiles[y*sceneSize+x] = tile;
    }
    public void removeTile(int x, int y) {
        tiles[y*sceneSize+x] = null;
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
