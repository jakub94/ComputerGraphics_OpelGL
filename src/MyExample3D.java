import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

public class MyExample3D extends AbstractSimpleBase {



	//Scaling
	float s = 1; 
	boolean growing = true; 

	//System-Time 
	long lastTime = 0;
	long diff; 
	long diffSum = 0;

	//Cube Rotation
	float angleX = 0;
	float angleY = 0;
	float rotateAngle = 1;
	
	//Cube Translation
	float transVal = 1; 
	
	//Pyramide Rotation
	float angle = 0; 
	float degreePerSec = 20; 
	

	//Keys
	boolean rightArr, leftArr, upArr, downArr, horizontalArr, verticalArr, spaceKey, keyA, keyS, keyD, keyW, keyShift, keyPlus, keyMinus, keyOne, keyTwo, keyThree;

	//Mouse
	int dx;
	int dy;
	int mouseX;
	int mouseY;
	boolean rMouse;
	boolean lMouse;

	//Textures
	Texture wall; 
	//Shaders
	ShaderProgram phong, phong2, procTex;
	//Shader Variables
	float colorChange = 0;
	float changePerSec = 0.1f; 
	boolean actualValueVariableIsGrowing = true; 

	//Objects
	ArrayList<Cube> cubes = new ArrayList<Cube>(); 
	ArrayList<Pyramide> pyramides = new ArrayList<Pyramide>();

	//Matrices
			Matrix4f projection; 
			Matrix4f mv;



	public static void main(String[] args)
	{
		new MyExample3D().start();
	}
	
	private void setUpProjectionMatrix()
	{
		float zNear = 1;
		float zFar = 100;		
		float left = -1.6f;
		float right = 1.6f;
		float bottom = -0.9f;
		float top = 0.9f;
		
		float width = right - left;
		float height = top - bottom; 
		
		
		projection = new Matrix4f();
		projection.m00=2*zNear/width;
		projection.m11=2*zNear/height;
		projection.m22=-((zFar + zNear)/(zFar - zNear));
		projection.m23=-1;
		projection.m32=-((2*zFar*zNear)/(zFar-zNear));
		projection.m33=0;
		
		passMatrixToShader(phong, "projection", projection);
		passMatrixToShader(phong2, "projection", projection);
		passMatrixToShader(procTex, "projection", projection);
		
	}
	
	private void passMatrixToShader(ShaderProgram shader, String matrixName, Matrix4f matrix )
	{
		glUseProgram(shader.getId()); 
		FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16); 
		matrix.store(mvpBuffer); 
		mvpBuffer.flip(); 
		glUniformMatrix4(
				glGetUniformLocation(shader.getId(), matrixName),
				false, mvpBuffer);
	}
	
	private void passModelViewMatrixToShader(ShaderProgram shader, String matrixName, Matrix4f matrix )
	{
		passMatrixToShader(shader, matrixName, matrix);
		matrixName = "normalMatrix"; 
		FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16); 
		matrix.invert().store(mvpBuffer);  // Invertiere ModelView
		mvpBuffer.flip(); 
		glUniformMatrix4(
				glGetUniformLocation(shader.getId(), matrixName),
				true, mvpBuffer); // true --> ModelView Matrix wird transponiert --> NormalMatrix
		
	}

	@Override
	protected void initOpenGL() {
		glMatrixMode(GL_PROJECTION);
		initTextures();
		initShaders(); 
		setUpProjectionMatrix();
		glMatrixMode(GL_MODELVIEW);

		//glShadeModel(GL_FLAT); // FLATSHADING
		glShadeModel(GL_SMOOTH); // Gouraud-Shading (Default)  --> Farverläufe aktiv
		glPointSize(10);

		glEnable( GL_DEPTH_TEST ); // Tiefenpuffer
		glEnable(GL_TEXTURE_2D); //2D-Texturen aktivieren


		//glEnable(GL_CULL_FACE); // Seiten abschneiden
		//glCullFace(GL_BACK); // Rückseiten!


	}

	@Override
	protected void render() {

		mv = new Matrix4f();
		
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  //Farbinformationen löschen | Tiefeninformationen löschen (gehörig zum Depth_test)
		glLoadIdentity();

		pollInput();
		handleRotationValues();
		

		//		if(lMouse)
		//        System.out.println(lMouse);
		//	    if(rMouse)
		//        System.out.println(rMouse);

		//		System.out.println(cubes.size());


		//System.out.println(mouseX + " AND " + mouseY);	

		// Drehung um Punkt außerhalb des Würfels 
		//glTranslatef(0f, 0f, 4.5f);
		//glRotatef(angle, 1f, 1f, 1f); 

		//glScalef(s, s, s);

		
		// ADD OBJECTS
		
		mv = new Matrix4f();
		passModelViewMatrixToShader(procTex, "mv", mv);
		Quad q = new Quad(100,100,-20);
		q.draw();
				
		int val_in = GL20.glGetUniformLocation(procTex.getId(), "val_in");		
		GL20.glUniform1f(val_in, colorChange);
			
		
		if(rMouse && keyShift)
		{
			int xr = 0;
			int yr = 0;
			int zr = 0;
			if(keyOne) 
				xr = 1;
			if(keyTwo)
				yr = 1;
			if(keyThree)
				zr = 1; 
			
			pyramides.add(new Pyramide(2, 4, mouseX, mouseY, 2, xr, yr, zr));
		}
		
		if(rMouse && !keyShift)
		{
			cubes.add(new Cube(2, mouseX, mouseY, 2)); 
		}
		// DELETE OBJECTS
		if(spaceKey && !keyShift)
		{
			cubes.clear();
		}
		
		if(spaceKey && keyShift)
		{
			pyramides.clear();
		}
		//Change Rotation ...
		if(keyPlus && !keyShift)
		{
			rotateAngle +=0.1;
		}
		if(keyMinus && !keyShift)
		{
			rotateAngle -=0.1;
			
		}
		//...and Translation Speed of cubes
		if(keyPlus && keyShift)
		{
			transVal += 0.1; 
		}
		if(keyMinus && keyShift)
		{
			transVal -= 0.1; 
		}
		
		

		for(Pyramide pyr : pyramides)
		{	
			
			mv = new Matrix4f();
			 
			
			mv.translate(new Vector3f((float)(10*(pyr.getX())), (float)(10*(pyr.getY())), -10)); 
			
			int xr = pyr.getXr();
			int yr = pyr.getYr();
			int zr = pyr.getZr();
			
			if(xr !=0 || yr != 0 || zr != 0)
			mv.rotate((float)Math.toRadians(angle), (Vector3f) new Vector3f(xr, yr, zr).normalise());
			
			passModelViewMatrixToShader(phong2, "mv", mv);
			pyr.draw();
			
		}

		for(Cube cube : cubes)
		{
			mv = new Matrix4f(); 
			
					
			mv.translate(new Vector3f((float)(10*(cube.getX())), (float)(10*(cube.getY())), -10)); 
			
			if(cube.isSelected)
			{
				updateCubePosition(cube);
				
							
				mv.rotate((float)Math.toRadians(angleX), (Vector3f) new Vector3f(1, 0, 0).normalise());
				mv.rotate((float)Math.toRadians(angleY), (Vector3f) new Vector3f(0, 1, 0).normalise());
			}
			

			passModelViewMatrixToShader(phong, "mv", mv);			    
			cube.draw();

			if(lMouse)
			{
				if(lMouse && cube.inBounds(mouseX, mouseY))
				{
					cube.setSelected(!cube.isSelected);
					System.out.println("Selected = " + cube.isSelected());
				}
			}


		}

		

		//Animation von Systemzeit abhängig --> Überall gleiche Geschwindigkeit 
		diff = Sys.getTime() - lastTime;
		angle += degreePerSec * diff / 1000f; //60 Grad Pro Sekunde /// diff / 1000f --> "Bruchteil" der Sekunde
		lastTime = Sys.getTime(); 

		
		if(actualValueVariableIsGrowing)
		{
			colorChange += changePerSec * diff / 1000f;
			if(colorChange >= 1)
				actualValueVariableIsGrowing = !actualValueVariableIsGrowing;
		}
		
		if(!actualValueVariableIsGrowing)
		{
			colorChange -= changePerSec * diff / 1000f;
			if(colorChange <= 0)
				actualValueVariableIsGrowing = !actualValueVariableIsGrowing;
		}
	
//		actualValueVariable = actualValueVariable % 1; 



		/*		if((s > 1.5) || (s < 0.7))
			growing = !growing; 

		if(growing)
			s += 0.01f;
		if(!growing)
			s -= 0.01f; */

		
		

	}



	private void initTextures()
	{
		wall = new Texture("wall.png");
	}

	private void initShaders()
	{
		phong = new ShaderProgram("phong"); 
		phong2 = new ShaderProgram("phong2"); 
		procTex = new ShaderProgram("procTex"); 
	}

	private void handleRotationValues()
	{

		if(rightArr){
			angleY += rotateAngle;
		}

		else if (leftArr){
			angleY -= rotateAngle;
		}


		if(upArr){
			angleX -= rotateAngle;
		}
		else if (downArr){
			angleX += rotateAngle;
		}


	}
	private void updateCubePosition(Cube cube)
	{
		
		if(keyD){
			cube.setX((int)(cube.getXw()+transVal));
		}
		
		else if (keyA){
			cube.setX((int)(cube.getXw()-transVal));
		}
		
		
		if(keyW){
			cube.setY(-(int)(cube.getYw()+transVal));
		}
		else if (keyS){
			cube.setY(-(int)(cube.getYw()-transVal));
		}
		
		
	}

	private void pollInput() {
		//reset Mouse
		lMouse = false;
		rMouse = false; 


		while(Mouse.next())
		{
			//Get Position
			mouseX = Mouse.getX()-640;
			mouseY = (720 - Mouse.getY())-360;
			//Get Mouse Speed
			dx = Mouse.getDX();
			dy = Mouse.getDY();


			if(Mouse.getEventButtonState())
			{
				if(Mouse.getEventButton() == 0)
				{
					System.out.println("Left Mouse Pressed");
					lMouse = true;
				}
				if(Mouse.getEventButton() == 1)
				{
					System.out.println("Right Mouse Pressed");
					rMouse = true; 
				}
			}
			else
			{
				if(Mouse.getEventButton() == 0)
				{
					System.out.println("Left Mouse Released");
					lMouse = false;
				}
				if(Mouse.getEventButton() == 1)
				{
					System.out.println("Right Mouse Released");
					rMouse = false; 
				}

			}
		}



		while (Keyboard.next()) {
//			System.out.println(Keyboard.getEventKey());
			
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					System.out.println("Left Key Pressed");
					leftArr = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					System.out.println("Down Key Pressed");
					downArr = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					System.out.println("Right Key Pressed");
					rightArr = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					System.out.println("Up Key Pressed");
					upArr = true;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					System.out.println("SPACE KEY IS DOWN");
					spaceKey = true; 
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Pressed");
					keyA = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					System.out.println("S Key Pressed");
					keyS = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					System.out.println("D Key Pressed");
					keyD = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_W) {
					System.out.println("W Key Pressed");
					keyW = true;
				}
				if (Keyboard.getEventKey() == 42) {
					System.out.println("Shift Key Pressed");
					keyShift = true;
				}
				if (Keyboard.getEventKey() == 13) {
					System.out.println("Plus Key Pressed");
					keyPlus = true;
				}
				if (Keyboard.getEventKey() == 12) {
					System.out.println("Minus Key Pressed");
					keyMinus= true;
				}
				if (Keyboard.getEventKey() == 2) {
					System.out.println("1 Key Pressed");
					keyOne= !keyOne;
					System.out.println(keyOne);
				}
				if (Keyboard.getEventKey() == 3) {
					System.out.println("2 Key Pressed");
					keyTwo= !keyTwo;
					System.out.println(keyTwo);
				}
				if (Keyboard.getEventKey() == 4) {
					System.out.println("3 Key Pressed");
					keyThree= !keyThree;
					System.out.println(keyThree);
				}
			}
			else {
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					System.out.println("Left Key Released");
					leftArr = false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					System.out.println("Down Key Released");
					downArr = false; 
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					System.out.println("Right Key Released");
					rightArr = false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					System.out.println("Up Key Released");
					upArr = false; 
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
					System.out.println("Space Key Released");
					spaceKey = false; 
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Released");
					keyA = false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					System.out.println("S Key Released");
					keyS = false; 
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					System.out.println("D Key Released");
					keyD = false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_W) {
					System.out.println("W Key Released");
					keyW = false; 
				}
				if (Keyboard.getEventKey() == 42) {
					System.out.println("Shift Key Released");
					keyShift = false; 
				}
				if (Keyboard.getEventKey() == 13) {
					System.out.println("Plus Key Released");
					keyPlus = false; 
				}
				if (Keyboard.getEventKey() == 12) {
					System.out.println("Minus Key Released");
					keyMinus = false; 
				}
			}
		}
	}

}
