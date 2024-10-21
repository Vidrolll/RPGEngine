package com.rpg.main.entity;

import com.jogamp.opengl.GL2;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.Vector;
import com.rpg.main.opengl.Graphics;

public class Player extends Entity {
    public Player() {
        hitbox = new Polygon(new Vector(500,500),new Vector(0,50), new Vector(0,-50),new Vector(100,-50),new Vector(100,50));
    }

    @Override
    public void update() {
        getHitbox().move(velocity);
        pos.add(velocity);
    }

    @Override
    public void render(GL2 gl) {
        Graphics.drawPoly(gl,getHitbox().getVertices());
    }
}
