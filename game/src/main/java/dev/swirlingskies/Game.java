package dev.swirlingskies;

import dev.swirlingskies.graphics.Window;
import dev.swirlingskies.util.Input;
import dev.swirlingskies.util.TaskManager;
import dev.swirlingskies.util.Time;

import java.util.Objects;

import static dev.swirlingskies.util.Input.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    private final Window window = new Window(this);

    public Game() {
        TaskManager.init(Runtime.getRuntime().availableProcessors() - 1);
        window.init();
        Input.init(window.getWindow());
        Time.init();
        loop();
    }

    public void loop() {
        glOrtho(0,window.getWindowDimensions().x,window.getWindowDimensions().y,0,-1,1);
        glClearColor(0.1f, 0.1f, 0.12f, 1f);
        TaskManager.runAsync(() -> {
           while(!Thread.currentThread().isInterrupted()) {
               System.out.println("test");
           }
        });
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

    }

    public void render() {
        glColor3f(1,1,1);
        glBegin(GL_POLYGON);
        glVertex2f(50,50);
        glVertex2f(100,50);
        glVertex2f(100,100);
        glVertex2f(50,100);
        glEnd();
    }

    public static void main(String[] args) {
        new Game();
    }
}
