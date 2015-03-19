#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec4 apparent_position[];
out vec2 interpolated_position;
out vec3 f_interpolated_pos;
out vec3 f_pos[3];

in vec2 tex_coord[];
out vec2 f_texcoord[3];

void main()
{
	f_pos[0] = apparent_position[0].xyz;
	f_pos[1] = apparent_position[1].xyz;
	f_pos[2] = apparent_position[2].xyz;

	f_texcoord[0] = tex_coord[0];
	f_texcoord[1] = tex_coord[1];
	f_texcoord[2] = tex_coord[2];
	
	gl_Position = gl_in[0].gl_Position+vec4(.00001,0,0,0);
	interpolated_position = tex_coord[0];
	f_interpolated_pos = apparent_position[0].xyz;
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position+vec4(.00001,0,0,0);
	interpolated_position = tex_coord[1];
	f_interpolated_pos = apparent_position[1].xyz;
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position+vec4(.00001,0,0,0);
	interpolated_position = tex_coord[2];
	f_interpolated_pos = apparent_position[2].xyz;
	EmitVertex();
	
	EndPrimitive();
}
