package physics.geometry.planar;

import com.badlogic.gdx.math.Vector3;
import physics.geometry.linear.Line;

/**
 * class modeling a triangle
 * @autor martin on 08.05.16.
 */
public class Triangle extends Shape
{
    /** number of sides of triangle */
    public static final int SIDES = 3;
    /**  number of vertices of triangle */
    public static final int VERTICES = 3;

    private Line[] lines;
    private Vector3[] vertices;

    /**
     * parametric constructor
     * @param vertices vertices of triangle
     * @param border line segments connecting vertices
     */
    public Triangle (Vector3[] vertices, Line[] border)
    {
        super (vertices, border);

        if (!checkNotOnLine(vertices))	{
        	System.out.println(vertices[0] + "\t" + vertices[1] + "\t" + vertices[2]);
            throw new IllegalArgumentException("vertices lie on line, don't form a triangle");
        }

        lines = border;
        this.vertices = vertices;
    }
    
    /*Robin Edit
     * Just going to place this here because it would be convenient to read out the lines for the terrain Tetraeder
     * Let me know of thats going to be a problem
     */
    
    public Line[] getLine()	{
    	return lines;
    }
    
    public Vector3[] getVertices()	{
    	return vertices;
    }
    

    private boolean checkNotOnLine(Vector3[] vertices)
    {
        Vector3 d1 = vertices[1].cpy().sub(vertices[0]);
        Vector3 d2 = vertices[2].cpy().sub(vertices[0]);

        return (!d1.isOnLine(d2) && !d1.isOnLine(d2));
    }
    
    public String toString()	{
    	return new String(vertices[0] + "\t" + vertices[1] + "\t" + vertices[2]);
    }

}
