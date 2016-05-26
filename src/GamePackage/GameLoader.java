package GamePackage;

import TerrainComponents.Terrain;
import TerrainComponents.proxyTerrain;

public class GameLoader {
	
	proxyTerrain[][] TerrainGrid;
	
	public GameLoader()	{
		setTerrainGrid();
	}
	
	private void setTerrainGrid()	{
		int xAmount = 4;
		int yAmount = 4;
		
		TerrainGrid = new proxyTerrain[xAmount][yAmount];
		
		for(int i = 0; i < TerrainGrid.length; i++)	{
			for(int j = 0; j < TerrainGrid[0].length; j++)	{
				TerrainGrid[i][j] = new proxyTerrain(i,j,"heightmap");
			}
		}
	}
	
	public static void main(String[] args)	{
		GameLoader gameLoad = new GameLoader();
	}

}
