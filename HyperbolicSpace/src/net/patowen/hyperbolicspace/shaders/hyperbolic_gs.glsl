#version 150

layout(lines) in;
layout(line_strip, max_vertices = 2) out;

in vec4 apparent_position[];
out vec4 ap0;
out vec4 ap1;

void main()
{
	ap0 = apparent_position[0];
	ap1 = apparent_position[1];
	
	gl_Position = gl_in[0].gl_Position + vec4(0,0,-0.03125,0);
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + vec4(0,0,-0.03125,0);
	EmitVertex();
	
	EndPrimitive();
}
