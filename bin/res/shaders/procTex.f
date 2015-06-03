vec4 newColor; 


varying vec3 position;
varying vec3 normal; //N
varying vec3 reflected; //R
varying vec3 lightVector; //L
varying vec3 view; //V

varying float val;



vec4 red()
{
	newColor = vec4(1,0,0,1);
	return newColor; 
}

vec4 green()
{
	newColor = vec4(0,1,0,1);
	return newColor; 
}

vec4 blue()
{
	newColor = vec4(0,0,1,1);
	return newColor; 
}

vec4 texture()
{
//	newColor = vec4(val,val,val,1);
	newColor = vec4(val*0.7,val/5,val/5,1);
	return newColor; 
}


void main(void)
{
	
	
	float Ia = 0.3;
	float Id = max(0.0, dot(normal, lightVector)*0.1); 
	float Is = pow(max(0.0, dot(reflected, view)), 10.0)*0.3;    
	
	float Il = 3.0; //Skalieren
		
	float I = Il*(Ia + Id + Is); 
	
	gl_FragColor = I*texture(); 
	 
	
	
	
}

