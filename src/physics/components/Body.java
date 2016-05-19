package physics.components;


import java.util.HashSet;

/**
 * @autor martin
 * created 12.05.2016
 */
public class Body<T> extends HashSet<T> implements Component
{
	//@TODO use immutable geometric objects
	//@TODO implement clone
	public Body<T> clone()
	{
		return null;
	}


	//@TODO flyweight
}
