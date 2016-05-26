package Editor;

import Entities.*;
import LogicAndExtras.MousePicker;
import RenderComponents.Loader;
import TerrainComponents.Terrain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 25-5-2016.
 */
public class MListener extends InputAdapter {
    Terrain terrain;
    Loader loader;
    ArrayList<gameEntity> instances;
    MousePicker picker;
    Camera camera;
    Editor parent;
    GolfBall ball ;

    private int switchcnt=0;

    public MListener(Loader loader,Camera camera,Terrain terrain,MousePicker picker,Editor parent){
        this.loader=loader;
        this.terrain = terrain;
        instances = new ArrayList<gameEntity>();
        this.picker = picker;
        this.camera = camera;
        this.parent = parent;
        ball = new GolfBall(new Vector3f(0,0,0),5);
        instances.add(ball);

    }
    public Terrain terrain(){
        return terrain;
    }
    public ArrayList<gameEntity> entities(){
        return instances;
    }
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Ray ray = new Ray(new Vector3(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z),new Vector3(picker.getCurrentRay().x,picker.getCurrentRay().y,picker.getCurrentRay().z));
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            gameEntity selected=null;
            BoundingBox out;
            for (int i=0;i<instances.size();i++){
                out = instances.get(i).boundingBox();
                if(Intersector.intersectRayBoundsFast(ray,out)){
                    selected = instances.get(i);
                }
            }
            if(selected!=null)
                selected.setRotY(selected.getRotY()+20f);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            parent.save();
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.B)){
            if(picker.getCurrentTerrainPoint()!=null){
                ball.setPosition(new Vector3f(picker.getCurrentTerrainPoint().x,terrain.getHeightDif(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z),picker.getCurrentTerrainPoint().z));
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.Z)){
            int selected=-1;
            BoundingBox out;
            for (int i=0;i<instances.size();i++){
                out = instances.get(i).boundingBox();
                if(Intersector.intersectRayBoundsFast(ray,out)){
                    selected = i;
                }
            }
            if(selected!=-1&&selected!=0)
                instances.remove(selected);
            return true;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.U)){
            if(picker.getCurrentTerrainPoint()!=null){
                terrain.changeHeight(picker.getCurrentTerrainPoint().x+40,picker.getCurrentTerrainPoint().x-40,picker.getCurrentTerrainPoint().z+40, picker.getCurrentTerrainPoint().z-40, 2);
                parent.updateEntities();
                return true;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.J)){
            terrain.changeHeight(picker.getCurrentTerrainPoint().x+40,picker.getCurrentTerrainPoint().x-40,picker.getCurrentTerrainPoint().z+40, picker.getCurrentTerrainPoint().z-40, -2);
            parent.updateEntities();
            return true;
        }
        else if(picker.getCurrentTerrainPoint()!=null&&Gdx.input.isKeyPressed(Input.Keys.P)){
            gameEntity selected=null;
            Vector3 out2=new Vector3();
            BoundingBox out;
            for (int i=0;i<instances.size();i++){
                out = instances.get(i).boundingBox();
                if(Intersector.intersectRayBoundsFast(ray,out)){
                    selected = instances.get(i);
                    Intersector.intersectRayBounds(ray,out,out2);
                }
            }
            if(selected!=null){
                Vector3f position = new Vector3f(out2.x,out2.y+2.5f,out2.z);
                crate tmp = new crate( position,25);
                instances.add(tmp);
            }else{
                Vector3f position = new Vector3f(picker.getCurrentTerrainPoint().x,terrain.getHeightDif(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z),picker.getCurrentTerrainPoint().z);
                Obstacle tmp = new Obstacle( position,25);
                instances.add(tmp);
            }
        }
        System.out.println(picker.getCurrentTerrainPoint());
        return true;
    }

    @Override
    public boolean scrolled(int amount){
        Ray ray = new Ray(new Vector3(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z),new Vector3(picker.getCurrentRay().x,picker.getCurrentRay().y,picker.getCurrentRay().z));

        gameEntity selected=null;
        BoundingBox out;
        for (int i=0;i<instances.size();i++){
            System.out.println(ray.toString());
            out = instances.get(i).boundingBox();
            if(Intersector.intersectRayBoundsFast(ray,out)){
                selected = instances.get(i);
                System.out.println("sel");
            }
        }
        if(selected!=null){
            float amountf = (float) amount;
            selected.setScale(selected.getScale()+amount);
            selected.setPosition(new Vector3f(selected.position.x,selected.position.y+amountf,selected.position.z));
        }

        return true;

    }

    gameEntity dragSelec = null;
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        Ray ray = new Ray(new Vector3(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z),new Vector3(picker.getCurrentRay().x,picker.getCurrentRay().y,picker.getCurrentRay().z));
        if(Gdx.input.isKeyPressed(Input.Keys.U)){
            if(picker.getCurrentTerrainPoint()!=null){
                terrain.changeHeight(picker.getCurrentTerrainPoint().x+40,picker.getCurrentTerrainPoint().x-40,picker.getCurrentTerrainPoint().z+40, picker.getCurrentTerrainPoint().z-40, 2);
                parent.updateEntities();
                return true;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.J)){
            terrain.changeHeight(picker.getCurrentTerrainPoint().x+40,picker.getCurrentTerrainPoint().x-40,picker.getCurrentTerrainPoint().z+40, picker.getCurrentTerrainPoint().z-40, -2);
            parent.updateEntities();
            return true;
        }else{
            BoundingBox out;
            for (int i=0;i<instances.size();i++){
                if(dragSelec!=null){

                }else{
                    out = instances.get(i).boundingBox();
                    if(Intersector.intersectRayBoundsFast(ray,out)){
                        dragSelec = instances.get(i);
                    }
                }
            }
            if(dragSelec!=null){

                Vector3f position= new Vector3f(picker.getCurrentTerrainPoint().x,terrain.getHeightDif(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z)+dragSelec.scale,picker.getCurrentTerrainPoint().z);
                dragSelec.setPosition(position);
            }
        }
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button){
        dragSelec =null;
        return true;
    }
}

