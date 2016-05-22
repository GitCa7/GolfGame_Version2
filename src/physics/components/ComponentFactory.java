package physics.components;

import com.badlogic.ashley.core.Component;

/**
 * interface for classes producing physics.components
 * @author martin
 */
public interface ComponentFactory
{
	public Component produce();
}
