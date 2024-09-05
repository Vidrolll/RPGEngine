package com.rpg.tester;

import com.rpg.main.Game;
import com.rpg.main.math.Line;
import com.rpg.main.math.LinearMath;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Main extends Game {
    Polygon s1,s2,s3,s4;

    public Main() {
        System.out.println("Triangle");
        s1 = new Polygon(new Vector2(300,200),new Vector2(0,0),new Vector2(50,300),new Vector2(100,-50));
        System.out.println("Square");
        s2 = new Polygon(new Vector2(500,500),new Vector2(0,50), new Vector2(0,-50),new Vector2(100,-50),new Vector2(100,50));
        System.out.println("Rectangle");
        s3 = new Polygon(new Vector2(500,700),new Vector2(0,50), new Vector2(0,-50),new Vector2(1000,-50),new Vector2(1000,50));
        int sides = 5;
        int size = 100;
        Vector2[] vArr = new Vector2[sides];
        for(int i = 0; i < sides; i++) {
            double theta = 2*Math.PI/sides*i;
            vArr[i] = new Vector2(size*Math.cos(theta),size*Math.sin(theta));
        }
        s4 = new Polygon(new Vector2(0,0), vArr);
        System.out.println("Custom Polygon");
        s4.setPos(100,700);
    }

    @Override
    public void update() {
        s2.move(new Vector2(velX,velY).norm().scale(5));
        s2.sat(s1);
        s2.sat(s3);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        //if(s2.sat(s1)) g.setColor(Color.RED);
        s1.renderPolygon(g);
        s2.renderPolygon(g);
        s3.renderPolygon(g);
        s4.renderPolygon(g);
    }

    int velX=0,velY=0;
    boolean[] keyDown = {false,false,false,false};
    @Override
    public void input(KeyEvent e) {
        if(e.getID()==KeyEvent.KEY_PRESSED) {
            if(e.getKeyCode()==KeyEvent.VK_W) {
                keyDown[0] = true;
                velY = -5;
            }
            if(e.getKeyCode()==KeyEvent.VK_S) {
                keyDown[1] = true;
                velY = 5;
            }
            if(e.getKeyCode()==KeyEvent.VK_A) {
                keyDown[2] = true;
                velX = -5;
            }
            if(e.getKeyCode()==KeyEvent.VK_D) {
                keyDown[3] = true;
                velX = 5;
            }
        }
        if(e.getID()==KeyEvent.KEY_RELEASED) {
            if(e.getKeyCode()==KeyEvent.VK_W) keyDown[0] = false;
            if(e.getKeyCode()==KeyEvent.VK_S) keyDown[1] = false;
            if(e.getKeyCode()==KeyEvent.VK_A) keyDown[2] = false;
            if(e.getKeyCode()==KeyEvent.VK_D) keyDown[3] = false;
            if(!(keyDown[0]||keyDown[1])) velY = 0;
            if(!(keyDown[2]||keyDown[3])) velX = 0;
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
