package physics.collision;

import com.badlogic.gdx.math.Vector3;
import physics.constants.Families;
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
public class PruneAndSweep extends BroadCollisionFinder
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

    public PruneAndSweep clone() { return new PruneAndSweep(); }

    public Collection<ColliderPair<ColliderEntity>> possibleCollisions()
    {
        HashSet<ColliderPair<ColliderEntity>> interX, interY, interZ;

        int entities = getAllBodies().size();
        ColliderPair<ColliderEntity>[][] colliderPairBuffer = new ColliderPair[entities - 1][entities - 1];

        interX = getIntersectionsFor(new XExtractor(), 0, colliderPairBuffer);
        interY = getIntersectionsFor(new YExtractor(), 1, colliderPairBuffer);
        interZ = getIntersectionsFor(new ZExtractor(), 2, colliderPairBuffer);

        interX.retainAll(interY);
        interX.retainAll(interZ);
        return new ArrayList<>(interX);
    }



    private HashSet<ColliderPair<ColliderEntity>> getIntersectionsFor(CoordinateExtractor coord, int nCoord, ColliderPair<ColliderEntity>[][] buffer)
    {
        QuickSort<EntityAndBody> sorter = new QuickSort<>(getAllBodies(), new Intersector(coord));
        sorter.sort(0, sorter.size() - 1);

        HashSet<ColliderPair<ColliderEntity>> lineCollisions = new HashSet<>();
        for (int cSorted = 0; cSorted < sorter.size(); ++cSorted)
        {
            boolean sortedMoving = Families.MOVING.matches(sorter.get(cSorted).mEntity);

            int cCompare = cSorted + 1;
            while (cCompare < sorter.size() && doCoordinatesIntersect(sorter.get(cSorted), sorter.get(cCompare), coord, nCoord))
            {
                if (sortedMoving || Families.MOVING.matches(sorter.get(cCompare).mEntity))
                {
                    //previous buffer size + added elements
                    if (buffer[cSorted][cCompare - 1] == null)
                    {
                        ColliderEntity e1 = getIncompleteCollider(sorter.get(cSorted));
                        ColliderEntity e2 = getIncompleteCollider(sorter.get(cCompare));
                        buffer[cSorted][cCompare - 1] = new ColliderPair<>(e1, e2);
                    }
                    lineCollisions.add(buffer[cSorted][cCompare - 1]);
                }

                ++cCompare;
            }
        }

        return lineCollisions;
    }

    private boolean doCoordinatesIntersect (EntityAndBody b1, EntityAndBody b2, CoordinateExtractor coord, int dimValue)
    {
        float o1 = coord.getValue(b1.mBody.getBound().getBoundingBox().getPosition());
        float o2 = coord.getValue(b2.mBody.getBound().getBoundingBox().getPosition());

        float l1 = ((Box) b1.mBody.getBound().getBoundingBox().getSolid()).getDimensions()[dimValue];

        if (o1 <= o2 && o2 <= o1 + l1)
            return true;
        return false;
    }
}
