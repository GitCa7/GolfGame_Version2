package framework.components;

import com.badlogic.ashley.core.Component;
import framework.components.Name;
import physics.components.ComponentFactory;

/**
 * class producing name components
 * @author martin
 */
public class NameFactory implements ComponentFactory
{


    public NameFactory()
    {
        mName = new String();
    }

    @Override
    /**
     * @return a new instance of name component
     */
    public Component produce()
    {
        if (mName == null)
            throw new IllegalStateException ("parameters not set");
        return new Name(mName);
    }


    private String mName;
}
