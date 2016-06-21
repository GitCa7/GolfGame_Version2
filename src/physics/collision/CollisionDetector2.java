package physics.collision;

import TerrainComponents.Terrain;
import com.badlogic.ashley.core.Entity;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Asus on 21-6-2016.
 */
public class CollisionDetector2 extends CollisionDetector {
    TerrainPartition mPart;

    public void setmPart(TerrainPartition mPart){
        this.mPart = mPart;
    }
    @Override
    public ArrayList<ColliderPair<ColliderEntity>> getAnyColliding()
    {
        //clone sets
        HashSet<BodyPair> currentBodies = (HashSet<BodyPair>) mAll.clone();

        ArrayList<ColliderPair<ColliderEntity>> colliding = new ArrayList<>();

        //for every active|all pair check for physics.collision
        for (BodyPair activeBody : mActive)
        {

            currentBodies.remove (activeBody);
            for (BodyPair passiveBody : currentBodies)
            {
                if(passiveBody.mEntity.getComponents().size()>10){
                    BodyIntersectionDetector checkBodies = new BodyIntersectionDetector (activeBody.mBody, mPart.partitionWith(activeBody.mBody.getBound().getBoundingBox().getPosition()));
                    checkBodies.checkForIntersection();
                    if (checkBodies.doIntersect())
                    {
                        ColliderPair<ColliderBody> cPair = checkBodies.getBodyCollision();
                        ColliderBody cb1 = cPair.getFirst();
                        ColliderBody cb2 = cPair.getSecond();
                        ColliderEntity ec1 = new ColliderEntity(activeBody.mEntity, cb1);
                        ColliderEntity ec2 = new ColliderEntity(passiveBody.mEntity, cb2);
                        ColliderPair<ColliderEntity> ePair = new ColliderPair<>(ec1, ec2);
                        colliding.add(ePair);
                    }
                }
                else {
                    //if colliding: add as collider pair
                    //@TODO store intersection detectors permanently
                    BodyIntersectionDetector checkBodies = new BodyIntersectionDetector(activeBody.mBody, passiveBody.mBody);
                    checkBodies.checkForIntersection();
                    if (checkBodies.doIntersect()) {
                        ColliderPair<ColliderBody> cPair = checkBodies.getBodyCollision();
                        ColliderBody cb1 = cPair.getFirst();
                        ColliderBody cb2 = cPair.getSecond();
                        ColliderEntity ec1 = new ColliderEntity(activeBody.mEntity, cb1);
                        ColliderEntity ec2 = new ColliderEntity(passiveBody.mEntity, cb2);
                        ColliderPair<ColliderEntity> ePair = new ColliderPair<>(ec1, ec2);
                        colliding.add(ePair);
                    }
                }
            }
        }

        return colliding;
    }
}
