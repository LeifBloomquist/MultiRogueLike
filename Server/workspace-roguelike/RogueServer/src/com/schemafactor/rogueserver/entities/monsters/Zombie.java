package com.schemafactor.rogueserver.entities.monsters;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Zombie extends Monster
{  
    private static final long serialVersionUID = 1L;

    /** Creates a new instance of the Zombie */
    public Zombie(String name, Position startposition)
    {
       super(name, startposition, entityTypes.MONSTER, Constants.CHAR_MONSTER_ZOMBIE, 1000f, 30f, 200);      
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
                // Is a Human entity nearby?
                List<Entity> nearby = Dungeon.getInstance().getEntitiesRange(this, 5);
                List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(this, entityTypes.CLIENT, nearby);
                
                if (nearby_humans.size() == 0) // All clear, keep idling
                {
                	State = States.IDLE;
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
                
                if (chase_direction == Constants.DIRECTION_NONE)  // Gone, or other level
                {
                    State = States.IDLE;
                }
                
                moved = attemptMove(chase_direction);
                
                double target_distance = distanceTo(target);
                
                if (target_distance <= 1.5d)
                {
                    State = States.ATTACKING;
                    break;
                }
                
                if (distanceTo(target) > 10)  // Too far  (just off screen...)
                {
                    State = States.IDLE;
                }
                
                break;
            }
            
            case ATTACKING:
            {                
                byte attack_direction = getDirectionTo(target);
                
                if (attack_direction == Constants.DIRECTION_NONE)  // Gone, or other level
                {
                    State = States.IDLE;
                    break;
                }

                moved = attemptAttack(attack_direction);
                
                if (distanceTo(target) > 1.5d)
                {
                    State = States.CHASING;
                }
                
                break;
            }
            
            case RETREATING:
            {
                State = States.IDLE;  // Never retreat!
            }
        }
        
        finishMove(moved);
    }
}