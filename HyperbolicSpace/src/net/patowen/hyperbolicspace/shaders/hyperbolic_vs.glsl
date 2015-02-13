#version 150

uniform mat4 transform;
in vec3 vertex_position;

void main()
{
	vec4 pos = vec4(vertex_position, 1.0);
	gl_Position = transform*pos;
}
