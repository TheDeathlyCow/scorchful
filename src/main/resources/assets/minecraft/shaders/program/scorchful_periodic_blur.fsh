#version 150

#define PI 3.1415926535

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Time;
uniform float Frequency;
uniform float BlurRadius;
uniform vec2 BlurDirection;

// rgba color with elements in range 0-1
out vec4 fragColor;

vec3 blur(vec2 centerPos, vec2 direction, float radius) {
    vec3 result = vec3(0.0, 0.0, 0.0);
    int count = 0;

    for (float r = -radius; r <= radius; r += 1.0) {
        vec2 offset = vec2(oneTexel.x * r * direction.x, oneTexel.y * r * direction.y);
        vec2 samplePos = centerPos + offset;

        vec3 rgb = texture(DiffuseSampler, samplePos).rgb;

        result += rgb;
        count++;
    }
    return result / float(count);
}

void main() {
    float intensity = 1;//sin(Frequency * Time * PI * 2.0);
    if (intensity < 0.0) {
        // just copy colour if no blur
        fragColor = texture(DiffuseSampler, texCoord);
    } else {
        vec3 rgb = blur(texCoord, BlurDirection, BlurRadius * intensity);
        fragColor = vec4(rgb, 1.0);
    }
}
