package com.schemafactor.rogueserver.entities.monsters;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Ghost extends Monster
{  
    private static final long serialVersionUID = 1L;
    
    byte direction = Constants.DIRECTION_NONE;

    /** Creates a new instance of the Ghost */
    public Ghost(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_GHOST, 600f, 20f);
    }
    
    @Override
    public void takeAction()
    {   
        boolean moved = false;
        
        if (target != null)
        {
            if (target.getRemoved())   // Target disconnected, or was removed/killed
            {
                target = null;
                State = States.IDLE;
            }
        }
        
        switch (State)
        {
            case IDLE:
            {  
                // Pick a new direction to wander in
                direction = (byte)(1 + JavaTools.generator.nextInt(Constants.DIRECTION_NW));   // Never None or Up or Down
                State = States.WANDERING;
                break;
            }
            
            case WANDERING:  // Keep going in last direction
            { 
                moved = attemptMove(direction);
                if (!moved)
                {
                    State = States.IDLE;   // Failed, pick a new direction                   
                }   
                
                // Is a Human entity too close?
                List<Entity> nearby = Dungeon.getInstance().getEntitiesRange(this, 1.5);
                List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(this, entityTypes.HUMAN_PLAYER, nearby);
                
                if (nearby_humans.size() == 0) // All clear
                {
                    break;
                }
                else  // Attack!
                {
                    target = nearby_humans.get(0);
                    byte attack_direction = getDirectionTo(target);
                    moved = attemptAttack(attack_direction);
                    break;
                }
            }                  
        
            case CHASING:
            {
                State = States.IDLE;   // Pick a new direction
                break;
            }
            
            case ATTACKING:
            {                
                State = States.IDLE;   // Pick a new direction
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