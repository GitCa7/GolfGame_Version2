package physics.collision;

import physics.generic.tree.avl.AvlTree;
import physics.geometry.spatial.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Asus on 21-6-2016.
 */
public class PrumeAndSweep {
    ArrayList<BoundingBox> bodies;
    private AvlTree<BoundingBox> X,Y,Z;


    public PrumeAndSweep (ArrayList<BoundingBox> bodies){
        this.bodies = bodies;
        X = new AvlTree<>(new BBComparator(0));
        Y = new AvlTree<>(new BBComparator(1));
        Z = new AvlTree<>(new BBComparator(2));

        for (int i=0;i<bodies.size();i++){
           X.add(bodies.get(i));
            Y.add(bodies.get(i));
           Z.add(bodies.get(i));
        }
    }

    public ArrayList<BoundingBox> interSectingWith(BoundingBox a){

    }
}
