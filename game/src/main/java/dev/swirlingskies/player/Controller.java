package dev.swirlingskies.player;

import dev.swirlingskies.entity.Entity;
import dev.swirlingskies.scene.Scene;
import dev.swirlingskies.util.Collision2D;
import dev.swirlingskies.util.Input;
import org.joml.Vector2f;

import static dev.swirlingskies.scene.Scene.SCENE_SIZE;
import static dev.swirlingskies.scene.Scene.TILE_SIZE;
import static dev.swirlingskies.util.Time.getDeltaTime;
import static org.lwjgl.glfw.GLFW.*;

public class Controller {
    private final Entity entity;
    private final Scene scene;

    private final boolean[] keyDown;

    public Controller(Entity entity, Scene scene) {
        this.entity = entity;
        this.scene = scene;
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
        int minX = (int)Math.floor(entity.getPosition().x/TILE_SIZE)-1,
                minY = (int)Math.floor(entity.getPosition().y/TILE_SIZE)-1,
                maxX = (int)Math.ceil((entity.getPosition().x+entity.getHitbox().x)/TILE_SIZE)+1,
                maxY = (int)Math.ceil((entity.getPosition().y+entity.getHitbox().y)/TILE_SIZE)+1;
        if(minX < 0) minX = 0; if(minY < 0) minY = 0;
        if(maxX > SCENE_SIZE-1) maxX = SCENE_SIZE-1;
        if(maxY > SCENE_SIZE-1) maxY = SCENE_SIZE-1;
        for(int x = minX; x < maxX; x++) {
            for(int y = minY; y < maxY; y++) {
                if(scene.getTile(x,y)==null||!scene.getTile(x,y).isSolid()) continue;
                Vector2f wallSize = new Vector2f(TILE_SIZE,TILE_SIZE);
                Vector2f wallPos = new Vector2f(x,y).mul(wallSize);
                Vector2f mtv = new Vector2f();
                if (Collision2D.overlapVector(entity.getPosition(), entity.getHitbox(), wallPos, wallSize, mtv).lengthSquared() > 0f) {
                    entity.getPosition().add(mtv); // separate
                    if (mtv.x != 0) entity.getVelocity().x = 0;
                    if (mtv.y != 0) entity.getVelocity().y = 0;
                }
            }
        }
    }
}
