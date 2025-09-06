package dev.swirlingskies.tile.tiles;

import dev.swirlingskies.scene.Scene;
import dev.swirlingskies.tile.Tile;
import org.joml.Vector2i;

import static dev.swirlingskies.scene.Scene.TILE_SIZE;
import static org.lwjgl.opengl.GL11.*;

public class TestTile extends Tile {
    public TestTile(Vector2i position, Scene scene) {
        super(position,scene);
        solid = true;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        glColor3f(0,0,0);
        glBegin(GL_POLYGON);
        glVertex2f(position.x*TILE_SIZE,position.y*TILE_SIZE);
        glVertex2f(position.x*TILE_SIZE+TILE_SIZE,position.y*TILE_SIZE);
        glVertex2f(position.x*TILE_SIZE+TILE_SIZE,position.y*TILE_SIZE+TILE_SIZE);
        glVertex2f(position.x*TILE_SIZE,position.y*TILE_SIZE+TILE_SIZE);
        glEnd();
    }
}
