package com.rpg.main.graphics.particle;

import com.jogamp.opengl.GL2;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.math.vector.Vector3;

public abstract class Particle {
    protected Vector2 position,velocity,rotationalVelocity;
    protected Vector3 color;
    protected float lifeSpan;

    public Particle(Vector2 position, Vector2 velocity, Vector3 color, float lifeSpan) {
        this.position = position;
        this.velocity = velocity;
        this.color = color;
        this.lifeSpan = lifeSpan;
    }

    public abstract void update();
    public abstract void render(GL2 gl);
}
