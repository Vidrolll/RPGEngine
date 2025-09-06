package dev.swirlingskies.scene;

import dev.swirlingskies.graphics.Texture;
import dev.swirlingskies.tile.Tile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Scene {
    public final static int SCENE_SIZE = 64;
    public final static int TILE_SIZE = 50;
    private final Tile[] tiles = new Tile[SCENE_SIZE*SCENE_SIZE];

    Texture grassText = new Texture("resources/textures/tiles/grass.png");

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
        for(int x = 0; x < SCENE_SIZE; x++) {
            for(int y = 0; y < SCENE_SIZE; y++) {
                Tile tile = tiles[y*SCENE_SIZE+x];
                if (tile != null) tile.render();
                else {
                    glColor3f(1,1,1);
                    glEnable(GL_TEXTURE_2D);
                    grassText.bind();
                    glBegin(GL_QUADS);
                    glTexCoord2f(0,0); glVertex2f(x*TILE_SIZE,y*TILE_SIZE);
                    glTexCoord2f(1,0); glVertex2f(x*TILE_SIZE+TILE_SIZE,y*TILE_SIZE);
                    glTexCoord2f(1,1); glVertex2f(x*TILE_SIZE+TILE_SIZE,y*TILE_SIZE+TILE_SIZE);
                    glTexCoord2f(0,1); glVertex2f(x*TILE_SIZE,y*TILE_SIZE+TILE_SIZE);
                    glEnd();
                    grassText.unbind();
                }
            }
        }
    }
    public Tile[] getTiles() {
        return tiles;
    }
}
