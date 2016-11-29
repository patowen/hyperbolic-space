#version 150

uniform mat4 transform;
uniform mat4 perspective;

in vec4 vertex_in;
in vec4 normal_in;
in vec2 tex_coord_in;

out vec4 vertex;
out vec4 normal;
out vec2 tex_coord;

void main()
{
	vertex = vertex_in;
	tex_coord = tex_coord_in;
	normal = normal_in;
	gl_Position = perspective*transform*vertex_in;
}
