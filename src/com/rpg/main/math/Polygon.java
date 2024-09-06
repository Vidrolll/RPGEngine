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
        directEdges();
        fric = new Vector2(1,1);
    }
    public void directEdges() {
        if(edges.length<2) return;
        for(int e = 0; e < getEdges().length; e++) {
            Vector2 pE1 = getEdges()[e].perp().norm();
            Vector2 pE2 = getEdges()[(e+1==getEdges().length)?0:e+1].perp().norm();
            Line pL1 = new Line(pE1.getY()/pE1.getX(),getVertices()[e].sub(edges[e].scale(0.5)));
            Line pL2 = new Line(pE2.getY()/pE2.getX(),getVertices()[(e+1==getEdges().length)?0:e+1].sub(edges[(e+1==getEdges().length)?0:e+1].scale(0.5)));
            Vector2 inter = pL1.getPoint(pL2);
            if(inter == null) continue;
            if(pE1.getX()/inter.getX()>=0) {
                for(int i = 0; i < vertices.length; i++) {
                    edges[i] = edges[i].scale(-1);
                }
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

    public void renderPolygon(Graphics2D g) {
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

    public boolean segmentInPolygon(Segment seg) {
        for(int e = 0; e < getEdges().length; e++) {
            Vector2 edge = getEdges()[e];
            Line edgeLine = new Line(edge.getY()/edge.getX(),getVertices()[e]);
            Line segLine = new Line(seg.getSlope(),new Vector2(seg.getX1(),seg.getY1()));
            Vector2 inter = edgeLine.getPoint(segLine);
            Vector2 bound1 = getVertices()[e];
            Vector2 bound2 = getVertices()[(e+1==getVertices().length)?0:e+1];
            Segment polySeg = new Segment((int)bound1.getX(),(int)bound1.getY(),(int)bound2.getX(),(int)bound2.getY());
            if(Double.isInfinite(edgeLine.getSlope())||Double.isNaN(edgeLine.getSlope())&&
                    polySeg.pointExists((int)edge.getX())&&seg.pointExists((int)edge.getX())) return true;
            if(polySeg.pointExists((int)inter.getX())&&seg.pointExists((int)inter.getX())) return true;
        }
        return false;
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
