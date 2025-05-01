package com.rpg.main.entity.entites;

import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.entity.Entity;
import com.rpg.main.graphics.particle.ParticleHandler;
import com.rpg.main.graphics.particle.particles.PlainParticle;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.math.vector.Vector3;
import com.rpg.main.util.Time;

public class SimpleEntity extends Entity {
    float rotate;

    public SimpleEntity(Vector2 pos, Polygon hitbox) {
        super(pos, hitbox);
    }

    @Override
    public void update() {
        move(getVelocity().scale((float) Time.deltaTime));
        if (velocity.getY() < 10) acc(new Vector2(0, (float) Time.deltaTime));

        if((int)(Math.random()*10)==0) {
            ParticleHandler.addParticle(new PlainParticle(
                    getPos().add(new Vector2(50-(float)(Math.random()*100),50-(float)(Math.random()*100))),
                    new Vector2(-5-(float)Math.random()*5,-5-(float)Math.random()*5),new Vector3(255,0,0),50));
        }

//        rotate = rotate % 360;
//        if (velocity.getX() > 0) rotate += (float) (5 * Time.deltaTime);
//        if (velocity.getX() < 0) rotate -= (float) (5 * Time.deltaTime);
//        if ((int) rotate % 90 != 0 && velocity.getX() == 0) {
//            if ((int) rotate % 45 == 0) rotate -= (float) (5 * Time.deltaTime);
//            rotate += (float) (5 * (Math.signum((rotate % 90) - 45)) * Time.deltaTime);
//            move(new Vector2(5 * ((int) Math.signum((rotate % 90) - 45)), 0).scale((float) Time.deltaTime));
//        }
//        getHitbox().setTransform(new Matrix3(new float[][]{
//                {(float) Math.cos(Math.toRadians(rotate)), -(float) Math.sin(Math.toRadians(rotate)), 0},
//                {(float) Math.sin(Math.toRadians(rotate)), (float) Math.cos(Math.toRadians(rotate)), 0},
//                {0, 0, 1}
//        }));
    }

    @Override
    public void render(GL2 gl) {
        hitbox.renderPolygon(gl);
    }
}
