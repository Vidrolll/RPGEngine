package com.rpg.main.opengl;

import com.jogamp.opengl.GL2;
import com.rpg.main.math.Vector;

public class Graphics {
    /**
     * Draw a polygon with the inputted vertices.
     * @param gl (GL2) The OpenGL object to render with.
     * @param vertices (Vector[]) The list of vertices to render with.
     */
    public static void drawPoly(GL2 gl, Vector...vertices) {
        gl.glBegin(GL2.GL_POLYGON);
        for (Vector vertex : vertices) {
            gl.glVertex2f(vertex.getX(), vertex.getY());
        }
        gl.glVertex2f(vertices[0].getX(), vertices[0].getY());
        gl.glEnd();
        gl.glFlush();
    }

    /**
     * Draw a polygon with the inputted x, y, width, and height values.
     * @param gl (GL2) The OpenGL object to render with.
     * @param x (Float) The x coordinate to render to.
     * @param y (Float) The y coordinate to render to.
     * @param width (Float) The width to render with.
     * @param height (Float) The height to render with.
     */
    public static void drawRect(GL2 gl, float x, float y, float width, float height) {
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(x,y);
        gl.glVertex2f(x+width,y);
        gl.glVertex2f(x+width,y+height);
        gl.glVertex2f(x,y+height);
        gl.glEnd();
        gl.glFlush();
    }
    public static void drawImg() {

    }
}
