package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import TerrainComponents.Terrain;

import javax.swing.*;

public class GameLoader {
	
	proxyTerrain fakeTerrain;
	
	public GameLoader()	{
		setFakeTerrain();
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
