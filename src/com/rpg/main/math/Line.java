package com.rpg.main.math;

public class Line {
    double m,b;

    public Line(double slope, double yInter) {
        m = slope;
        b = yInter;
    }
    public Line(double slope, Vector2 point) {
        this(slope,-slope*point.getX()+point.getY());
    }

    public double getValue(double x) {
        return m*x+b;
    }
    public double getSlope() {
        return m;
    }
    public double getYInter() {
        return b;
    }
    public Vector2 getPoint(Line line) {
        double x = (line.b-b)/(m-line.m);
        if(Double.isInfinite(getValue(x))||Double.isNaN(getValue(x))) return null;
        return new Vector2(x,getValue(x));
    }
}
