#version 120

varying vec3 vColor; // Interpolated color from vertex shader

void main() {
    // Output the color based on the vertex shader calculation
    gl_FragColor = vec4(vColor, 1.0); // Set full opacity
}