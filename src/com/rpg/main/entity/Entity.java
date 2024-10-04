package com.rpg.main.entity;

import com.jogamp.opengl.GL2;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.Vector;

public abstract class Entity {
    //Entity variables
    protected Vector pos, velocity;
    protected Polygon[] hitbox;

    /**
     * Creates an entity with a defined hitbox array.
     * @param hitbox (Polygon[]) The defined hitbox array to give to the created entity.
     */
    public Entity(Polygon...hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Default constructor. Creates an entity with a null hitbox array to it.
     */
    public Entity() {
        this((Polygon)null);
    }

    /**
     * Returns the current hitbox array of the entity.
     * @return (Polygon[]) The current hitbox array.
     */
    public Polygon[] getHitbox() {
        return hitbox;
    }

    /**
     * Returns the current position of the entity.
     * @return (Vector) The current position of the entity.
     */
    public Vector getPos() {
        return pos;
    }

    /**
     * Returns the current velocity of the entity.
     * @return (Vector) The current velocity of the entity.
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * Sets the position of the entity.
     * @param pos (Vector) The new position to move the entity to.
     */
    public void setPosition(Vector pos) {
        this.pos = pos;
    }

    /**
     * Sets the velocity of the entity.
     * @param velocity (Vector) The new velocity to set to.
     */
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    /**
     * The update function of the entity. Called every single game update.
     */
    public abstract void update();

    /**
     * The render function of the entity. Called every single game render.
     * @param gl (GL2) The OpenGL object to render to.
     */
    public abstract void render(GL2 gl);
}
