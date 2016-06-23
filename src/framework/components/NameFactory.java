package framework.components;


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
    
    /**
     * set name to produce to name
     * @param name a name
     */
    public void set(String name)
    {
    	mName = name;
    }


    private String mName;
}
