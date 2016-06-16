package framework.constants;

import com.badlogic.ashley.core.Family;


import framework.components.*;
import framework.internal.components.*;
import physics.components.Body;
import physics.components.Position;
import physics.components.Velocity;

/**
 * Class grouping global objects to match entities with categories depending on their components. For framework families.
 * @author martin
 */
public class Families
{
    /** family of all entities allowed to take turns */
    public static Family TURN_TAKING = Family.all (Turn.class, PlayerOrder.class).get();
    /** family of global state objects */
    public static Family GLOBAL_STATE = Family.all (Busy.class, Active.class).get();
    /** family of entities owned by another entity*/
    public static Family OWNED = Family.all(Ownership.class).get();
    /** family of entities set to reach a goal */
    public static Family GOAL_SEEKING = Family.all(Goal.class, Body.class, Position.class, Velocity.class).get();
    /** family of entities being possibly modified */
    public static Family MODIFIABLE = Family.one(Velocity.class, Busy.class, Active.class, Turn.class).get();
}
