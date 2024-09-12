package com.rpg.main.gui;

import com.rpg.main.Game;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    //Window width and height.
    static int width, height;

    //JFrame to render.
    static JFrame frame;

    //Main instance to run with.
    static Game main;

    /**
     * Creates a new main window for the game to render to.
     * @param main (Game) A game object to use as a renderer to the window.
     */
    public MainWindow(Game main) {
        maxSize();
        MainWindow.main = main;
        frame = new JFrame("RPG Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(width,height));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(main);
        main.start();
    }

    /**
     * Changes the dimensions of the window to fit the screen size.
     */
    public void maxSize() {
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    /**
     * Changes the window size to a new inputted size.
     * @param newWidth (int) New width of the window.
     * @param newHeight (int) New height of the window.
     */
    public static void changeSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        frame.setSize(new Dimension(width,height));
    }

    /**
     * Returns the size of the current window.
     * @return (Dimension) Size of the window.
     */
    public static Dimension getSize() {
        return frame.getSize();
    }

    /**
     * Reloads the window if anything has been changed that requires a refresh.
     */
    public static void reloadWindow() {
        frame.dispose();
        frame = new JFrame("RPG Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(width,height));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(main);
    }
}
