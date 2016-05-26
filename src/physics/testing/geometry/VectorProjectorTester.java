package physics.testing.geometry;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.VectorProjector;
import java.lang.reflect.Array;


/**
 * Created by marcel on 25.05.2016.
 */

//Tester class to see if the Vector projection is working good

public class VectorProjectorTester
{

   public void createTestVector()
   {
       Vector3[] vectorOne = new Vector3[2];
       vectorOne[0] = new Vector3(1, 5, 7);
       vectorOne[1] = new Vector3(2, 7, 2);
       vectorOne[2] = new Vector3(7, 1, 3);

       Vector3[] vectorTwo = new Vector3[2];
       vectorTwo[0] = new Vector3(2, 2, 2);
       vectorTwo[1] = new Vector3(3, 3, 3);
       vectorTwo[2] = new Vector3(4, 4, 4);

       boolean test = false;
       Array[][] results = new Array[2][2];


       for (int i = 0; i < vectorOne.length; i++ ){

           VectorProjector projector = new VectorProjector(vectorTwo[i]);
           Vector3 projectorTester = computeProjection(vectorOne[i], vectorTwo[i]);
           Vector3 projectorVector = projector.project(vectorOne[i]);

            System.out.println(projectorTester.toString() + projectorVector.toString());
       }
   }

   public Vector3 computeProjection(Vector3 vectorOne, Vector3 vectorTwo)
   {

       float  dotproduct = vectorOne.dot(vectorTwo);
       float length = vectorTwo.len();
       float scalar = (float) (dotproduct/(Math.pow(length, 2)));
       Vector3 vectorProjection = vectorTwo.scl(scalar);
       return vectorProjection;
   }
}
