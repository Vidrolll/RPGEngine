package com.rpg.main.math;

import com.jogamp.opengl.GL2;

import com.rpg.main.graphics.Graphics;
import com.rpg.main.math.vector.Matrix2;
import com.rpg.main.math.vector.Vector2;

public class Polygon {
    /**
     * Vector data for the polygon containing the vertices,
     * edges, and position of the polygon.
     */
    Vector2[] vertices;
    Vector2[] edges;
    Vector2 pos;

    Matrix2 transform;

    //The solidity of the polygon, used for collisions.
    boolean solid = true;

    //Experimental variable, currently unused.
    @Deprecated
    Vector2 fric;

    /**
     * Constructor for a polygon. Creates a polygon at the inputted position with the inputted vertices.
     * The first vertex should ALWAYS be Vector(0,0) and following vertices should be based relative to
     * that coordinate, regardless of position.
     * @param pos (Vector) The position to put the polygon.
     * @param vertices (Vector[]) A list of vertices to build the polygon.
     */
    public Polygon(Vector2 pos, Vector2...vertices) {
        transform = new Matrix2(new float[][]{
                {(float)Math.cos(Math.toRadians(0)),(float)-Math.sin(Math.toRadians(0))},
                {(float)Math.sin(Math.toRadians(0)),(float)Math.cos(Math.toRadians(0))}
        });
        this.vertices = vertices;
        this.pos = pos;
        Vector2 cent = getCenter();
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].sub(cent);
        }
        edges = new Vector2[getVertices().length];
        for(int i = 0; i < getVertices().length; i++)
            edges[i] = getVertices()[i].sub(getVertices()[(i+1==getVertices().length)?0:i+1]);
        directEdges();
        fric = new Vector2(1,1);
    }

    /**
     * Ensures edges are correctly oriented for SAT using the cross product test.
     */
    private void directEdges() {
        if (edges.length < 2) return;

        // Compute signed area using cross product
        float sum = 0;
        for (int i = 0; i < vertices.length; i++) {
            Vector2 v1 = vertices[i];
            Vector2 v2 = vertices[(i + 1) % vertices.length]; // Next vertex
            sum += (v2.getX() - v1.getX()) * (v2.getY() + v1.getY()); // Shoelace theorem
        }

        // If sum > 0, the polygon is counterclockwise and needs reversing
        if (sum > 0) {
            reverseVertices();  // Flip vertex order
        }

        // Recalculate edges after ensuring correct vertex order
        for (int i = 0; i < vertices.length; i++) {
            edges[i] = getVertices()[i].sub(getVertices()[(i+1==getVertices().length)?0:i+1]);
        }
    }

    /**
     * Reverses the order of vertices to correct edge orientation.
     */
    private void reverseVertices() {
        int n = vertices.length;
        for (int i = 0; i < n / 2; i++) {
            Vector2 temp = vertices[i];
            vertices[i] = vertices[n - 1 - i];
            vertices[n - 1 - i] = temp;
        }
    }

    /**
     * Returns the vertices of the polygon.
     * @return (Vector[]) The vertices of the polygon.
     */
    public Vector2[] getVertices() {
        Vector2[] v = new Vector2[vertices.length];
//        for(int i = 0; i < v.length; i++) v[i] = vertices[i].add(pos);
        for(int i = 0; i < v.length; i++) v[i] = transform.mul(vertices[i]).add(pos);
        return v;
    }

    /**
     * Returns the center of the polygon. Determined by (xValues+yValues)/2.
     * @return (Vector) The center of the polygon.
     */
    public Vector2 getCenter() {
        int x=0,y=0;
        for(int i = 0; i < getVertices().length; i++) {
            x += (int) getVertices()[i].sub(pos).getX();
            y += (int) getVertices()[i].sub(pos).getY();
        }
        return new Vector2((float) x /getVertices().length, (float) y /getVertices().length);
    }

    /**
     * Returns the edges of the polygon.
     * @return (Vector[]) An array containing all the polygon edges.
     */
    public Vector2[] getEdges() {
        return edges;
    }

    /**
     * Performs an algorithm titled Separating Axis Theorem (SAT) to determine if the parent polygon and the inputted
     * polygon are colliding. If a collision is detected and both polygons are solid said collision will be corrected.
     * @param poly (Polygon) The polygon to test for a collision.
     * @return (boolean) Whether a collision has been detected.
     */
    public boolean sat(Polygon poly) {
        double minimumTranslation = Integer.MAX_VALUE;
        Vector2 minimumTranslationVector = new Vector2(Integer.MAX_VALUE,Integer.MAX_VALUE);
        for(int s = 0; s < 2; s++) {
            Polygon sA = (s==0) ? this : poly;
            Polygon sB = (s==1) ? this : poly;
            for(Vector2 e : sA.getEdges()) {
                Vector2 pE = e.perp().norm();
                float aMin = Integer.MAX_VALUE,aMax = Integer.MIN_VALUE,bMin = Integer.MAX_VALUE,bMax = Integer.MIN_VALUE;
                for(Vector2 v : sA.getVertices()) {
                    float dot = pE.dot(v);
                    if(dot < aMin) aMin = dot;
                    if(dot > aMax) aMax = dot;
                }
                for(Vector2 v : sB.getVertices()) {
                    float dot = pE.dot(v);
                    if(dot < bMin) bMin = dot;
                    if(dot > bMax) bMax = dot;
                }
                if(aMax < bMin || bMax < aMin) return false;
                float distance = aMax-bMin;
                if(s==1) distance*=-1;
                if(pE.scale(-distance).mag()<minimumTranslationVector.mag())
                    minimumTranslationVector = pE.scale(-distance);
                if(distance<minimumTranslation) minimumTranslation = distance;
            }
        }
        if(getSolid()&&poly.getSolid())
            move(minimumTranslationVector);
        return true;
    }

    /**
     * A test function that renders the polygon data.
     * @param gl (GL2) The OpenGL object to render to.
     */
    public void renderPolygon(GL2 gl) {
        gl.glColor3f(1,1,1);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_LINE);
        Graphics.drawPoly(gl,getVertices());
        gl.glColor3f(0,1,0);
        for(int i = 0; i < getVertices().length; i++) {
            Vector2 midpoint = getVertices()[i].sub(getEdges()[i].scale(0.5f));
            Vector2 normal = getEdges()[i].perp().norm().scale(50).add(midpoint);;
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2f(normal.getX(),normal.getY());
            gl.glVertex2f(midpoint.getX(),midpoint.getY());
            gl.glEnd();
            gl.glFlush();
        }
    }

    /**
     * Returns if the shape is solid or not.
     * @return (boolean) The solidity of the shape.
     */
    public boolean getSolid() {
        return solid;
    }

    /**
     * Moves the x position of the polygon.
     * @param x (int) The x value to move by.
     */
    public void moveX(int x) {
        pos = pos.add(new Vector2(x,0));
    }

    /**
     * Moves the y position of the polygon.
     * @param y (int) The y value to move by.
     */
    public void moveY(int y) {
        pos = pos.add(new Vector2(0,y));
    }

    /**
     * Moves the polygon by a given x and y value.
     * @param x (int) The x value to move by.
     * @param y (int) The y value to move by.
     */
    public void move(int x, int y) {
        pos = pos.add(new Vector2(x,y));
    }

    /**
     * Moves the polygon by a given vector.
     * @param v (Vector) The vector to move by.
     */
    public void move(Vector2 v) {
        pos = pos.add(v);
    }

    /**
     * Sets the position of the polygon to a given value.
     * @param x (int) The x position to set to.
     * @param y (int) The y position to set to.
     */
    public void setPos(int x, int y) {
        pos = new Vector2(x,y);
    }

    /**
     * Returns the current position of the polygon.
     * @return (Vector2) The current position of the polygon.
     */
    public Vector2 getPos() {
        return pos;
    }

    /**
     * Sets the transformation matrix to the inputted matrix.
     * @param transform (Matrix) The inputted transformation matrix.
     */
    public void setTransform(Matrix2 transform) {
        this.transform = transform;
        for(int i = 0; i < getVertices().length; i++)
            edges[i] = getVertices()[i].sub(getVertices()[(i+1==getVertices().length)?0:i+1]);
    }

    //Experimental function, currently unused.
    @Deprecated
    public void setFric(Vector2 fric) {
        this.fric = fric;
    }
}
