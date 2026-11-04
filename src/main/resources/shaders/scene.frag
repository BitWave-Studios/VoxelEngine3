#version 330

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D txSampler;

void main() {
    fragColor = texture(txSampler, outTexCoord);
}