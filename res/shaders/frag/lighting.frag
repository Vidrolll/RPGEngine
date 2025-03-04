#version 120
varying vec2 fragPos;
varying vec3 color;

const int MAX_LIGHTS = 10;

uniform vec2 lightPos[MAX_LIGHTS];  // Light position in screen space
uniform float lightRadius[MAX_LIGHTS];  // Light radius
uniform vec3 lightColor[MAX_LIGHTS];  // Light color
uniform int lightCount;

void main() {
    vec3 finalColor = vec3(0.0);  // Start with black (no light)

    for (int i = 0; i < lightCount; i++) {
        // Distance from current light
        float dist = distance(fragPos, lightPos[i]);

        // Light intensity using smoothstep (smoother than linear)
        float intensity = 1.0 - smoothstep(0.0, lightRadius[i], dist);
        intensity = max(intensity,0.02f);

        // Accumulate light contributions
        finalColor += color * intensity * lightColor[i];
    }

    // Ensure colors don't exceed 1.0
    gl_FragColor = vec4(clamp(finalColor, 0.0, 1.0), 1.0);
}