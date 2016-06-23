package physics.collision.detection;

import physics.geometry.spatial.BoundingBox;

import java.util.Comparator;

/**
 * Created by Asus on 21-6-2016.
 */
public class BBComparator implements Comparator<BoundingBox> {
    int xyz;

    public BBComparator(int xyz){this.xyz=xyz;}

    @Override
    public int compare(BoundingBox o1, BoundingBox  o2) {

        if(xyz==0) {
            if (o1.getLowestX() < o2.getLowestX())
                return -1;
            if (o1.getLowestX() > o2.getLowestX())
                return 1;
            if (o1.getLowestX() == o2.getLowestX())
                return 0;
        }
        if(xyz==1) {
            if (o1.getLowestY() < o2.getLowestY())
                return -1;
            if (o1.getLowestY() > o2.getLowestY())
                return 1;
            if (o1.getLowestY() == o2.getLowestY())
                return 0;
        }
        if(xyz==2) {
            if (o1.getLowestZ() < o2.getLowestZ())
                return -1;
            if (o1.getLowestZ() > o2.getLowestZ())
                return 1;
            if (o1.getLowestZ() == o2.getLowestZ())
                return 0;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
