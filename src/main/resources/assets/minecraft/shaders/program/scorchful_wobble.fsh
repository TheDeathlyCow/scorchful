#version 150

#define PI 3.1415926535

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Time;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;

// rgba color with elements in range 0-1
out vec4 fragColor;

void main() {
    float xOffset = sin(texCoord.y * Frequency.x + Time * PI * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + Time * PI * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset);
    fragColor = texture(DiffuseSampler, texCoord + offset);
}