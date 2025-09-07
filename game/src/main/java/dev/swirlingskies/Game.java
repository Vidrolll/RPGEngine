package dev.swirlingskies;

import dev.swirlingskies.entity.Entity;
import dev.swirlingskies.graphics.Camera;
import dev.swirlingskies.graphics.Texture;
import dev.swirlingskies.graphics.Window;
import dev.swirlingskies.player.Controller;
import dev.swirlingskies.player.Player;
import dev.swirlingskies.scene.Scene;
import dev.swirlingskies.tile.Tile;
import dev.swirlingskies.tile.TileMask;
import dev.swirlingskies.tile.tiles.TestTile;
import dev.swirlingskies.util.Input;
import dev.swirlingskies.util.TaskManager;
import dev.swirlingskies.util.Time;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Objects;

import static dev.swirlingskies.scene.Scene.SCENE_SIZE;
import static dev.swirlingskies.scene.Scene.TILE_SIZE;
import static dev.swirlingskies.util.Input.*;
import static dev.swirlingskies.util.Time.getDeltaTime;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Game {
    private final Window window = new Window();

    private Scene scene;
    private Entity player;
    private Controller controller;

    Camera camera;

    public Game() {
        window.init();
        TaskManager.init(Runtime.getRuntime().availableProcessors() - 1);
        Input.init(window.getWindow());
        Time.init();

        camera = new Camera();
        scene = new Scene();

        camera.setZoom(2f);

        for(int i = 0; i < SCENE_SIZE; i++) {
            scene.addTile(new TestTile(new Vector2i(i,0), scene));
        }
        for(int i = 0; i < SCENE_SIZE; i++) {
            scene.addTile(new TestTile(new Vector2i(0,i), scene));
        }
        for(int i = 0; i < SCENE_SIZE; i++) {
            scene.addTile(new TestTile(new Vector2i(i,SCENE_SIZE-1), scene));
        }
        for(int i = 0; i < SCENE_SIZE; i++) {
            scene.addTile(new TestTile(new Vector2i(SCENE_SIZE-1,i), scene));
        }

        player = new Player(new Vector2f(50,50),new Vector2f(50,50));
        controller = new Controller(player,scene);

        loop();
    }

    public void loop() {
        glOrtho(0,window.getWindowDimensions().x,window.getWindowDimensions().y,0,-1,1);
        glClearColor(0.1f, 0.1f, 0.12f, 1f);
        while (!glfwWindowShouldClose(window.getWindow())) {
            glClear(GL_COLOR_BUFFER_BIT);
            camera.begin();

            Time.update();
            update();
            render();

            if(isKeyPressed(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(window.getWindow(), true);
            }

            camera.end();
            Input.publishSnapshot();
            Input.endFrame();
            glfwSwapBuffers(window.getWindow());
            glfwPollEvents();
        }
        glfwDestroyWindow(window.getWindow());
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        TaskManager.shutdown();
    }

    public void update() {
        float dist = camera.getPosition().distance(
                new Vector2f(player.getPosition().x-(1920/2f),player.getPosition().y-(1080/2f)));
        Vector2f moveVec = new Vector2f(player.getPosition().x-(1920/2f),player.getPosition().y-(1080/2f))
                .sub(camera.getPosition()).normalize().mul((float)getDeltaTime()).mul(dist/5f);
        camera.getPosition().add(moveVec);

        scene.update();
        player.update();
        controller.input();

        if(isKeyPressed(GLFW_KEY_SPACE)) {
            tile++;
            tilemap = new int[]{
                    0,0,0,0,0,
                    0,tile & 1,(tile >> 1) & 1,(tile >> 2) & 1,0,
                    0,(tile >> 3) & 1,1,(tile >> 4) & 1,0,
                    0,(tile >> 5) & 1,(tile >> 6) & 1,(tile >> 7) & 1,0,
                    0,0,0,0,0,
            };
        }
    }

    TileMask mask = new TileMask();
    Texture testTexture;

    public void render() {
        scene.render();
        player.render();
        if(testTexture == null) testTexture = new Texture("resources/textures/tiles/testautotile.png");
        for(int x = 1; x < 4; x++) {
            for(int y = 1; y < 4; y++) {
                if(tilemap[y*5+x]==0) continue;
                int id = mask.TILE_MASK_ID[mask8(x,y)];
                if(x==2&&y==2)System.out.println(id);
                float tw = 1.0f / 12f, th = 1.0f / 4f;
                float tx = tw * ((id+1)%12), ty = 1 - (th * ((id+1)/12));
                glColor3f(1,1,1);
                glEnable(GL_TEXTURE_2D);
                testTexture.bind();
                glBegin(GL_QUADS);
                glTexCoord2f(tx,ty); glVertex2f(x*TILE_SIZE,y*TILE_SIZE);
                glTexCoord2f(tx+tw,ty); glVertex2f(x*TILE_SIZE+TILE_SIZE,y*TILE_SIZE);
                glTexCoord2f(tx+tw,ty-th); glVertex2f(x*TILE_SIZE+TILE_SIZE,y*TILE_SIZE+TILE_SIZE);
                glTexCoord2f(tx,ty-th); glVertex2f(x*TILE_SIZE,y*TILE_SIZE+TILE_SIZE);
                glEnd();
                testTexture.unbind();
            }
        }
    }

    int tile = 0;
    int[] tilemap = {
            0,0,0,0,0,
            0,0,0,0,0,
            0,0,1,0,0,
            0,0,0,0,0,
            0,0,0,0,0,
    };

    public boolean same(int x, int y, int nx, int ny) {
        if (ny < 0 || ny >= 5 || nx < 0 || nx >= 5) return false;
        return tilemap[ny*5+nx] == tilemap[y*5+x];
    }

    public int mask8(int x, int y) {
        int m = 0;
        if (same(x,y,x-1,y-1)) m |= 1;      // NW
        if (same(x,y,x,y-1)) m |= 1<<1;         // N
        if (same(x,y,x+1,y-1)) m |= 1<<2;   // NE
        if (same(x,y,x-1,y)) m |= 1<<3;         // W
        if (same(x,y,x+1,y)) m |= 1<<4;         // E
        if (same(x,y,x-1,y+1)) m |= 1<<5;   // SW
        if (same(x,y,x,y+1)) m |= 1<<6;         // S
        if (same(x,y,x+1,y+1)) m |= 1<<7;   // SE
        if(x==2&&y==2)System.out.println(m);
        return m;
    }

    public static void main(String[] args) {
        new Game();
    }
}
