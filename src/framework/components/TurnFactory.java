package framework.components;

import com.badlogic.ashley.core.Component;
import physics.components.ComponentFactory;

/**
 * class producing turn components
 * @author martin
 */
public class TurnFactory implements ComponentFactory {

    @Override
    public Component produce()
    {
        return new Turn();
    }
}
