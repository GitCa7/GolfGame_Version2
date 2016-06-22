package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import Editor.ObstacleDat;
import Entities.Obstacle;
import Entities.gameEntity;
import TerrainComponents.TerrainData;
import TerrainComponents.TerrainGeometryCalc;
import com.badlogic.gdx.math.Vector3;
import framework.BotObserver;
import framework.Game;
import framework.GameConfigurator;
import framework.PlayerObserver;
import framework.logging.Logger;
import framework.GameSettings;

import framework.testing.HumanObserver;
import org.lwjgl.util.vector.Vector3f;
import physics.geometry.planar.Triangle;
import physics.geometry.spatial.*;
import physics.geometry.spatial.Box;

import javax.swing.*;
import java.util.ArrayList;

public class GameLoader {
	public static final float HOLE_SIZE = 20;

	public BotObserver mBobs;

	TerrainData tdata;
	ArrayList<ObstacleDat> obstacles;
	ArrayList<Boolean> bots;
	Course toPlay;
	ArrayList<Vector3f> ballPos;
	Vector3f holePos;
	TerrainData a;
	ArrayList<PlayerObserver> obs;

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
			params[i][0]=new Vector3(0,obstacles.get(i).getScale()*2,0);
			Vector3 tmpx = new Vector3(obstacles.get(i).getScale()*2,0,0);
			Vector3 tmpz = new Vector3(0,0,obstacles.get(i).getScale()*2);

			//System.out.println("aaaahhhhahahhahah"+obstacles.get(i).getRotY());
			params[i][1]=tmpx.rotate(obstacles.get(i).getRotY(),0,1,0);
			params[i][2]=tmpz.rotate(obstacles.get(i).getRotY(),0,1,0);

			BoxParameter tmp=new BoxParameter(params[i]);
			boxes[i]=tmp.instantiate();
			Vector3 tpos = obstacles.get(i).getPos().cpy();
			//System.out.println("aaaahhhhahahhahah" + tpos);
			//System.out.println("aaaahhhhahahhahah2" + tpos.sub(tmpx.scl(0.5f)));
			tpos = tpos.sub(tmpx.cpy().scl(0.5f));
			tpos = tpos.sub(tmpz.cpy().scl(0.5f));
			//System.out.println("aaaahhhhahahhahah" + tpos);
			positions[i] = new Vector3(tpos.x,tpos.y-obstacles.get(i).getScale(),tpos.z);
		}

		GameConfigurator config = new GameConfigurator();

		for (int i=0;i<boxes.length;i++) {
            ArrayList<SolidTranslator> box = new ArrayList<>();
            SolidTranslator a = new SolidTranslator(boxes[i], positions[i]);
            box.add(a);
            config.addObstacle(box);

        }

		//@TODO set hole position correctly
		Vector3 pos = new Vector3(holePos.x,(float) (holePos.y - .25 * HOLE_SIZE),holePos.z);
		config.setHole(pos,HOLE_SIZE);
		System.out.println(ballPos.size());
		obs = new ArrayList<>();
		for(int i=0;i<ballPos.size();i++) {

			PlayerObserver a ;

			//@TODO remove after testing
			//bots.set(i, false);

			if(bots.get(i)){
				String pName = JOptionPane.showInputDialog("Bot "+i+" Name?");
				mBobs = new BotObserver();
				a = mBobs;
				Logger logger = new Logger();
				mBobs.setLogger(logger);
				config.addBotAndBall(pName, 5, 1, new Vector3(ballPos.get(i).x, ballPos.get(i).y+20, ballPos.get(i).z), mBobs);
			}else {
				String pName = JOptionPane.showInputDialog("Player "+i+" Name?");
				a = new HumanObserver();
				config.addHumanAndBall(pName, 5, 1, new Vector3(ballPos.get(i).x, ballPos.get(i).y+20, ballPos.get(i).z),a);
			}

			obs.add(a);
		}
		a = new TerrainData(8,300);

		TerrainGeometryCalc calc =  new TerrainGeometryCalc();
		System.out.println(calc.terrainIsFlat(tdata));
		ArrayList<Triangle> tmp = calc.getAllTris(a);
		System.out.println("aaaaaa");
		config.setTerrain(tmp);


		return config.game();
	}
	
	public GameVisual loadVisual(Game game){
		GameVisual visual  = new GameVisual();
		visual.setBalls(ballPos,obs);
		visual.setHole(new Vector3f(holePos.x,holePos.y+2,holePos.z),HOLE_SIZE);
		//tdata.printVerts();
		//System.out.println("--------------------------------------------------------------------------------------");
		visual.setTerrain(a);

		//visual.startDisplay();
		ArrayList<gameEntity> entities = new ArrayList<>();
		for(ObstacleDat a:obstacles){
			Obstacle b = a.toObstacle();
			b.setRotY(a.getRotY());
			entities.add(b);
		}
		visual.setEntities(entities);

		visual.setEngine(game.getEnigine());
		return visual;
	}

}
