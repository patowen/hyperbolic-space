#version 150

#define NUM_LIGHTS 1

uniform sampler2D texture_sampler;
uniform vec4 color;

in vec4 vertex;
in vec4 normal;
in vec2 tex_coord;

uniform mat4 transform;

//Lighting
uniform vec3 light_ambient;
uniform vec4 light_position[NUM_LIGHTS];
uniform vec3 light_diffuse[NUM_LIGHTS];

//Material
uniform vec3 material_ambient;
uniform vec3 material_diffuse;

out vec4 fragColor;

vec4 normalize_as_position(vec4 input) {
	vec3 xyz = input.xyz;
	float w = input.w;
	float dist = -dot(xyz, xyz) + w*w;
	return input * (1/sqrt(dist));
}

vec4 normalize_as_direction(vec4 input) {
	vec3 xyz = input.xyz;
	float w = input.w;
	float dist = dot(xyz, xyz) - w*w;
	return input * (1/sqrt(dist));
}

float hypdot(vec4 v1, vec4 v2) {
	return -dot(v1.xyz, v2.xyz) + (v1.w * v2.w);
}

void main() {
	vec4 apos = transform*vertex;
	if (length(apos.xyz) < 0.1*apos.w) {
		discard;
	}
	vec3 color_multiplier = light_ambient * material_ambient;
	vec4 norm = normalize_as_direction(normal);
	vec4 vert = normalize_as_position(vertex);
	
	float ddd = 0;
	
	for (int i=0; i<NUM_LIGHTS; i++) {
		vec4 light_direction = light_position[i] - vert;
		light_direction -= vert * hypdot(light_direction, vert);
		light_direction = normalize_as_direction(light_direction); //TODO: Get some distance measure for attenuation
		
		float directness = abs(hypdot(light_direction, norm));
		ddd = directness;
		
		if (directness > 0) {
			color_multiplier += directness * light_diffuse[i] * material_diffuse;
		}
	}
	
	fragColor = vec4(color_multiplier, 1) * texture(texture_sampler, tex_coord);
}

/*
#version 150

#define NUM_LIGHTS 1

uniform sampler2D texture_sampler;

in vec3 vertex;
in vec2 tex_coord;
in vec3 normal;

//Lighting
uniform vec3 viewer_position;
uniform vec3 light_ambient;
uniform vec3 light_position[NUM_LIGHTS];
uniform vec3 light_diffuse[NUM_LIGHTS];

//Material
uniform vec3 material_ambient;
uniform vec3 material_diffuse;
uniform vec3 material_emission;
uniform float material_transparency;

uniform vec2 noise_displacement;

out vec4 fragColor;

void main() {
	vec3 color_multiplier = light_ambient * material_ambient + material_emission;
	vec3 norm = normalize(normal);
	
	for (int i=0; i<NUM_LIGHTS; i++) {
		vec3 light_direction = light_position[i] - vertex;
		float light_distance = length(light_direction);
		light_direction /= light_distance;
		
		float directness = dot(light_direction, norm);
		
		if (directness > 0) {
			color_multiplier += directness * light_diffuse[i] * material_diffuse * attenuation;
		}
	}
	
	fragColor = vec4(color_multiplier, material_transparency)*texture(texture_sampler, tex_coord);
}
*/