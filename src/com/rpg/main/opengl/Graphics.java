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
        gl.glEnd();
        gl.glFlush();
    }
}
