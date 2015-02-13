#version 150

uniform mat4 transform;
in vec3 vertex_position;
out vec4 apparent_position;

void main()
{
	vec4 pos = vec4(vertex_position, 1.0);
	apparent_position = transform*pos;
	gl_Position = apparent_position;
}
