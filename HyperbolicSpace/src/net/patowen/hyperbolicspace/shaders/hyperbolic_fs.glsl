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
	/*float xx;
	if (int(gl_FragCoord.x) % 3 == 0) xx = ap0.x;
	else if (int(gl_FragCoord.x) % 3 == 1) xx = ap1.x;
	else xx = ap2.x;*/
	
	/*if (xx < 0) fragColor = vec4(1,0,0,1);
	else if (xx > 0) fragColor = vec4(0,1,0,1);
	else fragColor = vec4 (1,1,0,1);*/
	fragColor = texture(texture_sampler, interpolated_position);
	//fragColor = vec4(interpolated_position, 0, 1);
}
