package testing.unit.physics.geometry;
import com.badlogic.gdx.math.Vector3;
import physics.geometry.VectorProjector;
import java.lang.reflect.Array;


/**
 * Created by marcel on 25.05.2016.
 */

//Tester class to see if the Vector projection is working good

public class VectorProjectorTester
{
    public static void main(String[] args){
      VectorProjectorTester test = new VectorProjectorTester();
        test.createTestVector();

    }
   public static void createTestVector()
   {
       Vector3[] vectorOne = new Vector3[3];
       vectorOne[0] = new Vector3(2, 2, 2);
       vectorOne[1] = new Vector3(1, 1, 1);
       vectorOne[2] = new Vector3(7, 1, 3);

       Vector3[] vectorTwo = new Vector3[3];
       vectorTwo[0] = new Vector3(300, 0, 0);
       vectorTwo[1] = new Vector3(1, 0, 0);
       vectorTwo[2] = new Vector3(4, 4, 4);

       boolean test = false;
       Array[][] results = new Array[2][2];


       for (int i = 0; i < vectorOne.length; i++ ){

           VectorProjector projector = new VectorProjector(vectorTwo[i]);
           Vector3 projectorTester = computeProjection(vectorOne[i].cpy(), vectorTwo[i].cpy());
           Vector3 projectorVector = projector.project(vectorOne[i]);

            System.out.println(projectorTester.toString() + projectorVector.toString());
       }
   }

   public static Vector3 computeProjection(Vector3 vectorOne, Vector3 vectorTwo)
   {

       float dotproduct = vectorOne.dot(vectorTwo);
       //System.out.println("Dot:" + dotproduct);
       float length = vectorTwo.len();
       //System.out.println("length:" + length);
       float scalar = (float) (dotproduct/(Math.pow(length, 2)));
       //System.out.println("skalar:" + scalar);
       Vector3 vectorProjection = vectorTwo.scl(scalar);
       //System.out.println(vectorProjection.toString());

       return vectorProjection;
   }
}
