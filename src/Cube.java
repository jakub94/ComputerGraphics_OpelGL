import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.*;


import java.util.Random;

import lenz.opengl.utils.Texture;


public class Cube {

	int length;
	double x, y; //ScreenCoordinates  
	double xw, yw, zw; //WorldCoordinates 
	double[] colorValues = new double[18]; 
	Random rand;
	boolean isSelected;

	//Textures
	Texture wall; 



	public double getXw() {
		return xw;
	}

	public void setXw(double xw) {
		this.xw = xw;
	}

	public double getYw() {
		return yw;
	}

	public void setYw(double yw) {
		this.yw = -yw;
	}

	public Cube(int length, int x, int y, int z) {
		setLength(length); 
		setX(x);
		setY(y);
		setXw(x);
		setYw(y);
		setZw(z);
		setSelected(false);
		rand = new Random(); 
		generateColors();		
		initTextures();
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public double getX() {
		return x;
	}


	public void setX(int x) {
		this.xw=x; 
		this.x = (x/1280.0)*3.2;
		
	}


	public double getY() {
		return y;
	}



	public void setY(int y) {
		this.yw = -y; 
		this.y = -(y/720.0)*1.8;
		
	}



	public double getZw() {
		return zw;
	}


	public void setZw(int z) {
		this.zw = z;
	}








	public void draw(){	

		glBindTexture(GL_TEXTURE_2D, wall.getId()); 

		glColor3d(colorValues[0], colorValues[1], colorValues[2]);

		glBegin(GL_QUADS); 
		//BACK
		glNormal3f(0, 0, -1);
		glTexCoord2f(0, 0);
		glVertex3d(length, -length, -length); //B'
		glTexCoord2f(1, 0);
		glVertex3d(length, length, -length); //A'
		glTexCoord2f(1, 1);
		glVertex3d(-length, length, -length); //D'
		glTexCoord2f(0, 1);
		glVertex3d(-length, -length, -length); //C'


		glColor3d(colorValues[3], colorValues[4], colorValues[5]);

		//TOP
		glNormal3f(0, 1, 0);
		glTexCoord2f(0, 0);
		glVertex3d(length, length, -length); //A'
		glTexCoord2f(1, 0);
		glVertex3d(length, length, length); //A
		glTexCoord2f(1, 1);
		glVertex3d(-length, length, length); //D
		glTexCoord2f(0, 1);
		glVertex3d(-length, length, -length); //D'

		glColor3d(colorValues[6], colorValues[7], colorValues[8]);
		//Bottom
		glNormal3f(0, -1, 0);
		glTexCoord2f(0, 0);
		glVertex3d(-length, -length, length); //C
		glTexCoord2f(1, 0);
		glVertex3d(length, -length, length); //B
		glTexCoord2f(1, 1);
		glVertex3d(length, -length, -length); //B'
		glTexCoord2f(0, 1);
		glVertex3d(-length, -length, -length); //C'


		glColor3d(colorValues[9], colorValues[10], colorValues[11]);
		//Right
		glNormal3f(1, 0, 0);
		glTexCoord2f(0, 0);
		glVertex3d(length, -length, length); //B
		glTexCoord2f(1, 0);
		glVertex3d(length, length, length); //A
		glTexCoord2f(1, 1);
		glVertex3d(length, length, -length); //A'
		glTexCoord2f(0, 1);
		glVertex3d(length, -length, -length); //B'



		glColor3d(colorValues[12], colorValues[13], colorValues[14]);
		//Left
		glNormal3f(-1, 0, 0);
		glTexCoord2f(0, 0);
		glVertex3d(-length, length, length); //D
		glTexCoord2f(1, 0);
		glVertex3d(-length, -length, length); //C
		glTexCoord2f(1, 1);
		glVertex3d(-length, -length, -length); //C'
		glTexCoord2f(0, 1);
		glVertex3d(-length, length, -length); //D'





		glColor3d(colorValues[15], colorValues[16], colorValues[17]);
		//Front	
		glNormal3f(0, 0, 1);
		glTexCoord2f(0, 0);
		glVertex3d(-length, -length, length); //C
		glTexCoord2f(1, 0);
		glVertex3d(-length, length, length); //D
		glTexCoord2f(1, 1);
		glVertex3d(length, length, length); //A
		glTexCoord2f(0, 1);
		glVertex3d(length, -length, length); //B
		glEnd();	



	}

	public double getRandomColorValue()
	{
		return rand.nextInt(255) / 255.0; 

	}

	private void generateColors()
	{
		for(int i = 0; i < 18; i++)
		{
			colorValues[i]=getRandomColorValue();  
		}
	}




	public boolean inBounds(int mouseX, int mouseY)
	{
		//Current Mouse position (WorldCoordinates --> Screen Coordinates)
		double xPos = (mouseX/1280.0)*3.2;
		double yPos = -(mouseY/720.0)*1.8; 
		
		return (inBetween(xPos, x-000.12*length, x+000.12*length) && (inBetween(yPos, y-000.12*length, y+000.12*length))); 	

	}

	private void initTextures()
	{
		wall = new Texture("wall.png");
	}

	private boolean inBetween(double value, double from, double to)
	{
		if(value >= from && value <=to)
			return true;
		else return false; 
	}



}
