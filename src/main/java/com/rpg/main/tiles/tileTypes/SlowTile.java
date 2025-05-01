package com.rpg.main.tiles.tileTypes;

import com.jogamp.opengl.GL2;
import com.rpg.main.entity.Entity;
import com.rpg.main.math.Polygon;
import com.rpg.main.tiles.Tile;

public class SlowTile extends Tile {
    public SlowTile(Polygon hitbox) {
        super(hitbox);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GL2 gl) {
        hitbox.renderPolygon(gl);
    }

    @Override
    public void interact(Entity entity) {
        entity.setVelX(Math.signum(entity.getVelocity().getX()) * 2);
    }
}
