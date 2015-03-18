#version 150

uniform mat4 transform;
uniform mat4 perspective;

in vec3 vertex_position;
in vec3 normal_position;
in vec2 tex_coord_in;

out vec4 apparent_position;
out vec2 tex_coord;

vec3 poincare_translate(vec3 point, vec3 trans)
{
	float point_sqr = dot(point, point);
	float trans_sqr = dot(trans, trans);
	float product = 2*dot(point, trans);
	float denom = point_sqr*trans_sqr + product + 1;
	float trans_factor = 1 + point_sqr + product;
	float point_factor = 1 - trans_sqr;
	
	return (point*point_factor + trans*trans_factor) / denom;
}

void main()
{
	tex_coord = tex_coord_in;
	
	vec3 pos = (2*vertex_position)/(1+dot(vertex_position, vertex_position));
	apparent_position = transform*vec4(pos, 1.0);
	gl_Position = perspective*apparent_position;
}
