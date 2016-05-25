package physics.geometry.spatial;
import physics.generic.Parameter;
import com.badlogic.gdx.math.Vector3;
import java.util.HashSet;
import java.util.Arrays;


/**
 * Created by Alexander on 24.05.2016.
 */
public class BoxParameter implements Parameter<Box>  {


    public BoxParameter(Vector3[] defining){
        mDefining=defining;

    }
    public Box instantiate() {
        BoxBuilder builder = new BoxBuilder(mDefining);
        return builder.build() ;
    }

    public Vector3[] getDefining(){return mDefining;}

    public boolean equals(BoxParameter para) {
        HashSet<Vector3> set1= new HashSet<>(Arrays.asList(mDefining));
        HashSet<Vector3> set2= new HashSet<>(Arrays.asList(para.mDefining));
        return false;
    }

    private Vector3[] mDefining;

}
