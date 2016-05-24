package framework.constants;

import com.badlogic.ashley.core.Family;


import framework.components.*;
import framework.internal.components.*;

/**
 * Class grouping global objects to match entities with categories depending on their components. For framework families.
 * @author martin
 */
public class Families
{
    /** family of all entities allowed to take turns */
    public static Family TURN_TAKING = Family.all (Turn.class, NextPlayer.class).get();
    /** family of global state objects */
    public static Family GLOBAL_STATE = Family.all (Busy.class, Active.class).get();
}
