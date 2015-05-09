#version 150

uniform sampler2D texture_sampler;
in vec2 f_texcoord[3];

uniform vec4 color;

in vec2 interpolated_position;
in vec3 f_interpolated_pos;
in vec3 f_pos[3];

out vec4 fragColor;

vec3 klein_to_poincare(vec3 klein)
{
	return klein / (1 + sqrt(max(0, 1-dot(klein, klein))));
}

vec3 poincare_to_klein(vec3 poincare)
{
	return (2*poincare) / (1 + dot(poincare, poincare));
}

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
	vec3 center = klein_to_poincare(f_interpolated_pos);
	vec3 trans = -center;
	
	vec3 pos[3];
	pos[0] = poincare_translate(klein_to_poincare(f_pos[0]), trans);
	pos[1] = poincare_translate(klein_to_poincare(f_pos[1]), trans)-pos[0];
	pos[2] = poincare_translate(klein_to_poincare(f_pos[2]), trans)-pos[0];
	center = -pos[0];
	
	mat3 transform1 = inverse(mat3(pos[1], pos[2], cross(pos[1],pos[2])));
	mat3 transform2 = mat3(f_texcoord[1]-f_texcoord[0], 0, f_texcoord[2]-f_texcoord[0], 0, 0, 0, 0);
	fragColor = color * texture(texture_sampler, (transform2*transform1*center).st + f_texcoord[0]);
}
