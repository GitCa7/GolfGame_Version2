package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import Editor.ObstacleDat;
import Entities.gameEntity;
import TerrainComponents.PointNode;
import TerrainComponents.TerrainData;
import TerrainComponents.TerrainGeometryCalc;
import com.badlogic.gdx.math.Vector3;
import framework.Game;
import framework.GameConfigurator;
import org.lwjgl.util.vector.Vector3f;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.*;
import physics.geometry.spatial.Box;

import javax.swing.*;
import java.util.ArrayList;

public class GameLoader {
	TerrainData tdata;
	ArrayList<ObstacleDat> obstacles;
	ArrayList<Boolean> bots;
	Course toPlay;
	ArrayList<Vector3f> ballPos;
	Vector3f holePos;

	public Game loadConfig(String name)	{
		toPlay = CourseLoader.loadCourse(name);
		bots=toPlay.getBots();
		obstacles = toPlay.getObs();
		tdata = toPlay.getTerrain();
		ballPos = toPlay.getBallPos();
		holePos = toPlay.getHolePos();

		Vector3[][] params = new Vector3[obstacles.size()][3];
		Box[] boxes = new Box[obstacles.size()];
		Vector3[] positions = new Vector3[obstacles.size()];
		for (int i=0;i<obstacles.size();i++){
			params[i][0]=new Vector3(0,obstacles.get(i).getScale(),0);
			Vector3 tmpx = new Vector3(obstacles.get(i).getScale(),0,0);
			Vector3 tmpz = new Vector3(0,0,obstacles.get(i).getScale());

			params[i][1]=tmpx.rotate(obstacles.get(i).getRotY(),0,1,0);
			params[i][2]=tmpz.rotate(obstacles.get(i).getRotY(),0,1,0);

			BoxParameter tmp=new BoxParameter(params[i]);
			boxes[i]=tmp.instantiate();
			positions[i] = obstacles.get(i).getPos();
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
		System.out.println(ballPos.size());
		for(int i=0;i<ballPos.size();i++) {
			String pName = JOptionPane.showInputDialog("Player "+i+" Name?");
			if(bots.get(i)){
				config.addBotAndBall(pName, 5, 1, new Vector3(ballPos.get(i).x, ballPos.get(i).y+10, ballPos.get(i).z));
			}else {
				config.addHumanAndBall(pName, 5, 1, new Vector3(ballPos.get(i).x, ballPos.get(i).y+10, ballPos.get(i).z));
			}
		}
		TerrainGeometryCalc calc =  new TerrainGeometryCalc();
		ArrayList<Triangle> tmp = calc.getAllTris(tdata);
		config.setTerrain(tmp);
		return config.game();
	}
	
	public GameVisual loadVisual(Game game){
		GameVisual visual  = new GameVisual();
		visual.setBalls(ballPos);
		visual.setTerrain(tdata);
		//visual.startDisplay();
		ArrayList<gameEntity> entities = new ArrayList<>();
		for(ObstacleDat a:obstacles){
			entities.add(a.toObstacle());
		}
		visual.setEntities(entities);

		visual.setEngine(game.getEnigine());
		return visual;
	}

}
