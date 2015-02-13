#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec4 apparent_position[];
out vec4 ap0;
out vec4 ap1;
out vec4 ap2;

void main()
{
	ap0 = apparent_position[0];
	ap1 = apparent_position[1];
	ap2 = apparent_position[2];
	
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	
	EndPrimitive();
}
