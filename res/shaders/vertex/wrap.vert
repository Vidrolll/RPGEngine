#version 120

attribute vec2 aPosition; // Input vertex positions
varying vec3 vColor; // Pass color to fragment shader
uniform float uTime; // A time uniform to animate the effect

void main() {
    // Create a wrapping effect on the x position
    float wrapAmount = 0.1; // Control the amount of wrapping
    float wrappedX = aPosition.x + sin(aPosition.y * 10.0 + uTime) * wrapAmount;

    // Set the transformed vertex position
    gl_Position = gl_ModelViewProjectionMatrix * vec4(wrappedX, aPosition.y, 0.0, 1.0);

    // Create a color based on the wrapped position
    // Adjust the color calculation to visualize the wrapping effect
    float colorValue = wrappedX * 0.5 + 0.5; // Normalize to [0, 1]
    vColor = vec3(colorValue, aPosition.y * 0.5 + 0.5, 0.5); // Use varying y for color
}