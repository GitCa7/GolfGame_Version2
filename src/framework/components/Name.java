package framework.components;

import physics.components.Component;

/**
 * component storing a name as string
 * @author martin
 */
public class Name implements Component
{
    public Name (String name)
    {
        mName = name;
    }

    public Name clone()
    {
        return new Name (mName);
    }

    public String mName;
}
