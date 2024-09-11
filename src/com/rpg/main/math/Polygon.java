package com.rpg.main.math;

import java.awt.*;

public class Polygon {
    /**
     * Vector data for the polygon containing the vertices,
     * edges, and position of the polygon.
     */
    Vector[] vertices;
    Vector[] edges;
    Vector pos;

    //The solidity of the polygon, used for collisions.
    boolean solid = true;

    //Experimental variable, currently unused.
    @Deprecated
    Vector fric;

    /**
     * Constructor for a polygon. Creates a polygon at the inputted position with the inputted vertices.
     * The first vertex should ALWAYS be Vector(0,0) and following vertices should be based relative to
     * that coordinate, regardless of position.
     * @param pos (Vector) The position to put the polygon.
     * @param vertices (Vector[]) A list of vertices to build the polygon.
     */
    public Polygon(Vector pos, Vector...vertices) {
        this.vertices = vertices;
        this.pos = pos;
        edges = new Vector[vertices.length];
        for(int i = 0; i < vertices.length; i++)
            edges[i] = vertices[i].sub(vertices[(i+1==vertices.length)?0:i+1]);
        directEdges();
        fric = new Vector(1,1);
    }

    /**
     * Function called when a polygon is created to make sure all edges are correctly oriented for SAT.
     * This function should not be called by any other class.
     */
    public void directEdges() {
        if(edges.length<2) return;
        for(int e = 0; e < getEdges().length; e++) {
            Vector pE1 = getEdges()[e].perp().norm();
            Vector pE2 = getEdges()[(e+1==getEdges().length)?0:e+1].perp().norm();
            Line pL1 = new Line(pE1.getY()/pE1.getX(),getVertices()[e].sub(edges[e].scale(0.5f)));
            Line pL2 = new Line(pE2.getY()/pE2.getX(),getVertices()[(e+1==getEdges().length)?0:e+1].sub(edges[(e+1==getEdges().length)?0:e+1].scale(0.5f)));
            Vector inter = pL1.getPoint(pL2);
            if(inter == null) continue;
            if(pE1.getX()/inter.getX()>=0) {
                for(int i = 0; i < vertices.length; i++) {
                    edges[i] = edges[i].scale(-1);
                }
            }
        }
    }

    /**
     * Returns the vertices of the polygon.
     * @return (Vector[]) The vertices of the polygon.
     */
    public Vector[] getVertices() {
        Vector[] v = new Vector[vertices.length];
        for(int i = 0; i < v.length; i++) v[i] = vertices[i].add(pos);
        return v;
    }

    /**
     * Returns the center of the polygon. Determined by (xValues+yValues)/2.
     * @return (Vector) The center of the polygon.
     */
    public Vector getCenter() {
        int x=0,y=0;
        for(int i = 0; i < getVertices().length; i++) {
            x += (int) getVertices()[i].getX();
            y += (int) getVertices()[i].getY();
        }
        return new Vector((float) x /getVertices().length, (float) y /getVertices().length);
    }

    /**
     * Returns the edges of the polygon.
     * @return (Vector[]) An array containing all the polygon edges.
     */
    public Vector[] getEdges() {
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
        Vector minimumTranslationVector = new Vector(Integer.MAX_VALUE,Integer.MAX_VALUE);
        for(int s = 0; s < 2; s++) {
            Polygon sA = (s==0) ? this : poly;
            Polygon sB = (s==1) ? this : poly;
            for(Vector e : sA.getEdges()) {
                Vector pE = e.perp().norm();
                float aMin = Integer.MAX_VALUE,aMax = Integer.MIN_VALUE,bMin = Integer.MAX_VALUE,bMax = Integer.MIN_VALUE;
                for(Vector v : sA.getVertices()) {
                    float dot = pE.dot(v);
                    if(dot < aMin) aMin = dot;
                    if(dot > aMax) aMax = dot;
                }
                for(Vector v : sB.getVertices()) {
                    float dot = pE.dot(v);
                    if(dot < bMin) bMin = dot;
                    if(dot > bMax) bMax = dot;
                }
                if(aMax < bMin || bMax < aMin) return false;
                float distance = aMax-bMin;
                if(s==1) distance*=-1;
                if(pE.scale(-distance).mag()<minimumTranslationVector.mag())
                    minimumTranslationVector = pE.scale(-distance).mul(sA.fric);
                if(distance<minimumTranslation) minimumTranslation = distance;
            }
        }
        if(getSolid()&&poly.getSolid()) {
            move(minimumTranslationVector);
            return false;
        }
        return true;
    }

    /**
     * A test function that renders the polygon data.
     * @param g (Graphics2D) The graphics object to render to.
     */
    public void renderPolygon(Graphics2D g) {
        for(int i = 0; i < getVertices().length; i++) {
            g.setColor(Color.GREEN);
            Vector midpoint = getVertices()[(i+1==getVertices().length)?0:i+1].add(getVertices()[i]).scale(0.5f);
            Vector normal = getEdges()[i].perp().norm().scale(50).add(midpoint);;
            g.drawLine((int)normal.getX(),(int)normal.getY(),(int)midpoint.getX(),(int)midpoint.getY());
            g.setColor(Color.WHITE);
            g.drawLine((int)getVertices()[i].getX(),
                    (int)getVertices()[i].getY(),
                    (int)getVertices()[(i+1==getVertices().length)?0:i+1].getX(),
                    (int)getVertices()[(i+1==getVertices().length)?0:i+1].getY());
        }
    }

    /**
     * Determines if a given segment intersects the parent polygon.
     * @param seg (Segment) The segment to test for intersection.
     * @return (boolean) Whether the segment intersects the polygon.
     */
    public boolean segmentInPolygon(Segment seg) {
        for(int e = 0; e < getEdges().length; e++) {
            Vector edge = getEdges()[e];
            Line edgeLine = new Line(edge.getY()/edge.getX(),getVertices()[e]);
            Line segLine = new Line(seg.getSlope(),new Vector(seg.getX1(),seg.getY1()));
            Vector inter = edgeLine.getPoint(segLine);
            if(inter==null) return false;
            Vector bound1 = getVertices()[e];
            Vector bound2 = getVertices()[(e+1==getVertices().length)?0:e+1];
            Segment polySeg = new Segment((int)bound1.getX(),(int)bound1.getY(),(int)bound2.getX(),(int)bound2.getY());
            if(polySeg.pointExists(inter)&&seg.pointExists(inter)) return true;
        }
        return false;
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
        pos = pos.add(new Vector(x,0));
    }

    /**
     * Moves the y position of the polygon.
     * @param y (int) The y value to move by.
     */
    public void moveY(int y) {
        pos = pos.add(new Vector(0,y));
    }

    /**
     * Moves the polygon by a given x and y value.
     * @param x (int) The x value to move by.
     * @param y (int) The y value to move by.
     */
    public void move(int x, int y) {
        pos = pos.add(new Vector(x,y));
    }

    /**
     * Moves the polygon by a given vector.
     * @param v (Vector) The vector to move by.
     */
    public void move(Vector v) {
        pos = pos.add(v);
    }

    /**
     * Sets the position of the polygon to a given value.
     * @param x (int) The x position to set to.
     * @param y (int) The y position to set to.
     */
    public void setPos(int x, int y) {
        pos = new Vector(x,y);
    }

    //Experimental function, currently unused.
    @Deprecated
    public void setFric(Vector fric) {
        this.fric = fric;
    }
}
