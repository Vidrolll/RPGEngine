package com.rpg.main.math;

public class LinearMath {
    /**
     * Returns a vector starting at a point perpendicular to an inputted line.
     * @param point (Vector) The inputted point to test from.
     * @param line (Line) The line to test.
     * @return (Vector) A vector starting at the point perpendicular to the line.
     */
    public static Vector pointInterLine(Vector point, Line line) {
        float slope = -(1.0f/line.getSlope());
        Line pLine = new Line(slope,point);
        return line.getPoint(pLine);
    }
}
