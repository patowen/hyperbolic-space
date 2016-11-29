#version 150

uniform sampler2D texture_sampler;
uniform vec4 color;

in vec3 vertex;
in vec3 normal;
in vec2 tex_coord;

out vec4 fragColor;

void main()
{
	fragColor = color * texture(texture_sampler, tex_coord);
}
