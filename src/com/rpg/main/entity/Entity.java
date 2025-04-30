package com.rpg.main.entity;

import com.jogamp.opengl.GL2;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.tiles.Tile;

public abstract class Entity {
    //Entity variables
    protected Vector2 pos, velocity;
    protected Polygon hitbox;

    /**
     * Creates an entity with a defined hitbox and position.
     *
     * @param pos    (Vector2) The defined position to place the player at.
     * @param hitbox (Polygon) The defined hitbox to give to the created entity.
     */
    public Entity(Vector2 pos, Polygon hitbox) {
        this.hitbox = hitbox;
        this.pos = pos;
        velocity = new Vector2();
    }

    /**
     * Creates an entity with a defined hitbox.
     * @param hitbox (Polygon) The defined hitbox to give to the created entity.
     */
    public Entity(Polygon hitbox) {
        this(new Vector2(),hitbox);
    }

    /**
     * Default constructor. Creates an entity with a null hitbox to it.
     */
    public Entity() {
        this((Polygon)null);
    }

    /**
     * Returns the current hitbox of the entity.
     * @return (Polygon[]) The current hitbox.
     */
    public Polygon getHitbox() {
        return hitbox;
    }

    /**
     * Returns the current position of the entity.
     * @return (Vector) The current position of the entity.
     */
    public Vector2 getPos() {
        return pos;
    }

    /**
     * Returns the current velocity of the entity.
     * @return (Vector) The current velocity of the entity.
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Adds a vector to the position vector. Usually the velocity.
     *
     * @param vec (Vector2) The new vector to increase position by.
     */
    public void move(Vector2 vec) {
        this.pos = pos.add(vec);
        this.hitbox.setPos(getPos());
    }

    /**
     * Accelerates the entity by a Vector2 by increasing the velocity vector.
     *
     * @param vec (Vector2) The vector to accelerate by.
     */
    public void acc(Vector2 vec) {
        this.velocity = velocity.add(vec);
    }

    /**
     * Collides with an inputted tile.
     *
     * @param tile (Tile) The inputted tile to collide with.
     */
    public void collide(Tile tile) {
        Vector2 vec;
        if ((vec = this.hitbox.sat(tile.getHitbox())) != null) {
            setVelY(5);
            tile.interact(this);
            move(vec);
        }
    }

    /**
     * Sets the position of the entity.
     * @param pos (Vector) The new position to move the entity to.
     */
    public void setPosition(Vector2 pos) {
        this.pos = pos;
    }

    /**
     * Sets the velocity of the entity.
     * @param velocity (Vector) The new velocity to set to.
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Set just the X component of the velocity vector.
     *
     * @param velX (float) The new X component.
     */
    public void setVelX(float velX) {
        this.velocity.setX(velX);
    }

    /**
     * Set just the Y component of the velocity vector.
     *
     * @param velY (float) The new Y component.
     */
    public void setVelY(float velY) {
        this.velocity.setY(velY);
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
