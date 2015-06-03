uniform mat4 mv;
uniform mat4 projection;
uniform mat4 normalMatrix;

uniform float val_in;
varying float val;

varying vec3 position;
varying vec3 normal; //N
varying vec3 reflected; //R
varying vec3 lightVector; //L
varying vec3 view; //V
vec3 lightPosition = vec3(5,3,10); // Kann ausgedacht werden (wo soll Licht herkommen?)



void main(void)
{
	gl_Position = projection*mv * gl_Vertex;  
	normal = gl_NormalMatrix*gl_Normal; //Normalmatrix enthält nur Rotationen (keine Skalierungen etc.) damit Normalen nicht verfäscht werden
	normal = normalize(normal); 
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;
	lightVector = vec3((lightPosition - position));
	lightVector= normalize(lightVector);
	reflected = reflect(-lightVector, normal); 
	view = -position; 
	reflected=normalize(reflected);
	view=normalize(view);
	
	val = val_in; 
}

