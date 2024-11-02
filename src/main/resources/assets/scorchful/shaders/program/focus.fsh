#version 150
#define CENTER vec2(0.5, 0.5)
uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 sampleStep;

uniform float Intensity;
uniform float Radius;
uniform float Kernel;

out vec4 fragColor;

vec4 darken(float intensity) {
    intensity = pow(intensity, Intensity);
    return texture(DiffuseSampler, texCoord) / intensity;
}

float length_squared(vec2 vec) {
    return vec.x * vec.x + vec.y * vec.y;
}

void main() {
    vec2 to_center = texCoord - CENTER;
    float len_squared = length_squared(to_center);
    if (len_squared < Radius * Radius) {
        fragColor = texture(DiffuseSampler, texCoord);
    } else {
        fragColor = darken(sqrt(len_squared) / Radius);
    }
}