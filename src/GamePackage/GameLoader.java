package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import Entities.gameEntity;
import TerrainComponents.PointNode;
import TerrainComponents.TerrainData;
import TerrainComponents.TerrainGeometryCalc;
import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;
import org.lwjgl.util.vector.Vector3f;
import physics.geometry.spatial.*;
import physics.geometry.spatial.Box;

import javax.swing.*;
import java.util.ArrayList;

public class GameLoader {
	TerrainData tdata;
	ArrayList<gameEntity> entities;
	Course toPlay;
	ArrayList<Vector3f> ballPos;
	Vector3f holePos;

	public Game loadConfig(String name)	{
		toPlay = CourseLoader.loadCourse(name);
		entities = toPlay.getEntities();
		tdata = toPlay.getTerrain();
		ballPos = toPlay.getBallPos();
		holePos = toPlay.getHolePos();

		Vector3[][] params = new Vector3[entities.size()][3];
		Box[] boxes = new Box[entities.size()];
		Vector3[] positions = new Vector3[entities.size()];
		for (int i=0;i<entities.size();i++){
			params[i][0]=new Vector3(0,entities.get(i).scale,0);
			Vector3 tmpx = new Vector3(entities.get(i).scale,0,0);
			Vector3 tmpz = new Vector3(0,0,entities.get(i).scale);

			params[i][1]=tmpx.rotate(entities.get(i).getRotY(),0,1,0);
			params[i][2]=tmpz.rotate(entities.get(i).getRotY(),0,1,0);

			BoxParameter tmp=new BoxParameter(params[i]);
			boxes[i]=tmp.instantiate();
			Vector3f a  = entities.get(i).getPosition();
			positions[i] = new Vector3(a.x,a.y,a.z) ;
		}

		GameConfigurator config = new GameConfigurator();

		for (int i=0;i<boxes.length;i++) {
            ArrayList<SolidTranslator> box = new ArrayList<>();
            SolidTranslator a = new SolidTranslator(boxes[i], positions[i]);
            box.add(a);
            config.addObstacle(box);

        }

		Vector3 pos = new Vector3(holePos.x,holePos.y,holePos.z);
		config.setHole(pos,20);

		for(int i=0;i<ballPos.size();i++) {
			String pName = JOptionPane.showInputDialog("Player "+i+" Name?");
			config.addPlayerAndBall(pName, entities.get(i).getScale(), 1, new Vector3(ballPos.get(i).x, ballPos.get(i).y, ballPos.get(i).z));
		}
		ArrayList<Vector3f> tmp = new ArrayList<>();
		for(PointNode a:tdata.getLeafs()) {
			tmp.add(a.g)
		}
		config.setTerrain(tdata.getLeafs());
		return config.game();
	}
	
	public GameVisual loadVisual(Game game){
		GameVisual visual  = new GameVisual();
		visual.setTerrain(tdata);
		visual.setEntities(entities);
		visual.setBalls(ballPos);
		visual.setEngine(game.getEnigine());
		return visual;
	}

}
