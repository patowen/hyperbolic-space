#version 150

//uniform sampler2D texture;
//varying vec2 texCoordVar;

uniform vec4 inputColor;
out vec4 fragColor;

void main()
{
	fragColor = inputColor;
}
