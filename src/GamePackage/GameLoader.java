package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import TerrainComponents.Terrain;

import javax.swing.*;

public class GameLoader {
	
<<<<<<< HEAD
	Terrain terra;
=======
	proxyTerrain fakeTerrain;
>>>>>>> origin/master
	
	public GameLoader()	{
		setFakeTerrain();
	}
	

	private void setTerrainGrid()	{
		int xAmount = 4;
		int yAmount = 4;
		
		terra = new Terrain(0, 0);
		
	}
	private void setFakeTerrain()	{


		String name = JOptionPane.showInputDialog("Course Name?");
		Course toPlay = CourseLoader.loadCourse(name);
		Terrain playTerrain = toPlay.getTerrain();

		fakeTerrain = new proxyTerrain(playTerrain.ge);

	}
	
	public static void main(String[] args)	{
		GameLoader gameLoad = new GameLoader();
	}

}
