#version 150

uniform sampler2D texture_sampler;
in vec2 tex_coord0, tex_coord1, tex_coord2;

uniform vec4 inputColor;
in vec2 interpolated_position;
in vec4 ap0;
in vec4 ap1;
in vec4 ap2;

out vec4 fragColor;

void main()
{
	fragColor = texture(texture_sampler, interpolated_position);
}
