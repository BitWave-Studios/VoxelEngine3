#version 330 core

in vec2 vTexCoord;
in vec4 vColor;
in vec3 vShape;

uniform sampler2D uTexture;

out vec4 fragColor;

void main() {
    vec4 texel = texture(uTexture, vTexCoord);
    float radius = vShape.z;

    if (radius > 0.0) {
        vec2 halfSize = vShape.xy;
        vec2 p = (vTexCoord * 2.0 - 1.0) * halfSize;
        vec2 q = abs(p) - (halfSize - vec2(radius));

        float distance = length(max(q, vec2(0.0))) + min(max(q.x, q.y), 0.0) - radius;
        float aa = fwidth(distance);
        float coverage = 1.0 - smoothstep(-aa, aa, distance);

        texel.a *= coverage;
    }

    fragColor = vColor * texel;
}
