package com.rpg.main.graphics.particle.particles;

import com.jogamp.opengl.GL2;
import com.rpg.main.graphics.particle.Particle;
import com.rpg.main.graphics.particle.ParticleHandler;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.math.vector.Vector3;
import com.rpg.main.util.Time;

public class PlainParticle extends Particle {
    public PlainParticle(Vector2 position, Vector2 velocity, Vector3 color, float lifeSpan) {
        super(position, velocity, color, lifeSpan);
    }

    @Override
    public void update() {
        position.add(velocity.scale((float)Time.deltaTime));
        lifeSpan-=(float)Time.deltaTime;
        if(lifeSpan <= 0) ParticleHandler.removeParticle(this);
    }

    @Override
    public void render(GL2 gl) {
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glColor4f(color.getX(),color.getY(),color.getZ(),1-1.0f/lifeSpan);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(position.getX(),position.getY());
        gl.glVertex2f(position.getX()+10,position.getY());
        gl.glVertex2f(position.getX()+10,position.getY()+10);
        gl.glVertex2f(position.getX(),position.getY()+10);
        gl.glEnd();
        gl.glFlush();
    }
}
