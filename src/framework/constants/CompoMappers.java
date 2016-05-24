package framework.constants;

import com.badlogic.ashley.core.ComponentMapper;

import framework.components.*;
import framework.internal.components.*;

/**
 * class grouping global component mappers for framework components
 * Created by martin on 23.05.16.
 */
public class CompoMappers
{
    /** component mapper for name components */
    public static final ComponentMapper<Name> NAME = ComponentMapper.getFor(Name.class);
    /** component mapper for turn components */
    public static final ComponentMapper<Turn> TURN = ComponentMapper.getFor(Turn.class);
    /** component mapper for next player component */
    public static final ComponentMapper<NextPlayer> NEXT_PLAYER = ComponentMapper.getFor(NextPlayer.class);

    //internal mappers
    /** component mapper for active component */
    public static final ComponentMapper<Active> ACTIVE = ComponentMapper.getFor(Active.class);
    /** component mapper for busy component */
    public static final ComponentMapper<Busy> BUSY = ComponentMapper.getFor(Busy.class);
}
