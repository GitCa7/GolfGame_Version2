package framework.components;


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
