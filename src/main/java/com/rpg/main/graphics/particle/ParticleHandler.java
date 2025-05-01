package com.rpg.main.graphics.particle;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class ParticleHandler {
    private static final ArrayList<Particle> PARTICLES = new ArrayList<>();

    public static void addParticle(Particle particle) {
        PARTICLES.add(particle);
    }
    public static void removeParticle(Particle particle) {
        PARTICLES.remove(particle);
    }

    public static void update() {
        for(int i = 0; i < PARTICLES.size(); i++) PARTICLES.get(i).update();
    }
    public static void render(GL2 gl) {
        for(Particle p : PARTICLES) p.render(gl);
    }
}
