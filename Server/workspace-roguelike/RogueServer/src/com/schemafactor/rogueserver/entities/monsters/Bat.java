package com.schemafactor.rogueserver.entities.monsters;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Bat extends Monster
{  
    /** Creates a new instance of the Bat */
    public Bat(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_BAT, 400f, 3f);    
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
                moved = attemptMove((byte)JavaTools.generator.nextInt(Constants.DIRECTION_2D));      // Stay on level           
                
                // Is a Human entity too close?
                List<Entity> nearby = Dungeon.getInstance().getEntitiesRange(this, 3);
                List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(this, entityTypes.HUMAN_PLAYER, nearby);
                
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
