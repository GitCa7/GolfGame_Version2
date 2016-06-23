package GamePackage;

import framework.main.Game;

public class GameStart {
	GameLoader gLoader;

	public static final int TICKS_PER_FRAME = 20;
	
	public GameStart()	{
		GameLoader load = new GameLoader();
		mGame = load.loadConfig("aahh");

		//pass necessary resources to visual (i.e. terrain) in loader
		//mVisual = load.loadVisual();

		mTranslate.setGame(mGame);

	}


	public void doGame()
	{
		while (true)
		{
			//maybe move to timer
			mGame.tick(TICKS_PER_FRAME);
			mTranslate.update();
		}
	}

	private Game mGame;
	private GameVisual mVisual;
	private PhysicsTranslator mTranslate;
}
