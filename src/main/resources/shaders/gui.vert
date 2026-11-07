#version 330

layout(location = 0) in vec2 inPos;
layout(location = 1) in vec2 inTexCoord;
layout(location = 2) in vec4 inColor;

out vec2 fragTexCoord;
out vec4 fragColor;

uniform mat4 projectionMatrix;

void main() {
    fragTexCoord = inTexCoord;
    fragColor = inColor;
    gl_Position = projectionMatrix * vec4(inPos, 0.0, 1.0);
}