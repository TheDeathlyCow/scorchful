#version 150

#define PI 3.1415926535

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Time;
uniform float Frequency;
uniform float BlurRadius;

// rgba color with elements in range 0-1
out vec4 fragColor;

vec3 blur(vec2 centerPos, float radius) {
    vec3 result = vec3(0.0, 0.0, 0.0);
    int count = 0;

    for (float x = -radius; x <= radius; x++) {
        for (float y = -radius; y <= radius; y++) {
            vec2 offset = vec2(oneTexel.x * x, oneTexel.y * y);
            vec2 samplePos = centerPos + offset;
            samplePos.x = clamp(samplePos.x, 0.0, 1.0);
            samplePos.y = clamp(samplePos.y, 0.0, 1.0);
            vec3 rgb = texture(DiffuseSampler, samplePos).rgb;
            result += rgb;
            count++;
        }
    }
    return result / float(count);
}

void main() {
    float intensity = sin(Frequency * Time * PI * 2.0);
    if (intensity < 0.0) {
        // just copy colour if no blur
        fragColor = texture(DiffuseSampler, texCoord);
    } else {
        vec3 rgb = blur(texCoord, BlurRadius * intensity);
        fragColor = vec4(rgb, 1.0);
    }
}
