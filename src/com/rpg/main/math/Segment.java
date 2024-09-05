package com.rpg.main.math;

public class Segment {
    int x1,x2,y1,y2;
    public Segment(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Vector2 intersect(Segment seg) {
        Line l1 = new Line(getSlope(),new Vector2(x1,y1));
        Line l2 = new Line(seg.getSlope(),new Vector2(seg.x1,seg.y1));
        Vector2 pos = l1.getPoint(l2);
        if(pointExists((int)pos.getX())) return pos;
        return null;
    }
    public double getSlope() {
        return ((double)y2-y1)/(x2-x1);
    }
    public boolean pointExists(int x) {
        return x >= Math.min(x1,x2) && x <= Math.max(x1,x2);
    }
}
