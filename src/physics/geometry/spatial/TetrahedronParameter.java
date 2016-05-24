package physics.geometry.spatial;

import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Alexander on 24.05.2016.
 */
public class TetrahedronParameter {
    public TetrahedronParameter(Vector3[] defining){
        mDefining=defining;

    }
    public Tetrahedron instantiate() {
        TetrahedronBuilder builder = new TetrahedronBuilder(mDefining);
        return builder.build() ;
    }

    public Vector3[] getDefining(){return mDefining;}

    public boolean equals(TetrahedronParameter para) {
        HashSet<Vector3> set1= new HashSet<>(Arrays.asList(mDefining));
        HashSet<Vector3> set2= new HashSet<>(Arrays.asList(para.mDefining));
        return false;
    }

    private Vector3[] mDefining;
}
