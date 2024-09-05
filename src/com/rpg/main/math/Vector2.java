package com.rpg.main.math;

public class Vector2 {
    double x,y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(x+v.x,y+v.y);
    }
    public Vector2 sub(Vector2 v) {
        return new Vector2(x-v.x,y-v.y);
    }
    public Vector2 mul(Vector2 v) {
        return new Vector2(x*v.x,y*v.y);
    }
    public Vector2 scale(double scale) {
        return new Vector2(x*scale,y*scale);
    }
    public Vector2 norm() {
        if(mag()==0) return new Vector2(0,0);
        return scale(1/mag());
    }
    public double mag() {
        return dist(new Vector2(0,0));
    }
    public Vector2 perp() {
        return new Vector2(-y,x);
    }
    public double dist(Vector2 v) {
        return Math.hypot((v.x-x),(v.y-y));
    }
    public double dot(Vector2 v) {
        return (x*v.x)+(y*v.y);
    }

    public void print() {
        System.out.println("x:"+x+" y:"+y);
    }
}
