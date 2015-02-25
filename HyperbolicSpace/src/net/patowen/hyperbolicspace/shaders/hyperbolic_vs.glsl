#version 150

uniform mat4 transform;
uniform mat4 perspective;

in vec3 vertex_position;
in vec3 normal_position;
in vec2 tex_coord_in;

out vec4 apparent_position;
out vec2 tex_coord;

void main()
{
	vec4 pos = vec4(vertex_position, 1.0);
	apparent_position = transform*pos;
	tex_coord = tex_coord_in;
	gl_Position = perspective*apparent_position;
}
