#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec4 apparent_position[];
out vec2 interpolated_position;
out vec4 ap0;
out vec4 ap1;
out vec4 ap2;

in vec2 tex_coord[];
out vec2 tex_coord0, tex_coord1, tex_coord2;

void main()
{
	ap0 = apparent_position[0];
	ap1 = apparent_position[1];
	ap2 = apparent_position[2];
	
	tex_coord0 = tex_coord[0];
	tex_coord1 = tex_coord[1];
	tex_coord2 = tex_coord[2];
	
	gl_Position = gl_in[0].gl_Position;
	interpolated_position = tex_coord[0];
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position;
	interpolated_position = tex_coord[1];
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position;
	interpolated_position = tex_coord[2];
	EmitVertex();
	
	EndPrimitive();
}
