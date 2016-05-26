package Editor;

import Entities.gameEntity;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Created by Asus on 26-5-2016.
 */
public class Course {
    TerrainData terrain;
    ArrayList<gameEntity> entities;
    String name;
    Vector3f ballPos;
    Vector3f holePos;

    public Course(TerrainData terrain, ArrayList<gameEntity> entities, Vector3f a, Vector3f b){
        this.terrain = terrain;
        this.entities = entities;
        ballPos =a;
        holePos=b;
    }

    public String getName(){
        return name;
    }

    public TerrainData getTerrain(){
        return terrain;
    }

    public ArrayList<gameEntity> getEntities(){
        return entities;
    }

    public Vector3f getBallPos() {
        return ballPos;
    }

    public Vector3f getHolePos() {
        return holePos;
    }
}
