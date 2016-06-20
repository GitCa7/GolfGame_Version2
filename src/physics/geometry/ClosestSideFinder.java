package physics.geometry;
import physics.constants.GlobalObjects;
import physics.geometry.spatial.Solid;
import physics.geometry.planar.Plane;
import physics.geometry.planar.Shape;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.spatial.SolidTranslator;


/**
 * Created by Alexander on 22.05.2016.
 * Computes to which side of a Solid a certain Vector3 is the closest
 */
public class ClosestSideFinder
{
    /**
     *
     * @param sides Solid to which sides the distance is computed
     */
    public ClosestSideFinder (SolidTranslator sides)
    {
        mSides = sides;
    }

    /**
     *    Precondition: p is within mSides
     *    @param p Vector3 in question.
     *    return returns a Plane as a representation of the clostest Side to a certain Vector
     */
    public Plane closestSide (Vector3 p) {
        //local variable instantiation

        float length = Float.MAX_VALUE;
        Plane[] solidSides = mSides.getSidePlanes();
        Plane closestSide = null;


        for (int i = 0; i < mSides.getSidePlanes().length; i++) {
            float thisDistance = distance(p,solidSides[i]);
            //if condition to determine the shortest distance
            if (thisDistance < length) {
                length = thisDistance;
                //Assignment of a certain Plane as the closest one to a particular Vector3
                closestSide=solidSides[i];
            }
        }

        return closestSide;
    }


    /**
     * Find the closest side to point whose normal is pointing in the same direction as direction.
     * If strictly, the normal may not be orthogonal to the direction, otherwise it may
     * @param point the point for which to find the closest side
     * @param direction direction to compare normal with
     * @param strictly strictly flag
     * @return the collision plane satisfying the conditions above
     */
    public Plane closestIntersectingSide(Vector3 point, Vector3 direction, boolean strictly)
    {
        float length = Float.MAX_VALUE;
        Plane[] solidSides = mSides.getSidePlanes();
        Plane closestSide = null;


        for (int i = 0; i < mSides.getSidePlanes().length; i++)
        {
            float thisDistance = distance(point, solidSides[i]);

            //evaluate: if normal has same direction as direction, dot product >(=) 0
            float planeDot = direction.dot(solidSides[i].getNormal());
            boolean directionCondition;
            if (strictly)
                directionCondition = planeDot > 0 && !GlobalObjects.ROUND.epsilonEquals(planeDot, 0f);
            else
                directionCondition = planeDot > 0 || GlobalObjects.ROUND.epsilonEquals(planeDot, 0f);

            //if condition to determine the shortest distance
            if (thisDistance < length && directionCondition)
            {
                length = thisDistance;
                //Assignment of a certain Plane as the closest one to a particular Vector3
                closestSide = solidSides[i];
            }
        }
        return closestSide;
    }

    /**
     *
     * @param vector Vector whose distance to a plane is to determined
     * @param plane Plane whose distance to a Vector3 is to be determined
     * @return the distance between a Vector3 and a Plane
     */

    public float distance(Vector3 vector, Plane plane){
        float distance=vector.dot(plane.getNormal())- plane.getDistance();
        if(distance<0)
            distance=-1*distance;
        return distance;
    }
    private SolidTranslator mSides;
}


