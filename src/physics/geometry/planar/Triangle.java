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
    
    
}
