package GamePackage;

import TerrainComponents.Terrain;
import TerrainComponents.TerrainData;

public class GameVisualConfig {
	
	Terrain terrain;
	
	
	public GameVisualConfig(TerrainData data)	{
		terrain = new Terrain(data);
	}
	
	public Terrain getTerrain()	{
		return terrain;
	}

}
