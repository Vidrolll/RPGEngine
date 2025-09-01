package dev.swirlingskies.entity;

import org.joml.Vector2f;

public abstract class Entity {
    protected Vector2f position;
    protected Vector2f velocity;
    protected Vector2f hitbox;

    public Entity(Vector2f position, Vector2f hitbox) {
        this.position = position;
        this.hitbox = hitbox;
        this.velocity = new Vector2f(0);
    }

    public abstract void render();
    public abstract void update();

    public Vector2f getPosition() {
        return position;
    }
    public Vector2f getVelocity() {
        return velocity;
    }
    public Vector2f getHitbox() {
        return hitbox;
    }
    public void setPosition(Vector2f position) {
        this.position = position;
    }
    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
    public void setHitbox(Vector2f hitbox) {
        this.hitbox = hitbox;
    }
}
