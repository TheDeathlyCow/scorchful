#version 150

#define PI 3.1415926535

uniform sampler2D DiffuseSampler;

// current texture coordinate
in vec2 texCoord;
// size of 1 pixel in input buffer.
in vec2 oneTexel;

uniform float Time;
uniform float Frequency;
uniform float BlurAmount;

// rgba color with elements in range 0-1
out vec4 fragColor;

vec3 blur(vec2 center, float intensity) {

    vec3 rgb = texture(DiffuseSampler, center).rgb;

    return rgb * intensity;
}

void main() {

    float intensity = sin(Frequency + Time * PI * 2.0);
    if (intensity < 0.0) {
        intensity = 0.0;
    }

    vec3 rgb = blur(texCoord, intensity);
    fragColor = vec4(rgb, 1.0);
}
