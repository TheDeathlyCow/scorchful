#version 330

#moj_import <minecraft:globals.glsl>

#define PI 3.1415926535

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout (std140) uniform BlurConfig {
    float Period;
    float BlurRadius;
    vec2 BlurDir;
};

in vec2 texCoord;

// rgba color with elements in range 0-1
out vec4 fragColor;

vec3 blur(vec2 centerPos, vec2 direction, float radius) {
    vec2 oneTexel = 1.0 / InSize;
    vec2 sampleStep = oneTexel * BlurDir;
    vec3 result = vec3(0.0, 0.0, 0.0);
    int count = 0;

    for (float r = -radius; r <= radius; r += 1.0) {
        vec2 offset = vec2(sampleStep.x * r * direction.x, sampleStep.y * r * direction.y);
        vec2 samplePos = centerPos + offset;

        vec3 rgb = texture(InSampler, samplePos).rgb;

        result += rgb;
        count++;
    }
    return result / float(count);
}

void main() {
    float time = fract(GameTime * 1200.0f / Period);

    float intensity = sin(time * PI * 2.0);
    if (intensity < 0.0) {
        // just copy colour if no blur
        fragColor = texture(InSampler, texCoord);
    } else {
        vec3 rgb = blur(texCoord, BlurDir, BlurRadius * intensity);
        fragColor = vec4(rgb, 1.0);
    }
}
