#version 120
attribute vec2 position;
varying vec2 fragPos;
varying vec3 color;

void main() {
    fragPos = position;  // Pass position to fragment shader
    color = gl_Color.rgb; // Pass vertex color
    gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 0.0, 1.0);
}