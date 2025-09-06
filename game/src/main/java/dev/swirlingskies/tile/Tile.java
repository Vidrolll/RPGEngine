package dev.swirlingskies.tile;

import dev.swirlingskies.scene.Scene;
import org.joml.Vector2i;

public abstract class Tile {
    protected Vector2i position;

    protected boolean solid;

    protected Scene scene;

    public Tile(Vector2i position, Scene scene) {
        this.position = position;
        this.scene = scene;
    }

    public abstract void update();
    public abstract void render();

    public boolean isSolid() {
        return solid;
    }

    public Vector2i getPosition() {
        return position;
    }
}
