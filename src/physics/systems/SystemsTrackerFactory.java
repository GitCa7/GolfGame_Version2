package physics.systems;

/**
 * Created by marcel on 21.05.2016.
 */
public class SystemsTrackerFactory {

    public SystemsTrackerFactory(){
    }

    public SystemsTracker producer(){
        SystemsTracker st = new SystemsTracker();
        return st;
    }
}
