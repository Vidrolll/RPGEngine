package com.rpg.main.math;

import java.awt.*;

public class Polygon {
    Vector2[] vertices;
    Vector2[] edges;
    Vector2 pos;

    Vector2 fric;

    public Polygon(Vector2 pos, Vector2...vertices) {
        this.vertices = vertices;
        this.pos = pos;
        edges = new Vector2[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            edges[i] = vertices[i].sub(vertices[(i+1==vertices.length)?0:i+1]);
//            edges[i] = vertices[(i+1==vertices.length)?0:i+1].sub(vertices[i]);
        }
//        directEdges();
        fric = new Vector2(1,1);
    }
    public void directEdges() {
        if(edges.length<2) return;
        Vector2 pE1 = getEdges()[0].perp().norm();
        Vector2 pE2 = getEdges()[1].perp().norm();
        Line pL1 = new Line(pE1.getY()/pE1.getX(),getEdges()[0]);
        Line pL2 = new Line(pE2.getY()/pE2.getX(),getEdges()[1]);
        Vector2 inter = pL1.getPoint(pL2).norm();
        System.out.println("---------------");
//        getEdges()[0].print();
//        getEdges()[1].print();
//        pE1.print();
//        pE2.print();
//        inter.print();
//        pE1.norm().print();
//        pE2.norm().print();
//        inter.norm().print();
        for(int i = 0; i < getEdges().length; i++) {
            if(getEdges()[i].perp().norm().getX()/inter.getX()>=0) {
                getEdges()[i].perp().norm().print();
                inter.print();
                System.out.println("test");
                for(int j = 0; j < vertices.length; j++) {
                    edges[j] = edges[j].scale(-1);
                }
                return;
            }
        }
    }

    public Vector2[] getVertices() {
        Vector2[] v = new Vector2[vertices.length];
        for(int i = 0; i < v.length; i++) v[i] = vertices[i].add(pos);
        return v;
    }
    public Vector2 getCenter() {
        int x=0,y=0;
        for(int i = 0; i < getVertices().length; i++) {
            x += (int) getVertices()[i].getX();
            y += (int) getVertices()[i].getY();
        }
        return new Vector2((double) x /getVertices().length, (double) y /getVertices().length);
    }
    public Vector2[] getEdges() {
        return edges;
    }
    public boolean sat(Polygon poly) {
        double minimumTranslation = Integer.MAX_VALUE;
        Vector2 minimumTranslationVector = new Vector2(Integer.MAX_VALUE,Integer.MAX_VALUE);
        Vector2 baseVector = null;
        for(int s = 0; s < 2; s++) {
            Polygon sA = (s==0) ? this : poly;
            Polygon sB = (s==1) ? this : poly;
            for(Vector2 e : sA.getEdges()) {
                Vector2 pE = e.perp().norm();
                double aMin = Integer.MAX_VALUE,aMax = Integer.MIN_VALUE,bMin = Integer.MAX_VALUE,bMax = Integer.MIN_VALUE;
                for(Vector2 v : sA.getVertices()) {
                    double dot = pE.dot(v);
                    if(dot < aMin) aMin = dot;
                    if(dot > aMax) aMax = dot;
                }
                for(Vector2 v : sB.getVertices()) {
                    double dot = pE.dot(v);
                    if(dot < bMin) bMin = dot;
                    if(dot > bMax) bMax = dot;
                }
                if(aMax < bMin || bMax < aMin) return false;
                double distance = aMax-bMin;
                if(s==1) distance*=-1;
                if(pE.scale(-distance).mag()<minimumTranslationVector.mag()) {
                    minimumTranslationVector = pE.scale(-distance).mul(sA.fric);
                    baseVector = pE;
                }
                if(distance<minimumTranslation) minimumTranslation = distance;
            }
        }
        move(minimumTranslationVector);
        baseVector.print();
        return true;
    }

    public boolean pip(Vector2 point) {
        boolean odd = false;
        for (int i = 0, j = getVertices().length - 1; i < getVertices().length; i++) {
            if (((getVertices()[i].getY() > point.getY()) != (getVertices()[j].getY() > point.getY()))
                    && (point.getX() < (getVertices()[j].getX() - getVertices()[i].getX()) * (point.getY() - getVertices()[i].getY()) / (getVertices()[j].getY() - getVertices()[i].getY()) + getVertices()[i].getX())) {
                odd = !odd;
            }
            j = i;
        }
        return odd;
    }

    public void renderPolygon(Graphics2D g) {
        Vector2 pE1 = getEdges()[0].perp();
        Line line = new Line(pE1.getY()/pE1.getX(),getVertices()[0].sub(edges[0].scale(0.5)));
        g.setColor(Color.RED);
        g.fillRect((int)getVertices()[0].sub(edges[0].scale(0.5)).getX()-3,(int)getVertices()[0].sub(edges[0].scale(0.5)).getY()-3,5,5);
        g.setColor(Color.WHITE);
        g.drawLine(-1000,(int)line.getValue(-1000),1000,(int)line.getValue(1000));
        for(int i = 0; i < getVertices().length; i++) {
            g.setColor(Color.GREEN);
            Vector2 midpoint = getVertices()[(i+1==getVertices().length)?0:i+1].add(getVertices()[i]).scale(0.5);
            Vector2 normal = getEdges()[i].perp().norm().scale(50).add(midpoint);;
            g.drawLine((int)normal.getX(),(int)normal.getY(),(int)midpoint.getX(),(int)midpoint.getY());
            g.setColor(Color.WHITE);
            g.drawLine((int)getVertices()[i].getX(),
                    (int)getVertices()[i].getY(),
                    (int)getVertices()[(i+1==getVertices().length)?0:i+1].getX(),
                    (int)getVertices()[(i+1==getVertices().length)?0:i+1].getY());
        }
    }

    public void moveX(int x) {
        pos = pos.add(new Vector2(x,0));
    }
    public void moveY(int y) {
        pos = pos.add(new Vector2(0,y));
    }
    public void move(int x, int y) {
        pos = pos.add(new Vector2(x,y));
    }
    public void move(Vector2 v) {
        pos = pos.add(v);
    }
    public void setPos(int x, int y) {
        pos = new Vector2(x,y);
    }
    public void setFric(Vector2 fric) {
        this.fric = fric;
    }
}
