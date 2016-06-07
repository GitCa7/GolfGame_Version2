package framework.constants;

import com.badlogic.ashley.core.ComponentMapper;

import framework.components.*;
import framework.internal.components.*;

import java.security.acl.Owner;

/**
 * class grouping global component mappers for framework components
 * Created by martin on 23.05.16.
 */
public class CompoMappers
{
    //player mappers
    /** component mapper for name components */
    public static final ComponentMapper<Name> NAME = ComponentMapper.getFor(Name.class);
    /** component mapper for turn components */
    public static final ComponentMapper<Turn> TURN = ComponentMapper.getFor(Turn.class);
    /** component mapper for next player component */
    public static final ComponentMapper<NextPlayer> NEXT_PLAYER = ComponentMapper.getFor(NextPlayer.class);

    //other mappers
    /** component mapper for goal componentns */
    public static final ComponentMapper<Goal> GOAL = ComponentMapper.getFor(Goal.class);
    /** component mapper for ownership componentns */
    public static final ComponentMapper<Ownership> OWNERSHIP = ComponentMapper.getFor(Ownership.class);

    //internal mappers
    /** component mapper for active component */
    public static final ComponentMapper<Active> ACTIVE = ComponentMapper.getFor(Active.class);
    /** component mapper for busy component */
    public static final ComponentMapper<Busy> BUSY = ComponentMapper.getFor(Busy.class);
}
