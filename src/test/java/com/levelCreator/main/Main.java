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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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

    @Override
    public void draw(GL2 gl) {
        if (level != null) level.render(gl);
        if (poly != null) poly.renderPolygon(gl);
        if (rect != null) rect.renderPolygon(gl);
    }

    boolean[] press = new boolean[13];

    public static void main(String[] args) {
        new Main();
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
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(CAMERA.getScreenCenter().getX(), CAMERA.getScreenCenter().getY());
        gl.glVertex2f(CAMERA.getScreenCenter().getX() + 1, CAMERA.getScreenCenter().getY());
        gl.glVertex2f(CAMERA.getScreenCenter().getX() + 1, CAMERA.getScreenCenter().getY() + 1);
        gl.glVertex2f(CAMERA.getScreenCenter().getX(), CAMERA.getScreenCenter().getY() + 1);
        gl.glEnd();
        gl.glFlush();

        Vector2 center = CAMERA.getTransformMatrix().inverse().mul(CAMERA.getScreenCenter());
        GLUT glut = new GLUT();
        gl.glRasterPos2d(900, 50);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, String.valueOf(mouse.getX()));
        gl.glRasterPos2d(900, 100);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, String.valueOf(mouse.getY()));
        gl.glRasterPos2d(1000, 50);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, String.valueOf(center.getX()));
        gl.glRasterPos2d(1000, 100);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, String.valueOf(center.getY()));
    }

    @Override
    public void update() {
        for (int jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++) {
            if (glfwJoystickPresent(jid)) {
                FloatBuffer axes = glfwGetJoystickAxes(jid);
                ByteBuffer buttons = glfwGetJoystickButtons(jid);

                if (axes != null) {
                    float lx = axes.get(0); // Left stick X
                    float ly = axes.get(1); // Left stick Y
                    if (Math.abs(lx) > 0.1) CAMERA.move(new Vector2(5, 0).scale(-lx));
                    if (Math.abs(ly) > 0.1) CAMERA.move(new Vector2(0, 5).scale(-ly));
                }

                if (buttons != null) {
                    for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++)
                        if (buttons.get(i) == GLFW_PRESS) System.out.println(i);

                    if (buttons.get(10) == GLFW_PRESS && !press[10]) {
                        press[10] = true;
                        CAMERA.zoom(2f, CAMERA.getScreenCenter());
                    } else if (buttons.get(10) != GLFW_PRESS) press[10] = false;
                    if (buttons.get(12) == GLFW_PRESS && !press[12]) {
                        press[12] = true;
                        CAMERA.zoom(0.5f, CAMERA.getScreenCenter());
                    } else if (buttons.get(12) != GLFW_PRESS) press[12] = false;
//                    if (buttons.get(0) == GLFW_PRESS&&!jump) {
//                        jump = true;
//                        System.out.println("jump");
//                        player.setVelY(-20);
//                    }else if(buttons.get(0) != GLFW_PRESS) jump = false;
//                    if (buttons.get(1) == GLFW_PRESS) {
//                        System.exit(1);
//                    }
                }
            }
        }
        if (rect != null) {
            Vector2 vert = mouse.sub(rect.getPos());
            if (vert.equals(rect.getPos())) vert = vert.add(new Vector2(1, 1));
            rect = new Polygon(rect.getPos(),
                    new Vector2(0, 0), new Vector2(vert.getX(), 0),
                    new Vector2(vert.getX(), vert.getY()), new Vector2(0, vert.getY()));
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
//                    level.map.put("shape" + level.map.size(), rect);
                    rect = null;
                }
                if (poly != null) {
//                    level.map.put("shape" + level.map.size(), poly);
                    poly = null;
                }
            }
        }
    }
}
