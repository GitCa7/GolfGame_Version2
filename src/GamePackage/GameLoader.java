package GamePackage;

import Editor.Course;
import Editor.CourseLoader;
import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;

import javax.swing.*;

public class GameLoader {
	
	private TerrainData terraObject;
	
	public GameLoader(TerrainData terraObject)	{
		this.terraObject = terraObject;
	}
	

	
	private void setFakeTerrain()	{


		String name = JOptionPane.showInputDialog("Course Name?");
		Course toPlay = CourseLoader.loadCourse(name);
		
	}
	
	private void sentToVisualsConfig()	{
		GameVisualConfig visConf = new GameVisualConfig(terraObject);
	}
	
	public static void main(String[] args)	{
		GameLoader gameLoad = new GameLoader();
	}

}
