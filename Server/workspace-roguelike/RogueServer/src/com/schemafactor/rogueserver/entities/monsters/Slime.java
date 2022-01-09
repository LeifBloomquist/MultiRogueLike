package com.schemafactor.rogueserver.entities.monsters;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;

public class Slime extends Monster
{ 
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of the Slime */
    public Slime(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_SLIME, 1000f, 20f, 80f);    
    }

    @Override
    public void takeAction() 
    {
        boolean moved = false;
        
        switch (State)
        {
            case IDLE:
            {                
                // Always go wandering
                State = States.WANDERING;
                break;
            }
               
            case WANDERING:
            {               
                // Occasionally go into turbo mode
                if (JavaTools.generator.nextInt(10) == 1)
                {
                    this.actionTime = 100f;                    
                }
                
                // Occasionally exit turbo mode
                if (JavaTools.generator.nextInt(10) == 1)
                {
                    this.actionTime = 1000f;                    
                }
                
                moved = attemptMove((byte)JavaTools.generator.nextInt(Constants.DIRECTION_2D));               
                
                // Is a Human entity too close?
                List<Entity> nearby = Dungeon.getInstance().getEntitiesRange(this, 3);
                List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(this, entityTypes.CLIENT, nearby);
                
                if (nearby_humans.size() == 0) // All clear
                {
                    break;
                }
                else  // Attack!
                {
                    target = nearby_humans.get(0);
                    
                    double target_distance = distanceTo(target);
                    if (target_distance <= 1.5d)
                    {
                        State = States.ATTACKING;
                    }
                    break;
                }
            }
            
            case ATTACKING:
            {
            	// Can't smell
                if (target != null)
                {
                	if (target.isInvisible)                
	                {
	                	State = States.WANDERING;
	                	break;
	                }
                }
                
                // Just take one attack, then wander again
                byte attack_direction = getDirectionTo(target);
                moved = attemptAttack(attack_direction);
                State = States.WANDERING;                
                break;
            }
        
            default:
                State = States.WANDERING;
                break;
            
        }
        
        finishMove(moved);
    }
}