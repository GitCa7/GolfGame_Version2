package physics.collision.detection;

import com.badlogic.gdx.math.Vector3;
import physics.collision.structure.ColliderEntity;
import physics.collision.structure.ColliderPair;
import physics.collision.structure.EntityAndBody;
import physics.constants.Families;
import physics.generic.QuickSort;
import physics.geometry.spatial.Box;

import physics.vectorUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Class performing the prune and sort algorithm.
 * Given an initial collection of entities belonging to the colliding familiy,
 * this class returns a collection of collider pairs. This collection contains
 * all entities whose bounding boxes are intersecting.
 * @author martin
 */
public class PruneAndSweep extends BroadCollisionFinder
{
    /**
     * Comparator object for sorting of bounding boxes
     */
    public class Intersector implements Comparator<EntityAndBody>
    {
        /**
         * @param coord the extractor for the coordinate on which the sorting
         *              should be based.
         */
        public Intersector(CoordinateExtractor coord)
        {
            mCoord = coord;
        }

        @Override
        /**
         * @return -1 if the offset of left is smaller than the offset of right
         *          0 if the offsets are equal
         *          1 if the offset of left is larger than the offset of right
         */
        public int compare(EntityAndBody left, EntityAndBody right)
        {
            Vector3 leftOffset = left.mBody.getBound().getBoundingBox().getPosition();
            Vector3 rightOffset = right.mBody.getBound().getBoundingBox().getPosition();

            if (mCoord.extract(leftOffset) < mCoord.extract(rightOffset))
                return -1;
            else if (mCoord.extract(leftOffset) > mCoord.extract(rightOffset))
                return 1;
            return 0;
        }

        private CoordinateExtractor mCoord;
    }

    /**
     * @return a deep copied instance
     */
    public PruneAndSweep clone() { return new PruneAndSweep(); }

    /**
     * Performs the prune and sweep algorithm.
     * @return  A collection of collider pairs containing all the pairs of entities whose
     *          bounding boxes intersect.
     */
    public Collection<ColliderPair<ColliderEntity>> possibleCollisions()
    {
        HashSet<ColliderPair<ColliderEntity>> interX, interY, interZ;

        int entities = getAllBodies().size();

        interX = getIntersectionsFor(new XExtractor(), 0);
        interY = getIntersectionsFor(new YExtractor(), 1);
        interZ = getIntersectionsFor(new ZExtractor(), 2);

        interX.retainAll(interY);
        interX.retainAll(interZ);
        return new ArrayList<>(interX);
    }


    /**
     * performs prune and sweep in one dimension
     * @param coord coordinate extractor matching the dimension
     * @param nCoord the coordinate's index
     * @return a hash set of collider pairs for all entities intersecting in one dimension
     */
    private HashSet<ColliderPair<ColliderEntity>> getIntersectionsFor(CoordinateExtractor coord, int nCoord)
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
                    ColliderEntity e1 = getIncompleteCollider(sorter.get(cSorted));
                    ColliderEntity e2 = getIncompleteCollider(sorter.get(cCompare));
                    lineCollisions.add(new ColliderPair<>(e1, e2));
                }

                ++cCompare;
            }
        }

        return lineCollisions;
    }

    /**
     * @param b1 first body
     * @param b2 second body
     * @param coord coordinate mapper
     * @param dimValue index of coordinate (for retrieving dimension)
     * @return true if b1 and b2 intersect for the given coordinate
     */
    private boolean doCoordinatesIntersect (EntityAndBody b1, EntityAndBody b2, CoordinateExtractor coord, int dimValue)
    {
        float o1 = coord.extract(b1.mBody.getBound().getBoundingBox().getPosition());
        float o2 = coord.extract(b2.mBody.getBound().getBoundingBox().getPosition());

        float l1 = ((Box) b1.mBody.getBound().getBoundingBox().getSolid()).getDimensions()[dimValue];

        if (o1 <= o2 && o2 <= o1 + l1)
            return true;
        return false;
    }
}
