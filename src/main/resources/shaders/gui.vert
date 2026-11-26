#version 330 core

layout(location = 0) in vec2 aPosition;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in vec4 aColor;
layout(location = 3) in vec3 aShape;

uniform mat4 uProjection;

out vec2 vTexCoord;
out vec4 vColor;
out vec3 vShape;


void main() {
    vTexCoord = aTexCoord;
    vColor = aColor;
    vShape = aShape;

    gl_Position = uProjection * vec4(aPosition, 0.0, 1.0);
}
