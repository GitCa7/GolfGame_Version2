package framework.entities;

import com.badlogic.ashley.core.Entity;

/**
 * Tag class for player objects signalling special treatment.
 * Additionally, every entity labelled as player shall belong to the turn taking
 * family and needs to provide a turn, next player component.
 * @author martin
 */
public class Player extends Entity
{

}
