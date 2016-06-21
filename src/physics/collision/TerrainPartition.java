package physics.collision;

import com.badlogic.gdx.math.Vector3;
import physics.components.Body;
import physics.geometry.spatial.SolidTranslator;

import java.util.ArrayList;

/**
 * Created by Asus on 21-6-2016.
 */
public class TerrainPartition {
    ArrayList<SolidTranslator>[] parts;
    private final int NUMPARTS =4;
    float size;

    public TerrainPartition(ArrayList<SolidTranslator> a,float size){
        parts = new ArrayList[NUMPARTS^2];
        this.size =size;
        for(int i=0;i<a.size();i++) {
            System.out.println("busy "+i);
            for (int j = 0; j < a.get(i).getVertices().length; j++) {
                if (a.get(i).getVertices()[j].x>0&&a.get(i).getVertices()[j].x<size/NUMPARTS&&a.get(i).getVertices()[j].z>0&&a.get(i).getVertices()[j].z<size/NUMPARTS){
                    if(!parts[0].contains(a.get(i)))
                        parts[0].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>size/NUMPARTS&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z>0&&a.get(i).getVertices()[j].z<(size/NUMPARTS)){
                    if(!parts[1].contains(a.get(i)))
                        parts[1].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z>0&&a.get(i).getVertices()[j].z<(size/NUMPARTS)){
                    if(!parts[2].contains(a.get(i)))
                        parts[2].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*4&&a.get(i).getVertices()[j].z>0&&a.get(i).getVertices()[j].z<(size/NUMPARTS)){
                    if(!parts[3].contains(a.get(i)))
                        parts[3].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>0&&a.get(i).getVertices()[j].x<size/NUMPARTS&&a.get(i).getVertices()[j].z>size/NUMPARTS&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*2){
                    if(!parts[4].contains(a.get(i)))
                        parts[4].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>size/NUMPARTS&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z>size/NUMPARTS&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*2){
                    if(!parts[5].contains(a.get(i)))
                        parts[5].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z>size/NUMPARTS&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*2){
                    if(!parts[6].contains(a.get(i)))
                        parts[6].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*4&&a.get(i).getVertices()[j].z>size/NUMPARTS&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*2){
                    if(!parts[7].contains(a.get(i)))
                        parts[7].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>0&&a.get(i).getVertices()[j].x<size/NUMPARTS&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*3){
                    if(!parts[8].contains(a.get(i)))
                        parts[8].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>size/NUMPARTS&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*3){
                    if(!parts[9].contains(a.get(i)))
                        parts[9].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*3){
                    if(!parts[10].contains(a.get(i)))
                        parts[10].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*4&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*3){
                    if(!parts[11].contains(a.get(i)))
                        parts[11].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>0&&a.get(i).getVertices()[j].x<size/NUMPARTS&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*4){
                    if(!parts[12].contains(a.get(i)))
                        parts[12].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>size/NUMPARTS&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*2&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*4){
                    if(!parts[13].contains(a.get(i)))
                        parts[13].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*2&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*4){
                    if(!parts[14].contains(a.get(i)))
                        parts[14].add(a.get(i));
                }
                if (a.get(i).getVertices()[j].x>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].x<(size/NUMPARTS)*4&&a.get(i).getVertices()[j].z>(size/NUMPARTS)*3&&a.get(i).getVertices()[j].z<(size/NUMPARTS)*4){
                    if(!parts[15].contains(a.get(i)))
                        parts[15].add(a.get(i));
                }
            }
        }
    }
    
    public Body partitionWith(Vector3 pos){
        ArrayList<SolidTranslator> tmp = new ArrayList<>();
        if(pos.x<0&&pos.x>size/NUMPARTS&&pos.z<0&&pos.z>size/NUMPARTS)
            tmp.addAll(parts[0]);
        if(pos.x<size/NUMPARTS&&pos.x>(size/NUMPARTS)*2&&pos.z<0&&pos.z>size/NUMPARTS)
            tmp.addAll( parts[1]);
        if(pos.x<(size/NUMPARTS)*2&&pos.x>(size/NUMPARTS)*3&&pos.z<0&&pos.z>size/NUMPARTS)
            tmp.addAll( parts[2]);
        if(pos.x<(size/NUMPARTS)*3&&pos.x>(size/NUMPARTS)*4&&pos.z<0&&pos.z>size/NUMPARTS)
            tmp.addAll( parts[3]);
        if(pos.x<0&&pos.x>size/NUMPARTS&&pos.z>(size/NUMPARTS)&&pos.z>(size/NUMPARTS)*2)
            tmp.addAll( parts[4]);
        if(pos.x<size/NUMPARTS&&pos.x>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)&&pos.z>(size/NUMPARTS)*2)
            tmp.addAll( parts[5]);
        if(pos.x<(size/NUMPARTS)*2&&pos.x>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)&&pos.z>(size/NUMPARTS)*2)
            tmp.addAll( parts[6]);
        if(pos.x<(size/NUMPARTS)*3&&pos.x>(size/NUMPARTS)*4&&pos.z>(size/NUMPARTS)&&pos.z>(size/NUMPARTS)*2)
            tmp.addAll( parts[7]);
        if(pos.x<0&&pos.x>size/NUMPARTS&&pos.z>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)*3)
            tmp.addAll( parts[8]);
        if(pos.x<size/NUMPARTS&&pos.x>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)*3)
            tmp.addAll( parts[9]);
        if(pos.x<(size/NUMPARTS)*2&&pos.x>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)*3)
            tmp.addAll( parts[10]);
        if(pos.x<(size/NUMPARTS)*3&&pos.x>(size/NUMPARTS)*4&&pos.z>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)*3)
            tmp.addAll( parts[11]);
        if(pos.x<0&&pos.x>size/NUMPARTS&&pos.z>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)*4)
            tmp.addAll( parts[12]);
        if(pos.x<size/NUMPARTS&&pos.x>(size/NUMPARTS)*2&&pos.z>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)*4)
            tmp.addAll( parts[13]);
        if(pos.x<(size/NUMPARTS)*2&&pos.x>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)*4)
            tmp.addAll( parts[14]);
        if(pos.x<(size/NUMPARTS)*3&&pos.x>(size/NUMPARTS)*4&&pos.z>(size/NUMPARTS)*3&&pos.z>(size/NUMPARTS)*4)
            tmp.addAll( parts[15]);
        Body bod = new Body(tmp);
        return bod;
    }
}
