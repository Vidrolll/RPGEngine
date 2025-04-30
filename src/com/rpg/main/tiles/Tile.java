package com.rpg.main.tiles;

import com.jogamp.opengl.GL2;
import com.rpg.main.entity.Entity;
import com.rpg.main.math.Polygon;

public abstract class Tile {
    //Hitbox of the tile
    protected Polygon hitbox;

    public Tile(Polygon hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Runs an update on the tile.
     */
    public abstract void update();

    /**
     * Runs a render update on the tile.
     *
     * @param gl (GL2) The OpenGL object to render with.
     */
    public abstract void render(GL2 gl);

    /**
     * A function that applies an effect on an inputted entity.
     *
     * @param entity (Entity) The given entity to apply the interaction on.
     */
    public abstract void interact(Entity entity);

    /**
     * Returns the current hitbox of the tile.
     *
     * @return (Polygon) The current hitbox of the tile.
     */
    public Polygon getHitbox() {
        return hitbox;
    }
}
