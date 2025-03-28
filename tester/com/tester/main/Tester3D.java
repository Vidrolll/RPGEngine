package com.tester.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.graphics.objects.OBJLoader;
import com.rpg.main.math.vector.Matrix4;
import com.rpg.main.math.vector.Vector4;

import java.nio.FloatBuffer;
import java.util.List;

public class Tester3D extends Game {
    boolean db = false;
    private int vaoId, vboId;

    Vector4[][] hedron = {
            {
                new Vector4(-100,-100,-100,1),
                new Vector4(100,-100,-100,1),
                new Vector4(100,100,-100,1),
                new Vector4(-100,100,-100,1)
            },
            {
                new Vector4(-100,-100,100,1),
                new Vector4(100,-100,100,1),
                new Vector4(100,100,100,1),
                new Vector4(-100,100,100,1)
            }
    };
    int[][] faces = {
            {0,0,0,1,0,2,0,3},
            {1,0,1,1,1,2,1,3},
            {0,0,1,0,1,1,0,1},
            {0,1,1,1,1,2,0,2},
            {0,2,1,2,1,3,0,3},
            {0,0,1,0,1,3,0,3},
    };
    Matrix4 mat = new Matrix4(new float[][]{
            {1,0,0,0},
            {0,1f,0,0},
            {0,0,1f,0},
            {0,0,0,1}
    });

    public Tester3D() {

    }

    public void perspectiveDivide(Vector4 vec) {
        if(vec.getW() != 0) {
            vec.setX(vec.getX()/vec.getW());
            vec.setY(vec.getY()/vec.getW());
            vec.setZ(vec.getZ()/vec.getW());
        }
    }
    private float[] transformedVertices;

    @Override
    public void draw(GL2 gl) {
        List<Vector4> vertices = OBJLoader.loadWireframeOBJ("res/monkey.obj");
        if (vertices == null) {
            System.err.println("Failed to load OBJ file!");
            return;
        }

        transformedVertices = new float[vertices.size() * 3];

        // Apply matrix transformations
        for (int i = 0; i < vertices.size(); i++) {
            Vector4 transformed = mat.mul(vertices.get(i)); // Apply your matrix
            perspectiveDivide(transformed); // Divide by w for perspective projection
            transformedVertices[i * 3] = transformed.getX();
            transformedVertices[i * 3 + 1] = transformed.getY();
            transformedVertices[i * 3 + 2] = transformed.getZ();
        }

        int[] buffers = new int[2];
        gl.glGenVertexArrays(1, buffers, 0);
        vaoId = buffers[0];
        gl.glGenBuffers(1, buffers, 1);
        vboId = buffers[1];

        gl.glBindVertexArray(vaoId);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboId);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, transformedVertices.length * Float.BYTES,
                FloatBuffer.wrap(transformedVertices), GL.GL_STATIC_DRAW);

        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3 * Float.BYTES, 0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
        // Render loop
//        gl.glBegin(GL2.GL_LINES);
//        for (int i = 0; i < 8; i++) {
//            int v = i % 4, f = i / 4;
//            gl.glColor3f(1, 1, 1);
//
//            Vector4 transformed1 = mat.mul(hedron[f][v]);
//            Vector4 transformed2 = mat.mul(hedron[f][(v + 1) % 4]);
//            Vector4 transformed3 = mat.mul(hedron[f][v]);
//            Vector4 transformed4 = mat.mul(hedron[(f + 1) % 2][v]);
//
//            perspectiveDivide(transformed1);
//            perspectiveDivide(transformed2);
//            perspectiveDivide(transformed3);
//            perspectiveDivide(transformed4);
//
//            // Apply perspective divide here if needed (for 3D to 2D conversion)
//            gl.glVertex3f(transformed1.getX(), transformed1.getY(), 0);
//            gl.glVertex3f(transformed2.getX(), transformed2.getY(), 0);
//            gl.glVertex3f(transformed3.getX(), transformed3.getY(), 0);
//            gl.glVertex3f(transformed4.getX(), transformed4.getY(), 0);
//        }
//        gl.glEnd();
//        gl.glFlush();

        gl.glBindVertexArray(vaoId);
        gl.glDrawArrays(GL.GL_LINES, 0, transformedVertices.length / 3);
        gl.glBindVertexArray(0);

        // Render the object
//        gl.glBegin(GL2.GL_LINES);
//        for (int i = 0; i < 8; i++) {
//            int v = i % 4, f = i / 4;
//            gl.glColor3f(1, 1, 1);
//
//            // Get the transformed vertices using the full transformation matrix
//            Vector3 transformed1 = mat.mul(hedron[f][v]);
//            Vector3 transformed2 = mat.mul(hedron[f][(v + 1) % 4]);
//
//            // Use transformed 3D coordinates for lines (without manual perspective divide)
//            gl.glVertex3f(transformed1.getX(), transformed1.getY(), transformed1.getZ());
//            gl.glVertex3f(transformed2.getX(), transformed2.getY(), transformed2.getZ());
//        }
//        gl.glEnd();
//
//        gl.glFlush();

// Same for faces:
//        for (int i = 0; i < faces.length; i++) {
//            float color = 1.0f - ((float) i / (faces.length));
//            gl.glColor4f(color, color, color, 0.8f);
//            int[] f = faces[i];
//            gl.glBegin(GL2.GL_POLYGON);
//
//            // Loop through each face and apply the same transformation and perspective divide
//            for (int j = 0; j < f.length; j += 2) {
//                Vector3 transformedFace = mat.mul(hedron[f[j]][f[j + 1]]);
//                float w = transformedFace.getZ();
//                float x = transformedFace.getX() / w;
//                float y = transformedFace.getY() / w;
//
//                // Debug logging for the face vertices
//                System.out.println("Face Vertex: " + transformedFace + " -> (" + x + ", " + y + ")");
//
//                gl.glVertex3f(x, y, 0);  // Perspective divide applied
//            }
//
//            gl.glEnd();
//            gl.glFlush();
//        }
    }

    int t = 0;
    @Override
    public void update() {
        t++;
        double r = Math.toRadians(t);

        Matrix4 translationMatrix = new Matrix4(new float[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, -3},
                {0,0,0,1}
        });

        // Create a rotation matrix for the x-axis (already in your code)
        Matrix4 rotationX = new Matrix4(new float[][]{
                {(float)Math.cos(r), 0, (float)Math.sin(r), 0},
                {0, 1, 0, 0},
                {-(float)Math.sin(r), 0, (float)Math.cos(r), 0},
                {0, 0, 0, 1f}
        });
        // Create a rotation matrix for the y-axis (already in your code)
        Matrix4 rotationY = new Matrix4(new float[][]{
                {1, 0, 0, 0},
                {0, (float)Math.cos(r / 2), -(float)Math.sin(r / 2), 0},
                {0, (float)Math.sin(r / 2), (float)Math.cos(r / 2), 0},
                {0, 0, 0, 1}
        });
        // Create a rotation matrix for the z-axis (already in your code)
        Matrix4 rotationZ = new Matrix4(new float[][]{
                {(float)Math.cos(r / 4), -(float)Math.sin(r / 4), 0, 0},
                {(float)Math.sin(r / 4), (float)Math.cos(r / 4), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        Matrix4 perspectiveMatrix = Matrix4.perspective(60.0f, 16.0f / 9.0f, 0.1f, 1000.0f);
//        mat = perspectiveMatrix.mul(translationMatrix).mul(rotationX).mul(rotationY).mul(rotationZ);
        mat = translationMatrix.mul(rotationX).mul(rotationY).mul(rotationZ);
        mat = perspectiveMatrix.mul(mat);
    }

    @Override
    public void input(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);
    }

    @Override
    public void input(MouseEvent e) {

    }

    public static void main(String[] args) {
        new Tester3D();
    }
}
