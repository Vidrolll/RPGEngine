package dev.swirlingskies;

import dev.swirlingskies.entity.Entity;
import dev.swirlingskies.graphics.Window;
import dev.swirlingskies.player.Controller;
import dev.swirlingskies.player.Player;
import dev.swirlingskies.util.Input;
import dev.swirlingskies.util.TaskManager;
import dev.swirlingskies.util.Time;
import org.joml.Vector2f;

import java.util.Objects;

import static dev.swirlingskies.util.Input.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    private final Window window = new Window();

    private Entity player;
    private Controller controller;

    public Game() {
        window.init();
        TaskManager.init(Runtime.getRuntime().availableProcessors() - 1);
        Input.init(window.getWindow());
        Time.init();

        player = new Player(new Vector2f(50,50),new Vector2f(50,50));
        controller = new Controller(player);

        loop();
    }

    public void loop() {
        glOrtho(0,window.getWindowDimensions().x,window.getWindowDimensions().y,0,-1,1);
        glClearColor(0.1f, 0.1f, 0.12f, 1f);
        while (!glfwWindowShouldClose(window.getWindow())) {
            glClear(GL_COLOR_BUFFER_BIT);

            Time.update();
            update();
            render();

            if(isKeyPressed(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(window.getWindow(), true);
            }

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
        controller.input();
        player.update();
    }

    public void render() {
        player.render();
    }

    public static void main(String[] args) {
        new Game();
    }
}
