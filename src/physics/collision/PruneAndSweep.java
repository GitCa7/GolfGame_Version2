package physics.collision;

import com.badlogic.gdx.math.Vector3;
import physics.components.Body;
import physics.generic.QuickSort;
import physics.geometry.spatial.Box;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Class performing the prune and sort algorithm
 * @author martin
 */
public class PruneAndSweep extends CollisionFinder
{

    public enum Coordinate {X, Y, Z}


    public interface CoordinateExtractor
    {
        public float getValue(Vector3 v);
    }

    public class XExtractor implements CoordinateExtractor
    {
        public float getValue (Vector3 v) { return v.x; }
    }

    public class YExtractor implements CoordinateExtractor
    {
        public float getValue (Vector3 v) { return v.y; }
    }

    public class ZExtractor implements CoordinateExtractor
    {
        public float getValue (Vector3 v) {return v.z; }
    }

    public class Intersector implements Comparator<Body>
    {

        public Intersector(CoordinateExtractor coord)
        {
            mCoord = coord;
        }

        @Override
        public int compare(Body left, Body right)
        {
            Vector3 leftOffset = left.getBound().getBoundingBox().getPosition();
            Vector3 rightOffset = right.getBound().getBoundingBox().getPosition();

            if (mCoord.getValue(leftOffset) < mCoord.getValue(rightOffset))
                return -1;
            else if (mCoord.getValue(leftOffset) > mCoord.getValue(rightOffset))
                return 1;
            return 0;
        }

        private CoordinateExtractor mCoord;
    }

    public PruneAndSweep(ArrayList<Body> bodies)
    {
        super(bodies);
    }


    public ArrayList<BodyPair> possibleCollisions()
    {
        HashSet<BodyPair> interX, interY, interZ;
        interX = getIntersectionsFor(new XExtractor(), 0);
        interY = getIntersectionsFor(new YExtractor(), 1);
        interZ = getIntersectionsFor(new ZExtractor(), 2);

        interX.retainAll(interY);
        interX.retainAll(interZ);
        return new ArrayList<>(interX);
    }



    private HashSet<BodyPair> getIntersectionsFor(CoordinateExtractor coord, int nCoord)
    {
        QuickSort<Body> sorter = new QuickSort<Body>(getBodies(), new Intersector(coord));
        sorter.sort(0, sorter.size() - 1);

        HashSet<BodyPair> lineCollisions = new HashSet<>();
        for (int cSorted = 0; cSorted < sorter.size(); ++cSorted)
        {
            int cCompare = cSorted + 1;
            while (cCompare < sorter.size() && doCoordinatesIntersect(sorter.get(cSorted), sorter.get(cCompare), coord, nCoord))
                lineCollisions.add(new BodyPair(sorter.get(cSorted), sorter.get(cCompare)));
        }

        return lineCollisions;
    }

    private boolean doCoordinatesIntersect (Body b1, Body b2, CoordinateExtractor coord, int dimValue)
    {
        float o1 = coord.getValue(b1.getBound().getBoundingBox().getPosition());
        float o2 = coord.getValue(b2.getBound().getBoundingBox().getPosition());

        float l2 = ((Box) b2.getBound().getBoundingBox().getSolid()).getDimensions()[dimValue];

        if (o1 >= o2 && o1 <= o2 + l2)
            return true;
        return false;
    }
}
