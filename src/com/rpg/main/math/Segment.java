package com.rpg.main.math;

public class Segment {
    //Positional data of the segment, including a starting x and y, and an ending x and y.
    int x1,x2,y1,y2;

    /**
     * Creates a new segment between two points defined by the inputted x and y values.
     * @param x1 (int) The x value of the first point.
     * @param y1 (int) The y value of the first point.
     * @param x2 (int) The x value of the second point.
     * @param y2 (int) The y value of the second point.
     */
    public Segment(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Returns the intersection point of two segments if one exists.
     * If the segments do not intersect a null value is returned.
     * @param seg (Segment) The inputted segment to test for.
     * @return (Vector) The intersection point between the two vectors, null if the segments do not intersect or infinitely intersect.
     */
    public Vector intersect(Segment seg) {
        Line l1 = new Line(getSlope(),new Vector(x1,y1));
        Line l2 = new Line(seg.getSlope(),new Vector(seg.x1,seg.y1));
        Vector pos = l1.getPoint(l2);
        if(pointExists((int)pos.getX())) return pos;
        return null;
    }

    /**
     * Returns the slope of the segment.
     * @return (float) Slope of the segment.
     */
    public float getSlope() {
        return ((float)y2-y1)/(x2-x1);
    }

    /**
     * Returns whether an inputted x value is within the bounds of the segment.
     * @param x (int) The x value to test for.
     * @return (boolean) Whether the inputted x value exists in the segment.
     */
    public boolean pointExists(int x) {
        return x >= Math.min(x1,x2) && x <= Math.max(x1,x2);
    }
    /**
     * Returns whether an inputted Vector is included in the segment.
     * Tests if the X value exists and the Y value exists.
     * @param vec (Vector) Vector to test for.
     * @return (boolean) Whether the inputted Vector exists in the segment.
     */
    public boolean pointExists(Vector vec) {
        Line line = new Line(getSlope(),new Vector(x1,y1));
        return pointExists((int)vec.getX())&&yExists((int)vec.getY())&&(line.getValue(vec.getX())==vec.getY());
    }

    /**
     * Returns the x value of the first point.
     * @return (int) X value of the first point.
     */
    public int getX1() {
        return x1;
    }

    /**
     * Sets the x value of the first point.
     * @param x1 (int) The inputted x value.
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * Returns the y value of the first point.
     * @return (int) Y value of the first point.
     */
    public int getY1() {
        return y1;
    }

    /**
     * Sets the y value of the first point.
     * @param y1 (int) The inputted y value.
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * Returns the x value of the second point.
     * @return (int) X value of the second point.
     */
    public int getX2() {
        return x2;
    }

    /**
     * Sets the x value of the second point.
     * @param x2 (int) The inputted x value.
     */
    public void setX2(int x2) {
        this.x2 = x2;
    }

    /**
     * Returns the y value of the second point.
     * @return (int) Y value of the second point.
     */
    public int getY2() {
        return y2;
    }

    /**
     * Sets the y value of the second point.
     * @param y2 (int) The inputted y value.
     */
    public void setY2(int y2) {
        this.y2 = y2;
    }

    /**
     * Returns if a y value exists in the bounds of the segment.
     * @param y (int) The inputted y value to test for.
     * @return (boolean) Whether the inputted y value exists in the bounds of the segment.
     */
    public boolean yExists(int y) {
        return y >= Math.min(y1,y2) && y <= Math.max(y1,y2);
    }
}
