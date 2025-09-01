package dev.swirlingskies.player;

import dev.swirlingskies.entity.Entity;
import dev.swirlingskies.util.Input;

import static org.lwjgl.glfw.GLFW.*;

public class Controller {
    private final Entity entity;

    private final boolean[] keyDown;

    public Controller(Entity entity) {
        this.entity = entity;
        keyDown = new boolean[4];
    }

    public void input() {
        keyDown[0] = Input.isKeyDown(GLFW_KEY_A);
        keyDown[1] = Input.isKeyDown(GLFW_KEY_D);
        keyDown[2] = Input.isKeyDown(GLFW_KEY_W);
        keyDown[3] = Input.isKeyDown(GLFW_KEY_S);
        if(keyDown[0]) entity.setVelocity(entity.getVelocity().setComponent(0,-1));
        if(keyDown[1]) entity.setVelocity(entity.getVelocity().setComponent(0,1));
        if(keyDown[2]) entity.setVelocity(entity.getVelocity().setComponent(1,-1));
        if(keyDown[3]) entity.setVelocity(entity.getVelocity().setComponent(1,1));
        if(!keyDown[0]&&!keyDown[1]) entity.setVelocity(entity.getVelocity().setComponent(0,0));
        if(!keyDown[2]&&!keyDown[3]) entity.setVelocity(entity.getVelocity().setComponent(1,0));
        collisionCheck();
    }

    private void collisionCheck() {
        //To be implemented
    }
}
