package com.schemafactor.rogueserver.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.ServerControlled;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;
import com.schemafactor.rogueserver.entities.ServerControlled.States;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Skeleton extends ServerControlled
{  
    /** Creates a new instance of the Skeleton */
    public Skeleton(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_SKELETON, 300f, 10f);    
    }
    
    @Override
    public void takeAction()
    {   
        boolean moved = false;
        
        switch (State)
        {
            case IDLE:
            {                
                // Wait for trouble               
                
                // Is a Human entity nearby?
                List<Entity> nearby = Dungeon.getInstance().getEntitiesRange(this, 5);
                List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(this, entityTypes.HUMAN_PLAYER, nearby);
                
                if (nearby_humans.size() == 0) // All clear, keep waiting
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
            
            case WANDERING:  // Never
            { 
                State = States.IDLE;
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
                // TODO - fix case where target is on a different level
                byte attack_direction = getDirectionTo(target);

                moved = attemptAttack(attack_direction);
                
                if (distanceTo(target) > 4)
                {
                    State = States.CHASING;
                }
                
                if (distanceTo(target) > 10)  // Too far  (just off screen...)
                {
                    State = States.IDLE;
                }
                
                break;
            }
            
            case RETREATING:
            {
                break;
            }
        }
        
        finishMove(moved);
    }
}