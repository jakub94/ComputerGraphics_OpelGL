import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.util.ArrayList;
import java.util.Random;

import lenz.opengl.utils.Texture;


public class Pyramide {
	
	int a;
	int t; 
	double x, y, z;  // Z is stored in WorldCoordinates 
	
	//rotationValues
	int xr, yr, zr; 
	
	public int getXr() {
		return xr;
	}

	public void setXr(int xr) {
		this.xr = xr;
	}

	public int getYr() {
		return yr;
	}

	public void setYr(int yr) {
		this.yr = yr;
	}

	public int getZr() {
		return zr;
	}

	public void setZr(int zr) {
		this.zr = zr;
	}

	//textures
	Texture wall, meme;
	ArrayList<Texture> memes;
	
	
	public Pyramide(int a, int t, int x, int y, int z, int xr, int yr, int zr)
	{
		this.a=a;
		this.t=t; 
		setX(x);
		setY(y);
		setZ(z);
		initTextures();
		setXr(xr);
		setYr(yr);
		setZr(zr);
	}
	
	private double calculateAngle()
	{
		return Math.toDegrees(Math.atan(t/a));
	}
	
	private double calculateNormalOrientation()
	{
			double angle = calculateAngle();
			return angle + 90; 
	}
	
//	public double[] calculateNormals()
//	{
//		double normalAngle = calculateNormalOrientation();
//		double[][] normals = new double[2][3];
//						
//		double m = Math.tan(normalAngle);
//
//	}
	
	
	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}


	
	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = (x/1280.0)*3.2;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = -(y/720.0)*1.8;
	}


	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void draw()
	{
	//	double y = -(t / 2);
		
		
		
		
		//Bottom
		glNormal3f(0, -1, 0);
		glBindTexture(GL_TEXTURE_2D, meme.getId());
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex3d(a, y, a); //A
		glTexCoord2f(1, 0);
		glVertex3d(a, y, -a); //B
		glTexCoord2f(1, 1);
		glVertex3d(-a, y, -a); //C
		glTexCoord2f(0, 1);
		glVertex3d(-a, y, a); //D
		glEnd();

		
		//Right
		glNormal3f(2, 1, 0);
		glBindTexture(GL_TEXTURE_2D, wall.getId());
		glBegin(GL_TRIANGLES);
		glTexCoord2f(0, 0);
		glVertex3d(a, y, a); //A
		glTexCoord2f(1, 0);
		glVertex3d(y, t, y); //S
		glTexCoord2f(0, 1);
		glVertex3d(a, y, -a); //B
		glEnd();
		
		
		//Front
		glNormal3f(0, 1, -2);
		glBindTexture(GL_TEXTURE_2D, wall.getId());
		glBegin(GL_TRIANGLES);
		glTexCoord2f(0, 0);
		glVertex3d(a, y, -a); //B
		glTexCoord2f(1, 0);
		glVertex3d(y, t, y); //S
		glTexCoord2f(0, 1);
		glVertex3d(-a, y, -a); //C
		glEnd();

		//Left
		glNormal3f(-2, 1, 0);
		glBindTexture(GL_TEXTURE_2D, wall.getId());
		glBegin(GL_TRIANGLES);
		glTexCoord2f(0, 0);
		glVertex3d(-a, y, -a); //C
		glTexCoord2f(1, 0);
		glVertex3d(y, t, y); //S
		glTexCoord2f(0, 1);
		glVertex3d(-a, y, a); //D
		glEnd();

		//Back
		glNormal3f(0, 1, 2);
		glBindTexture(GL_TEXTURE_2D, wall.getId());
		glBegin(GL_TRIANGLES);
		glTexCoord2f(0, 0);
		glVertex3d(-a, y, a); //D
		glTexCoord2f(1, 0);
		glVertex3d(y, t, y); //S
		glTexCoord2f(0, 1);
		glVertex3d(a, y, a); //A
		glEnd(); 
		
	}
	
	private void initTextures()
	{
		 wall = new Texture("wall.png");
		 Texture megusta = new Texture("megusta.jpg");
		 Texture alone = new Texture("alone.jpg");
		 Texture  haha = new Texture("haha.jpg");
		 Texture lol = new Texture("lol.jpg");
		 
		 memes = new ArrayList<Texture>();
		 memes.add(megusta);
		 memes.add(alone);
		 memes.add(haha);
		 memes.add(lol);
		 
		 Random random = new Random(); 
		 
		 meme=memes.get(random.nextInt(memes.size())); 
		 
		 
	}
	

}
