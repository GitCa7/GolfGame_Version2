package Editor;

import Entities.gameEntity;
import TerrainComponents.Terrain;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Created by Asus on 26-5-2016.
 */
public class Course {
    Terrain terrain;
    ArrayList<gameEntity> entities;
    String name;
    Vector3f ballPos;
    Vector3f holePos;

    public Course(Terrain terrain,ArrayList<gameEntity> entities,Vector3f a, Vector3f b){
        this.terrain = terrain;
        this.entities = entities;
        ballPos =a;
        holePos=b;
    }

    public String getName(){
        return name;
    }

}
