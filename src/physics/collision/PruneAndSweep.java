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

    public class Intersector implements Comparator<EntityAndBody>
    {

        public Intersector(CoordinateExtractor coord)
        {
            mCoord = coord;
        }

        @Override
        public int compare(EntityAndBody left, EntityAndBody right)
        {
            Vector3 leftOffset = left.mBody.getBound().getBoundingBox().getPosition();
            Vector3 rightOffset = right.mBody.getBound().getBoundingBox().getPosition();

            if (mCoord.getValue(leftOffset) < mCoord.getValue(rightOffset))
                return -1;
            else if (mCoord.getValue(leftOffset) > mCoord.getValue(rightOffset))
                return 1;
            return 0;
        }

        private CoordinateExtractor mCoord;
    }

    public PruneAndSweep(ArrayList<EntityAndBody> bodies)
    {
        super(bodies);
    }


    public Collection<ColliderPair<ColliderEntity>> possibleCollisions()
    {
        HashSet<ColliderPair<ColliderEntity>> interX, interY, interZ;
        interX = getIntersectionsFor(new XExtractor(), 0);
        interY = getIntersectionsFor(new YExtractor(), 1);
        interZ = getIntersectionsFor(new ZExtractor(), 2);

        interX.retainAll(interY);
        interX.retainAll(interZ);
        return new ArrayList<>(interX);
    }



    private HashSet<ColliderPair<ColliderEntity>> getIntersectionsFor(CoordinateExtractor coord, int nCoord)
    {
        QuickSort<EntityAndBody> sorter = new QuickSort<>(getBodies(), new Intersector(coord));
        sorter.sort(0, sorter.size() - 1);

        HashSet<ColliderPair<ColliderEntity>> lineCollisions = new HashSet<>();
        for (int cSorted = 0; cSorted < sorter.size(); ++cSorted)
        {
            int cCompare = cSorted + 1;
            while (cCompare < sorter.size() && doCoordinatesIntersect(sorter.get(cSorted), sorter.get(cCompare), coord, nCoord))
            {
                ColliderEntity e1 = getIncompleteCollider(sorter.get(cSorted));
                ColliderEntity e2 = getIncompleteCollider(sorter.get(cCompare));
                lineCollisions.add(new ColliderPair<>(e1, e2));
            }
        }

        return lineCollisions;
    }


    private ColliderEntity getIncompleteCollider(EntityAndBody entity)
    {
        ColliderSolid colliderSolid = new ColliderSolid(null, null);
        ColliderBody colliderBody = new ColliderBody(entity.mBody, colliderSolid);
        return new ColliderEntity(entity.mEntity, colliderBody);
    }

    private boolean doCoordinatesIntersect (EntityAndBody b1, EntityAndBody b2, CoordinateExtractor coord, int dimValue)
    {
        float o1 = coord.getValue(b1.mBody.getBound().getBoundingBox().getPosition());
        float o2 = coord.getValue(b2.mBody.getBound().getBoundingBox().getPosition());

        float l2 = ((Box) b2.mBody.getBound().getBoundingBox().getSolid()).getDimensions()[dimValue];

        if (o1 >= o2 && o1 <= o2 + l2)
            return true;
        return false;
    }
}
