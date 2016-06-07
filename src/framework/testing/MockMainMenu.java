package framework.testing;

import framework.Main;

/**
 * @author martin
 */
public class MockMainMenu
{

    public MockMainMenu(Main main)
    {
        mMain = main;
        mMain.initGame();
        mMain.run();
    }


    private Main mMain;
}
