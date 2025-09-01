package dev.swirlingskies.tile;

import org.joml.Vector2i;

public abstract class Tile {
    protected Vector2i position;

    protected boolean solid;

    public Tile(Vector2i position) {
        this.position = position;
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
