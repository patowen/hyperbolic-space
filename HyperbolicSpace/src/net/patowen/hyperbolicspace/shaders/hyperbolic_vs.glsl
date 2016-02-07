#version 150

uniform mat4 transform;
uniform mat4 perspective;

in vec4 vertex_position;
in vec4 normal_position;
in vec2 tex_coord_in;

out vec4 apparent_position;
out vec2 tex_coord;

void main()
{
	tex_coord = tex_coord_in;
	
	vec4 pos = transform*vertex_position;
	apparent_position = pos;
	gl_Position = perspective*pos;
}
