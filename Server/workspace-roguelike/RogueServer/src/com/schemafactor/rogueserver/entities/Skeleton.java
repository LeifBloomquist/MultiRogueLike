package com.schemafactor.rogueserver.entities;

import java.time.Duration;
import java.time.Instant;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.ServerControlled;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Skeleton extends ServerControlled
{  
    /** Creates a new instance of the Skeleton */
    public Skeleton(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_SKELETON, 500f);    
    }

    @Override
    public void update() 
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
                // Occasionally go back to Idle
                //if (JavaTools.generator.nextInt(3000) == 1)
                //{
                //    State = States.IDLE;
                //    break;
                //} 
               
                Duration elapsed = Duration.between(lastAction, Instant.now());
                
                if (elapsed.toMillis() >= actionTime)  // Move at this rate
                {
                    // Randomly move in a direction. 
                    moved = attemptMove((byte)JavaTools.generator.nextInt(Constants.DIRECTION_COUNT));
                }
                
                break;
            }
        
            case CHASING:
            {
                break;
            }
            
            case ATTACKING:
            {
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