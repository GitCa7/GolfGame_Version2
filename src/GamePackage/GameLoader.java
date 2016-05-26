package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import Entities.gameEntity;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import framework.ComponentBundle;
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
import physics.geometry.spatial.BoxParameter;
import physics.geometry.spatial.Box;
import physics.geometry.spatial.SphereTetrahedrizer;
import physics.geometry.spatial.Tetrahedron;
import physics.systems.EntitySystemFactory;

import javax.swing.*;
import java.util.ArrayList;

public class GameLoader {
	TerrainData tdata;
	ArrayList<gameEntity> entities;
	Course toPlay;
	Vector3f ballPos;
	Vector3f holePos;

	public GameConfigurator loadConfig()	{
		String name = JOptionPane.showInputDialog("Course Name?");
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
		BodyFactory bodyMaker = new BodyFactory();
		ComponentBundle bodyBundle = new ComponentBundle(bodyMaker,null);

		PositionFactory positionMaker = new PositionFactory();
		ComponentBundle positionBundle = new ComponentBundle(positionMaker,null);

		GameConfigurator config = new GameConfigurator();
		EntityFactory entityMaker  = config.entityFactory();

		for (int i=0;i<boxes.length;i++) {
			bodyMaker.addSolid(boxes[i]);
			positionMaker.setVector(positions[i]);

			entityMaker.addComponent(bodyBundle, positionBundle);
			config.addEntities(entityMaker, 1);
			entityMaker.removeComponents(bodyBundle, positionBundle);
			bodyMaker.clear();
		}
		Vector3f a =entities.get(0).getPosition();
		SphereTetrahedrizer ballMaker = new SphereTetrahedrizer(new Vector3(a.x,a.y,a.z),entities.get(0).scale/2);
		ArrayList<Tetrahedron> tmp = ballMaker.tetrahedrize(2,5);
		for (Tetrahedron b:tmp){
			bodyMaker.addSolid(b);
		}
		positionMaker.setVector(new Vector3(ballPos.x,ballPos.y,ballPos.z));
		entityMaker.addComponent(bodyBundle, positionBundle);
		Ball ball = new Ball(entityMaker.produce());
		entityMaker.removeComponents(bodyBundle, positionBundle);
		bodyMaker.clear();

		NextPlayerFactory nextplayerMaker = new NextPlayerFactory();
		TurnFactory turnMaker = new TurnFactory();
		ComponentBundle playerBundle = new ComponentBundle(nextplayerMaker,null);
		ComponentBundle turnBundle = new ComponentBundle(turnMaker,null);
		entityMaker.addComponent(playerBundle,turnBundle);
		Player player = new Player(entityMaker.produce());
		entityMaker.removeComponents(bodyBundle, positionBundle);

		config.addBall(player,ball);

		a = holePos;
		positionMaker.setVector(new Vector3(a.x,a.y,a.z));
		entityMaker.addComponent(bodyBundle, positionBundle);
		Hole hole = new Hole(entityMaker.produce());
		config.setHole(hole);



		return config;
	}
	
	public GameVisual loadVisual(){
		GameVisual visual  = new GameVisual();
		visual.setTerrain(tdata);
		return visual;
	}

}
