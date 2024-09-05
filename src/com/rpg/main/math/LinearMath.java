package com.rpg.main.math;

public class LinearMath {
    public static Vector2 pointInterLine(Vector2 point, Line line) {
        double slope = -(1.0/line.getSlope());
        Line pLine = new Line(slope,-slope*point.getX()+point.getY());
        return line.getPoint(pLine);
    }
}
