package PHY_Geometry.planar;

import com.badlogic.gdx.math.Vector3;

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

   
    /**
     * parametric constructor
     * @param vertices vertices of triangle
     * @param border line segments connecting vertices
     */
    public Triangle (Vector3[] vertices, Line[] border)
    {
        super (vertices, border);
    }
    
    
}
