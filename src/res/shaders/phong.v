uniform mat4 mv;
uniform mat4 projection;
uniform mat4 normalMatrix;

varying vec4 color;
varying vec2 uv;
varying vec3 position;
varying vec3 normal; //N
varying vec3 reflected; //R
varying vec3 lightVector; //L
varying vec3 view; //V
vec3 lightPosition = vec3(0,0,100); // Kann ausgedacht werden (wo soll Licht herkommen?)



void main(void)
{
	color=gl_Color; //Magische Konstante (Farbe aus dem Objekt)
	uv = gl_MultiTexCoord0.xy; 
	 

    gl_Position = projection* mv * gl_Vertex; 
    
	

	
	//normal = gl_NormalMatrix*gl_Normal; //Normalmatrix enthält nur Rotationen (keine Skalierungen etc.) damit Normalen nicht verfäscht werden
	normal = vec3(normalMatrix*vec4(gl_Normal,1)); //Normalmatrix enthält nur Rotationen (keine Skalierungen etc.) damit Normalen nicht verfäscht werden
	normal = normalize(normal); 
	
	position = (mv * gl_Vertex).xyz;
	
	lightVector = vec3((lightPosition - position));
	lightVector= normalize(lightVector);
	reflected = reflect(-lightVector, normal); 
	view = -position; 
	
	
	reflected=normalize(reflected);
	
	view=normalize(view);
}

