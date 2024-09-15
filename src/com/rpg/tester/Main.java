package com.rpg.tester;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.math.*;
import com.rpg.main.math.Polygon;

import java.awt.*;

public class Main extends Game {
    Polygon s1,s2,s3,s4;

    public Main() {
        System.out.println("Triangle");
        s1 = new Polygon(new Vector(300,200),new Vector(0,0),new Vector(50,300),new Vector(100,-50));
        System.out.println("Square");
        s2 = new Polygon(new Vector(500,500),new Vector(0,50), new Vector(0,-50),new Vector(100,-50),new Vector(100,50));
        System.out.println("Rectangle");
        s3 = new Polygon(new Vector(500,700),new Vector(0,50), new Vector(0,-50),new Vector(1000,-50),new Vector(1000,50));
        int sides = 100;
        int size = 100;
        Vector[] vArr = new Vector[sides];
        for(int i = 0; i < sides; i++) {
            double theta = 2*Math.PI/sides*i;
            vArr[i] = new Vector(size*(float)Math.cos(theta),size*(float)Math.sin(theta));
        }
        s4 = new Polygon(new Vector(0,0), vArr);
        System.out.println("Custom Polygon");
        s4.setPos(500,700);
        s3.setTransform(new Matrix(new float[][]{
                {5000*(float)Math.cos(Math.toRadians(0)),5000*-(float)Math.sin(Math.toRadians(0))},
                {(float)Math.sin(Math.toRadians(0)),(float)Math.cos(Math.toRadians(0))}
        }));
    }

    @Override
    public void draw(GL2 gl) {
        if(s1==null||s2==null||s3==null||s4==null) return;
        s1.renderPolygon(gl);
        s2.renderPolygon(gl);
        s3.renderPolygon(gl);
        s4.renderPolygon(gl);
    }

    int rotate;
    @Override
    public void update() {
        if(s1==null||s2==null||s3==null||s4==null) return;
        s2.move(new Vector(velX,velY));
        if(velX>0) rotate+=5;
        if(velX<0) rotate-=5;
        if(rotate%90!=0&&velX==0) {
            if(rotate%45==0) rotate-=5;
            rotate+=5*((int)Math.signum((rotate%90)-45));
            s2.move(new Vector(5*((int)Math.signum((rotate%90)-45)),0));
        }
        if(velY < 10) velY++;
        if(s2.sat(s1)) velY = 5;
        if(s2.sat(s3)) velY = 5;
        if(s2.sat(s4)) velY = 5;
        s2.setTransform(new Matrix(new float[][]{
                {(float)Math.cos(Math.toRadians(rotate)),-(float)Math.sin(Math.toRadians(rotate))},
                {(float)Math.sin(Math.toRadians(rotate)),(float)Math.cos(Math.toRadians(rotate))}
        }));
    }

    @Override
    public void input(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
        if(e.getEventType()==KeyEvent.EVENT_KEY_PRESSED&&(InputEvent.AUTOREPEAT_MASK & e.getModifiers()) == 0) {
            if(e.getKeyCode()==KeyEvent.VK_A) {
                keyDown[2] = true;
                velX = -5;
            }
            if(e.getKeyCode()==KeyEvent.VK_D) {
                keyDown[3] = true;
                velX = 5;
            }
            if(e.getKeyCode()==KeyEvent.VK_SPACE) velY = -20;
        }
        if(e.getEventType()==KeyEvent.EVENT_KEY_RELEASED&&(InputEvent.AUTOREPEAT_MASK & e.getModifiers()) == 0) {
            if(e.getKeyCode()==KeyEvent.VK_A) keyDown[2] = false;
            if(e.getKeyCode()==KeyEvent.VK_D) keyDown[3] = false;
            if(!(keyDown[2]||keyDown[3])) velX = 0;
        }
    }

    @Override
    public void input(MouseEvent e) {

    }

    int velX=0,velY=0;
    boolean[] keyDown = {false,false,false,false};

    public static void main(String[] args) {
        new Main();
    }
}
