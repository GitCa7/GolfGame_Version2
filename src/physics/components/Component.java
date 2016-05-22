package physics.components;

/**
 * Interface for Factory classes that produce components
 * Created by martin on 19.05.16.
 */
public interface Component extends com.badlogic.ashley.core.Component, Cloneable
{
	public Component clone();
}
