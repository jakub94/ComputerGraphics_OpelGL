import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3d;


public class Quad {
	
	int width;
	int height;
	int z;
	


	public Quad(int widht, int height, int z) {
		super();
		this.width = widht;
		this.height = height;
		this.z = z;
	}



	public int getWidht() {
		return width;
	}



	public void setWidht(int widht) {
		this.width = widht;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getZ() {
		return z;
	}



	public void setZ(int z) {
		this.z = z;
	}



	public void draw()
	{

		glBegin(GL_QUADS);
		glVertex3d(width/2, height/2, z); 
		glVertex3d(-width/2, height/2, z);
		glVertex3d(-width/2, -height/2, z); 
		glVertex3d(width/2, -height/2, z); 
		glEnd();


	}
	
	
}