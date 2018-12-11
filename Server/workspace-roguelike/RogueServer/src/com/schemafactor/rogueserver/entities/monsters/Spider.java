package com.schemafactor.rogueserver.entities.monsters;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Spider extends Monster
{  
    /** Creates a new instance of the Spider */
    public Spider(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_SPIDER, 700f, 3f);    
    }

    @Override
    public void takeAction()
    {   
        // TODO Check logic where Spiders sometimes stop chasing targets
        
        boolean moved = false;

        if (target == null) //  No target.  Wander around.
        {
            State = States.WANDERING;
        }
        else
        {
            if (target.getRemoved())   // Target disconnected, or was removed/killed
            {
                target = null;
                State = States.WANDERING;
            }
        }
        
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
                // Randomly move in a direction. 
                moved = attemptMove((byte)JavaTools.generator.nextInt(Constants.DIRECTION_2D));
                
                // Is a Human entity nearby?
                List<Entity> nearby = Dungeon.getInstance().getEntitiesRange(this, 10);
                List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(this, entityTypes.CLIENT, nearby);
                
                if (nearby_humans.size() == 0) // All clear
                {
                    break;
                }
                else  // Attack!
                {
                    target = nearby_humans.get(0);
                    State = States.CHASING;
                }
                
                break;
            }
        
            case CHASING:
            {
                byte chase_direction = getDirectionTo(target);
                moved = attemptMove(chase_direction);
                
                double target_distance = distanceTo(target);
                if (target_distance <= 1.5d)
                {
                    State = States.ATTACKING;
                }
                break;
            }
            
            case ATTACKING:
            {
                byte attack_direction = getDirectionTo(target);
                
                if (attack_direction == Constants.DIRECTION_NONE)  // Gone, or other level
                {
                    State = States.WANDERING;
                }

                moved = attemptAttack(attack_direction);
                
                if (distanceTo(target) > 1.5d)
                {
                    State = States.CHASING;
                }
                
                if (distanceTo(target) > 20)  // Too far
                {
                    State = States.WANDERING;
                }
                
                break;
            }
            
            case RETREATING:
            {
                State = States.WANDERING;
            }
        }
        
        finishMove(moved);
    }
}