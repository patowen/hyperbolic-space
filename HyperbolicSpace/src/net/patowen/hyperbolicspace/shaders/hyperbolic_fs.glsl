#version 150

uniform sampler2D texture_sampler;
in vec2 f_texcoord[3];

uniform vec4 color;

in vec2 interpolated_position;
in vec4 f_interpolated_pos;
in vec4 f_pos[3];

out vec4 fragColor;

void main()
{
	fragColor = color * texture(texture_sampler, interpolated_position);
}
