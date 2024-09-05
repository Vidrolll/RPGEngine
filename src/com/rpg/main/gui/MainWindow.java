package com.rpg.main.gui;

import com.rpg.main.Game;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    static int width, height;

    static JFrame frame;

    static Game main;

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
    public void maxSize() {
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;
    }
    public static void changeSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        frame.setSize(new Dimension(width,height));
    }
    public static Dimension getSize() {
        return frame.getSize();
    }
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
