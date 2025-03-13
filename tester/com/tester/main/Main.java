package com.tester.main;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.math.Polygon;
import com.rpg.main.graphics.opengl.EventListener;
import com.rpg.main.graphics.opengl.Renderer;
import com.rpg.main.math.vector.Matrix3;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.util.Assets;
import com.rpg.main.util.Time;
import org.lwjgl.openal.AL10;

public class Main extends Game {
    Polygon s1,s2,s3,s4;

    public Main() {
        Assets.initialize();
        System.out.println("Triangle");
        s1 = new Polygon(new Vector2(300,200),new Vector2(0,0),new Vector2(50,300),new Vector2(100,-50));
        System.out.println("Square");
        s2 = new Polygon(new Vector2(500,500),new Vector2(0,50), new Vector2(0,-50),new Vector2(100,-50),new Vector2(100,50));
        System.out.println("Rectangle");
        s3 = new Polygon(new Vector2(500,700),new Vector2(0,50), new Vector2(0,-50),new Vector2(1000,-50),new Vector2(1000,50));
        int sides = 100;
        int size = 100;
        Vector2[] vArr = new Vector2[sides];
        for(int i = 0; i < sides; i++) {
            double theta = 2*Math.PI/sides*i;
            vArr[i] = new Vector2(size*(float)Math.cos(theta),size*(float)Math.sin(theta));
        }
        s4 = new Polygon(new Vector2(0,0), vArr);
        System.out.println("Custom Polygon");
        s4.setPos(500,700);
        s3.setTransform(new Matrix3(new float[][]{
                {5000*(float)Math.cos(Math.toRadians(0)),5000*-(float)Math.sin(Math.toRadians(0)),0},
                {(float)Math.sin(Math.toRadians(0)),(float)Math.cos(Math.toRadians(0)),0},
                {0,0,1}
        }));
        AL10.alListener3f(AL10.AL_POSITION,0,0,0);
        Assets.getSound("entrance_code").play();
    }

    float x=0,y=0;
    @Override
    public void draw(GL2 gl) {
        if(s1==null||s2==null||s3==null||s4==null) return;
//        int shaderProgram = Shaders.getShader("lighting");
//        gl.glUseProgram(shaderProgram);
//
//
//        //Get uniform locations
//        int lightPosLoc = gl.glGetUniformLocation(shaderProgram, "lightPos");
//        int lightRadiusLoc = gl.glGetUniformLocation(shaderProgram, "lightRadius");
//        int lightColorLoc = gl.glGetUniformLocation(shaderProgram, "lightColor");
//        int lightCountLoc = gl.glGetUniformLocation(shaderProgram, "lightCount");
//
//        float[] lightPositions = {
//                x,y,
//                500f,325f
//        };
//        float[] lightColors = {
//                1.0f,1.0f,0.8f,
//                0.5f,0.5f,0.4f
//        };
//        float[] lightRadii = {400f,250f};
//        int lightCount = 2;
//
//        // Set uniform values
//        gl.glUniform2fv(lightPosLoc, lightCount, lightPositions, 0);
//        gl.glUniform3fv(lightColorLoc, lightCount, lightColors, 0);
//        gl.glUniform1fv(lightRadiusLoc, lightCount, lightRadii, 0);
//        gl.glUniform1i(lightCountLoc, lightCount);

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glColor3f(1,0,0);
        gl.glVertex2f(500,100);
        gl.glColor3f(0,1,0);
        gl.glVertex2f(250,600);
        gl.glColor3f(0,0,1);
        gl.glVertex2f(750,600);
        gl.glEnd();

        s1.renderPolygon(gl);
        s2.renderPolygon(gl);
        s3.renderPolygon(gl);
        s4.renderPolygon(gl);
        gl.glUseProgram(0);
    }

    float rotate;
    @Override
    public void update() {
        if(s1==null||s2==null||s3==null||s4==null) return;
        s2.move(new Vector2(velX,velY).scale((float)Time.deltaTime));
        if(velX>0) rotate+=(float)(5*Time.deltaTime);
        if(velX<0) rotate-=(float)(5*Time.deltaTime);
        if((int)rotate%90!=0&&velX==0) {
            if((int)rotate%45==0) rotate-=(float)(5*Time.deltaTime);
            rotate+=(float)(5*(Math.signum((rotate%90)-45))*Time.deltaTime);
            s2.move(new Vector2(5*((int)Math.signum((rotate%90)-45)),0).scale((float)Time.deltaTime));
        }
        if(velY < 10) velY+=(float)Time.deltaTime;
        if(s2.sat(s1)) velY = 5;
        if(s2.sat(s3)) velY = 5;
        if(s2.sat(s4)) velY = 5;
        s2.setTransform(new Matrix3(new float[][]{
                {(float)Math.cos(Math.toRadians(rotate)),-(float)Math.sin(Math.toRadians(rotate)),0},
                {(float)Math.sin(Math.toRadians(rotate)),(float)Math.cos(Math.toRadians(rotate)),0},
                {0,0,1}
        }));
        getCamera().setTest(s2.getPos().sub(new Vector2((float) 1920 /2, (float) 1080 /2)));
    }

    @Override
    public void input(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
            ((EventListener)(Renderer.getWindow().getGLEventListener(0))).cleanupAudioDevices();
            System.exit(0);
        }
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
        x = e.getX()+getCamera().getPosition().getX();
        y = e.getY()+getCamera().getPosition().getY();
        if(Assets.getSound("entrance1")==null) return;
        Assets.getSound("entrance1").setPan((e.getX()/960.0f)-1.0f);
    }

    float velX=0,velY=0;
    boolean[] keyDown = {false,false,false,false};

    public static void main(String[] args) {
        new Main();
    }
}
