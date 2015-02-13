#version 150

//uniform sampler2D texture;
//varying vec2 texCoordVar;

uniform vec4 inputColor;
in vec4 ap0;
in vec4 ap1;

out vec4 fragColor;

void main()
{
	if (ap0.w == 0) fragColor = vec4(1,1,1,1);
	else if (ap0.x < 0) fragColor = vec4(1,0,0,1);
	else if (ap0.x > 0) fragColor = vec4(0,1,0,1);
	else fragColor = vec4 (1,1,0,1);
	//fragColor = inputColor;
}
