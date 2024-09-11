package com.rpg.main.math;

public class Line {
    //Line data, including slope, y-intercept, and an x value (in case slope is infinite).
    float m,b,x;

    /**
     * Creates a new Line object, using an inputted slope and an inputted point utilizing the equation y-y1=m(x-x1).
     * @param slope (float) Slope of the inputted line.
     * @param point (Vector) A point to intersect with the line.
     */
    public Line(float slope, Vector point) {
        this.m = slope;
        this.b = -slope*point.getX()+point.getY();
        this.x = point.getX();
    }

    /**
     * Returns a value at the inputted x coordinate.
     * @param x (float) X position of the value.
     * @return (float) Y value of the graph at the x position.
     */
    public float getValue(float x) {
        return m*x+b;
    }

    /**
     * Returns the slope of the line.
     * @return (float) Slope of the line.
     */
    public float getSlope() {
        return m;
    }

    /**
     * Returns the y-intercept of the graph.
     * @return (float) Y-intercept of the graph.
     */
    public float getYInter() {
        return b;
    }

    /**
     * Returns the intersection point of two lines, returns null if the slopes of
     * both lines are the same (resulting in either 0 or infinite answers. In this case 0 answers is automatically assumed).
     * @param line (Line) The inputted line to check.
     * @return (Vector) The intersection point of the two lines.
     */
    public Vector getPoint(Line line) {
        float x = (line.b-b)/(m-line.m);
        if(m==line.m) return null;
        if(Float.isInfinite(m)) return new Vector(this.x,line.getValue(this.x));
        if(Float.isInfinite(line.m)) return new Vector(line.x,getValue(line.x));
        return new Vector(x,getValue(x));
    }
}
