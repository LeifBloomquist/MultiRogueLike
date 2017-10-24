package com.schemafactor.rogueserver.entities;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.ServerControlled;

public class Spider extends ServerControlled
{  
    /** Creates a new instance of the Spider */
    public Spider(String name, Position startposition)
    {
       super(name, startposition, entityTypes.SPIDER, Constants.CHAR_MONSTER_SPIDER);    
    }

    @Override
    public void update() 
    {
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
                // Randomly switch directions.  Do this by switching back to Idle momentarily (next loop). 
                if (JavaTools.generator.nextInt(3000) == 200)
                {
                    State = States.IDLE;
                    break;
                } 
                
                // Avoid objects - TODO
                
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

        move();
    }   
}