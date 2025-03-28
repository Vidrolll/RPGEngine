package com.rpg.main.graphics;

import com.jogamp.opengl.GL2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Shaders {
    private static final HashMap<String,Integer> shaderPrograms = new HashMap<>();

    public static int getShader(String shader) {
        return shaderPrograms.get(shader);
    }

    /**
     * Creates a new shader.
     *
     * @param gl     (GL2) The OpenGL object to utilize to create the shader.
     * @param shader (String) The name of the shader to initialize, should be the same
     *               as the names of the vertex and fragment shaders in the files.
     */
    public static void createShader(GL2 gl, String shader) {
        int[] shaderSupport = new int[1];
        gl.glGetIntegerv(GL2.GL_MAX_VERTEX_UNIFORM_COMPONENTS, shaderSupport, 0);
        System.out.println("Max Vertex Uniform Components: " + shaderSupport[0]);

        int shaderProgram = gl.glCreateProgram();
        int vertexShader = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
        int fragmentShader = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

        String vertexShaderSource = readShaderSource("shaders/vertex/"+shader+".vert");
        String fragmentShaderSource = readShaderSource("shaders/frag/"+shader+".frag");

        gl.glShaderSource(vertexShader, 1, new String[]{vertexShaderSource}, null);
        gl.glCompileShader(vertexShader);
        printErrors(gl,vertexShader,"Vertex");

        gl.glShaderSource(fragmentShader, 1, new String[]{fragmentShaderSource}, null);
        gl.glCompileShader(fragmentShader);
        printErrors(gl,fragmentShader,"Fragment");

        gl.glAttachShader(shaderProgram, vertexShader);
        gl.glAttachShader(shaderProgram, fragmentShader);

        // Check if shaders exist before linking
        int[] attachedShaders = new int[1];
        gl.glGetProgramiv(shaderProgram, GL2.GL_ATTACHED_SHADERS, attachedShaders, 0);
        System.out.println("Attached Shaders: " + attachedShaders[0]);

        gl.glLinkProgram(shaderProgram);
        checkProgramErrors(gl, shaderProgram); // Check for link errors
        gl.glValidateProgram(shaderProgram);
        checkProgramErrors(gl, shaderProgram); // Check for validation errors
        shaderPrograms.put(shader,shaderProgram);
    }

    /**
     * Prints any errors detected when creating a new shader.
     * @param gl (GL2) The OpenGL object to utilize to create the shader.
     * @param shader (int) The OpenGL index of the shader.
     * @param shaderType (String) The type of shader, either fragment or vertex.
     */
    private static void printErrors(GL2 gl, int shader, String shaderType) {
        int[] compiled = new int[1];
        gl.glGetShaderiv(shader, GL2.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == GL2.GL_FALSE) {
            System.out.println(shaderType + " shader compilation failed.");

            int[] logLength = new int[1];
            gl.glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

            if (logLength[0] > 0) {
                byte[] log = new byte[logLength[0]];
                gl.glGetShaderInfoLog(shader, logLength[0], null, 0, log, 0);
                System.out.println(shaderType + " Shader Log: " + new String(log));
            }
        }
    }

    /**
     * Detects if there were any errors in creation of a shader program.
     * @param gl (GL2) The OpenGL object to utilize to create the shader.
     * @param program (int) The OpenGL index of the shader program.
     */
    private static void checkProgramErrors(GL2 gl, int program) {
        int[] linked = new int[1];
        gl.glGetProgramiv(program, GL2.GL_LINK_STATUS, linked, 0);
        if (linked[0] == GL2.GL_FALSE) {
            System.out.println("Shader program linking failed.");

            int[] logLength = new int[1];
            gl.glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
            if (logLength[0] > 0) {
                byte[] log = new byte[logLength[0]];
                gl.glGetProgramInfoLog(program, logLength[0], null, 0, log, 0);
                System.out.println("Program Log: " + new String(log));
            }
        }
    }

    /**
     * Reads the entirety of a shader file and returns it as a formatted String.
     * @param path (String) The path of the shader file to read.
     * @return (String) The entire file as a single String.
     */
    private static String readShaderSource(String path) {
        StringBuilder source = new StringBuilder();

        try (InputStream inputStream = Shaders.class.getClassLoader().getResourceAsStream(path)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    source.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading shader file: " + path);
            throw new RuntimeException(e);
        }

        return source.toString();
    }

}
