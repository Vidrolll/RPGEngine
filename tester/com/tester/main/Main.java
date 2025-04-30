package com.tester.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.graphics.opengl.EventListener;
import com.rpg.main.graphics.opengl.Renderer;
import com.rpg.main.level.Level;
import com.rpg.main.level.LevelLoader;
import com.rpg.main.util.Assets;

public class Main extends Game {
    Level level;

    public Main() {
//        AL10.alListener3f(AL10.AL_POSITION,0,0,0);
//        Assets.getSound("entrance_code").play();
        level = LevelLoader.loadLevel("test");
    }

    float x=0,y=0;

    public static void main(String[] args) {
        new Main();

//        for(int j = 0; j < 10; j++) {
//            Function<Double,Double> function = Math::sin;
//            System.out.println(Calculus.integrateTrapezoidal(function, 0, Math.PI, 1000000));
//            System.out.println(Calculus.integrateTrapezoidal2(function, 0, Math.PI, 1000000));
//            double averageTimeA=0,averageTimeB=0;
//            for(int i = 0; i < 10; i++) {
//                long time1 = System.nanoTime();
//                Calculus.integrateTrapezoidal(function, 0, Math.PI, 1000000);
//                averageTimeA+=System.nanoTime()-time1;
//                long time2 = System.nanoTime();
//                Calculus.integrateTrapezoidal2(function, 0, Math.PI, 1000000);
//                averageTimeB+=System.nanoTime()-time2;
//            }
//            System.out.println("Average Time A: " + (averageTimeA/100000));
//            System.out.println("Average Time B: " + (averageTimeB/100000));
//        }
    }

    float rotate;
    @Override
    public void update() {
        if (level != null) level.update();
    }

    @Override
    public void input(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
            ((EventListener)(Renderer.getWindow().getGLEventListener(0))).cleanupAudioDevices();
            System.exit(0);
        }
        if (level != null) level.input(e);
    }

    @Override
    public void input(MouseEvent e) {
        x = e.getX() + CAMERA.getPosition().getX();
        y = e.getY() + CAMERA.getPosition().getY();
        if (level != null) level.input(e);
        if(Assets.getSound("entrance1")==null) return;
        Assets.getSound("entrance1").setPan((e.getX()/960.0f)-1.0f);
    }

    @Override
    public void draw(GL2 gl) {
//        int shaderProgram = Shaders.getShader("lighting");
//        gl.glUseProgram(shaderProgram);
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

        if (level != null) level.render(gl);

        //gl.glUseProgram(0);
    }
}
