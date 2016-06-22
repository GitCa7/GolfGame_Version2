package physics.collision;

import physics.components.Body;

/**
 * Created by marcel on 21.06.2016.
 */
public class BodyPair
{
    public BodyPair(Body b1, Body b2)
    {
        mB1 = b1;
        mB2 = b2;
    }

    public boolean equals(BodyPair another)
    {
        return ((another.mB1 == this.mB1 || another.mB1 == this.mB2)
                && (another.mB2 == this.mB1 && another.mB2 == this.mB2));
    }

    public Body mB1;
    public Body mB2;
}
