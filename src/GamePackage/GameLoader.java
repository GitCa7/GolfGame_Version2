package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import Entities.gameEntity;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;
import TerrainComponents.TerrainGeometryCalc;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.ComponentBundle;
import framework.Game;
import framework.GameConfigurator;
import framework.components.NextPlayerFactory;
import framework.components.TurnFactory;
import framework.entities.EntityFactory;
import framework.entities.Player;
import org.lwjgl.util.vector.Vector3f;
import physics.components.BodyFactory;
import physics.components.ComponentFactory;
import physics.components.PositionFactory;
import physics.entities.Ball;
import physics.entities.Hole;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.*;
import physics.geometry.spatial.Box;
import physics.systems.EntitySystemFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class GameLoader {
	TerrainData tdata;
	ArrayList<gameEntity> entities;
	Course toPlay;
	Vector3f ballPos;
	Vector3f holePos;

	public Game loadConfig(String name)	{
		toPlay = CourseLoader.loadCourse(name);
		entities = toPlay.getEntities();
		tdata = toPlay.getTerrain();
		ballPos = toPlay.getBallPos();
		holePos = toPlay.getHolePos();

		Vector3[][] params = new Vector3[entities.size()-1][3];
		Box[] boxes = new Box[entities.size()-1];
		Vector3[] positions = new Vector3[entities.size()-1];
		for (int i=1;i<entities.size();i++){
			params[i-1][0]=new Vector3(0,entities.get(i).scale,0);
			Vector3 tmpx = new Vector3(entities.get(i).scale,0,0);
			Vector3 tmpz = new Vector3(0,0,entities.get(i).scale);

			params[i-1][1]=tmpx.rotate(entities.get(i).getRotY(),0,1,0);
			params[i-1][2]=tmpz.rotate(entities.get(i).getRotY(),0,1,0);

			BoxParameter tmp=new BoxParameter(params[i]);
			boxes[i-1]=tmp.instantiate();
			Vector3f a  = entities.get(i).getPosition();
			positions[i-1] = new Vector3(a.x,a.y,a.z) ;
		}

		GameConfigurator config = new GameConfigurator();

		for (int i=0;i<boxes.length;i++) {
            ArrayList<SolidTranslator> box = new ArrayList<>();
            SolidTranslator a = new SolidTranslator(boxes[i], positions[i]);
            box.add(a);
            config.addObstacle(1, box);

        }

		Vector3 pos = new Vector3(holePos.x,holePos.y,holePos.z);
		config.setHole(pos,20);

        String pName = JOptionPane.showInputDialog("Player 1 Name?");
        config.addPlayerAndBall(pName,entities.get(0).getScale(),1,new Vector3(ballPos.x,ballPos.y,ballPos.z));

        TerrainGeometryCalc calc = new TerrainGeometryCalc();
        config.setTerrain(calc.getAllTris(tdata));


		return config.game();
	}
	
	public GameVisual loadVisual(){
		GameVisual visual  = new GameVisual();
		visual.setTerrain(tdata);
		return visual;
	}

}
