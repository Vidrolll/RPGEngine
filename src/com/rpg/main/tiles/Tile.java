package com.rpg.main.tiles;

import com.rpg.main.math.Polygon;

public abstract class Tile {
    protected Polygon hitbox;

    protected Tile(Polygon hitbox) {
        this.hitbox = hitbox;
    }
}
