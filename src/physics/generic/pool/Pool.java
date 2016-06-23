package physics.generic.pool;
import java.util.HashMap;

/**
 * Created by Alexander on 24.05.2016.
 * Generic Pool class to manage Flyweight objects.
 */
public class Pool<E> {

    /**
     *
     */

    public Pool() {
        mInstanceMap=new HashMap<>();
    }

    /**
     *
     * @param param key of a HashMap
     * @return instance stored at param
     */


    public E getInstance(Parameter<E> param) {
        if (mInstanceMap.containsKey(param))
            return mInstanceMap.get(param);

        E newInstance= param.instantiate();
        mInstanceMap.put(param, newInstance);

        return newInstance;
    }

    /**
     *
     * @param param key of a HashMap
     * @return whether mInstanceMap contains an instance at param
     */

    public boolean hasInstance(Parameter<E> param){
        return mInstanceMap.containsKey(param);
    }



    private HashMap<Parameter<E>, E> mInstanceMap;
}
