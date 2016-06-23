package framework.components;

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
