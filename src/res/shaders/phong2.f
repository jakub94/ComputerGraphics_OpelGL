varying vec4 color;
vec4 newColor; 
varying vec2 uv;
uniform sampler2D sampler;

varying vec3 position;
varying vec3 normal; //N
varying vec3 reflected; //R
varying vec3 lightVector; //L
varying vec3 view; //V



void main(void)
{
	
	
	float Ia = 0.2;
	float Id = max(0.0, dot(normal, lightVector)*0.45); 
	float Is = pow(max(0.0, dot(reflected, view)), 10.0)*0.35;    
	
	float Il = 1.0; //Skalieren
		
	float I = Il*(Ia + Id + Is);  
	
	
	gl_FragColor = I*(texture2D(sampler, uv)); 
	 
	
	
	
}

