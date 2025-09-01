package dev.swirlingskies;

import dev.swirlingskies.entity.Entity;
import dev.swirlingskies.graphics.Camera;
import dev.swirlingskies.graphics.Window;
import dev.swirlingskies.player.Controller;
import dev.swirlingskies.player.Player;
import dev.swirlingskies.scene.Scene;
import dev.swirlingskies.tile.tiles.TestTile;
import dev.swirlingskies.util.Input;
import dev.swirlingskies.util.TaskManager;
import dev.swirlingskies.util.Time;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Objects;

import static dev.swirlingskies.util.Input.*;
import static dev.swirlingskies.util.Time.getDeltaTime;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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

        for(int i = 0; i < 512; i++) {
            scene.addTile(new TestTile(new Vector2i(i,0)));
        }
        for(int i = 0; i < 512; i++) {
            scene.addTile(new TestTile(new Vector2i(0,i)));
        }
        for(int i = 0; i < 512; i++) {
            scene.addTile(new TestTile(new Vector2i(i,511)));
        }
        for(int i = 0; i < 512; i++) {
            scene.addTile(new TestTile(new Vector2i(511,i)));
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
        controller.input();
        player.update();
    }

    public void render() {
        scene.render();
        player.render();
    }

    public static void main(String[] args) {
        new Game();
    }
}
