#version 330

#moj_import <minecraft:globals.glsl>

#define PI 3.1415926535

uniform sampler2D InSampler;

layout (std140) uniform WobbleConfig {
    vec2 Frequency;
    vec2 WobbleAmount;
};

in vec2 texCoord;
in vec2 oneTexel;

// rgba color with elements in range 0-1
out vec4 fragColor;

void main() {
    float time = fract(GameTime * 1200.0f);
    float xOffset = sin(texCoord.y * Frequency.x + time * PI * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + time * PI * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset);
    fragColor = texture(InSampler, texCoord + offset);
}