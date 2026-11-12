#version 330 core

in vec2 outTexCoord;
out vec4 fragColor;

uniform vec4 diffuse;
uniform sampler2D txSampler;
uniform int hasTexture;

void main()
{
    if (hasTexture == 1) {
        fragColor = texture(txSampler, outTexCoord);
    } else {
        fragColor = diffuse;
    }
}