package com.levelCreator.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.rpg.main.Game;
import com.rpg.main.entity.entites.SimpleEntity;
import com.rpg.main.level.Level;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.player.PlayerController;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Main extends Game {
    Level level;

    Polygon poly;
    Polygon rect;
    ArrayList<Vector2> vert;
    Vector2 mouse = new Vector2(0, 0);

    public Main() {
        level = new Level(new PlayerController(new SimpleEntity(
                new Vector2(50, 50),
                new Polygon(new Vector2(0, 0), new Vector2(0, 0), new Vector2(50, 0),
                        new Vector2(50, 50), new Vector2(0, 50)))));
        CAMERA.lockOn(null);
    }

    public static void main(String[] args) {
        for (int jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++) {
            if (glfwJoystickPresent(jid)) {
                System.out.println("Joystick " + jid + " is present: " + glfwGetJoystickName(jid));
            }
        }
        new Main();
    }

    @Override
    public void draw(GL2 gl) {
        if (level != null) level.render(gl);
        if (poly != null) poly.renderPolygon(gl);
        if (rect != null) rect.renderPolygon(gl);
    }

    @Override
    public void drawGUI(GL2 gl) {
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(0, 800);
        gl.glVertex2f(1920, 800);
        gl.glVertex2f(1920, 1080);
        gl.glVertex2f(0, 1080);
        gl.glEnd();
        gl.glFlush();

        GLUT glut = new GLUT();
        gl.glRasterPos2d(900, 50);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, String.valueOf(mouse.getX()));
        gl.glRasterPos2d(900, 100);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, String.valueOf(mouse.getY()));
    }

    @Override
    public void update() {
        if (rect != null) {
            Vector2 vert = mouse.sub(rect.getPos());
            if (vert.equals(rect.getPos())) vert = vert.add(new Vector2(1, 1));
            rect = new Polygon(rect.getPos(),
                    new Vector2(0, 0), new Vector2(vert.getX(), 0),
                    new Vector2(vert.getX(), vert.getY()), new Vector2(0, vert.getY()));
        }
    }

    @Override
    public void input(KeyEvent e) {
        if (e.getEventType() == KeyEvent.EVENT_KEY_PRESSED) {
            if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ESCAPE)
                System.exit(0);
            if (e.getKeyCode() == KeyEvent.VK_Q) {
                if (poly == null) {
                    vert = new ArrayList<>();
                    vert.add(new Vector2(0, 0));
                    poly = new Polygon(new Vector2(mouse.getX(), mouse.getY()), vert.toArray(new Vector2[0]));
                } else {
                    vert.add(new Vector2(mouse.getX(), mouse.getY()).sub(poly.getPos()));
                    poly = new Polygon(poly.getPos(), vert.toArray(new Vector2[0]));
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_W && rect == null) {
                rect = new Polygon(new Vector2(mouse.getX(), mouse.getY()), new Vector2(0, 0));
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (rect != null) {
                    level.map.put("shape" + level.map.size(), rect);
                    rect = null;
                }
                if (poly != null) {
                    level.map.put("shape" + level.map.size(), poly);
                    poly = null;
                }
            }
        }
    }

    @Override
    public void input(MouseEvent e) {
        Vector2 mouse = CAMERA.getTransformMatrix().inverse().mul(new Vector2(e.getX(), e.getY()));
        if (e.getEventType() == MouseEvent.EVENT_MOUSE_DRAGGED)
            CAMERA.move(mouse.sub(this.mouse));
        if (e.getEventType() == MouseEvent.EVENT_MOUSE_WHEEL_MOVED)
            CAMERA.zoom((float) Math.pow(2, e.getRotation()[1]), new Vector2(e.getX(), e.getY()));
        this.mouse = CAMERA.getTransformMatrix().inverse().mul(new Vector2(e.getX(), e.getY()));
    }
}
