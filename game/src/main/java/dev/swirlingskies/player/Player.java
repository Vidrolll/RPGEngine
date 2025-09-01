package dev.swirlingskies.player;

import dev.swirlingskies.entity.Entity;
import org.joml.Vector2f;

import static dev.swirlingskies.util.Time.*;
import static org.lwjgl.opengl.GL11.*;

public class Player extends Entity {
    public Player(Vector2f position, Vector2f hitbox) {
        super(position, hitbox);
    }

    @Override
    public void render() {
        glColor3f(1,1,1);
        glBegin(GL_POLYGON);
        glVertex2f(position.x,position.y);
        glVertex2f(position.x+hitbox.x,position.y);
        glVertex2f(position.x+hitbox.x,position.y+hitbox.y);
        glVertex2f(position.x,position.y+hitbox.y);
        glEnd();
    }

    @Override
    public void update() {
        if(velocity.length()!=0) position.add(velocity.normalize().mul(5).mul((float)getDeltaTime()));
    }
}
