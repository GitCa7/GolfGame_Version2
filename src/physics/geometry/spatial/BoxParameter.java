package physics.geometry.spatial;
import physics.generic.Parameter;
import com.badlogic.gdx.math.Vector3;
import java.util.HashSet;
import java.util.Arrays;


/**
 * Created by Alexander on 24.05.2016.
 */
public class BoxParameter implements Parameter<Box>
{
    /**
     *
     * @param directions array of 3 direction vectors
     */
    public BoxParameter(Vector3 ... directions)
    {
        mBuilder = new BoxBuilder(directions);
        mDirections = directions;
    }

    public Box instantiate() {
        return mBuilder.build() ;
    }

    public Vector3[] getmDirections(){return mDirections;}

    public boolean equals(BoxParameter para)
    {
        HashSet<Vector3> set1= new HashSet<>(Arrays.asList(this.mBuilder.vertices()));
        return set1.containsAll(Arrays.asList(para.mBuilder.vertices()));
    }


    private BoxBuilder mBuilder;
    private Vector3[] mDirections;

}
