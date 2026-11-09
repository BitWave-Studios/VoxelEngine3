#version 330 core

in vec2 outTextCoord;
out vec4 fragColor;

uniform vec4 diffuse;
uniform sampler2D txSampler;
uniform int hasTexture;

void main()
{
    if (hasTexture == 1) {
        fragColor = texture(txSampler, outTextCoord);
    } else {
        fragColor = diffuse;
    }
}