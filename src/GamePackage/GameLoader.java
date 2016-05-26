package GamePackage;

import TerrainComponents.Terrain;
import TerrainComponents.proxyTerrain;

public class GameLoader {
	
	Terrain terra;
	
	public GameLoader()	{
		setTerrainGrid();
	}
	
	private void setTerrainGrid()	{
		int xAmount = 4;
		int yAmount = 4;
		
		terra = new Terrain(0, 0);
		
	}
	
	public static void main(String[] args)	{
		GameLoader gameLoad = new GameLoader();
	}

}
