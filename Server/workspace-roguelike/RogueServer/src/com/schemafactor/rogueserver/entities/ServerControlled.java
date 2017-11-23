package com.schemafactor.rogueserver.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public abstract class ServerControlled extends Entity
{  
    // The entity this creature is currently chasing    
    Entity target = null;   
    
    // States
    protected enum States {IDLE, WANDERING, CHASING, ATTACKING, RETREATING};
    protected States State = States.IDLE;
    
    // Time in between moves.  May change based on state.  
    float actionTime = 1000f;  // Milliseconds    
       
    /** Creates a new instance of Server Controlled */
    public ServerControlled(String name, Position startposition, entityTypes type, byte charCode, float actionTime, float maxDamage)
    {
       super(name, startposition, type, charCode, maxDamage);  
       this.actionTime = actionTime;
    }
    
    /*
    protected void navigateTo(Entity target, double visible_range, double target_attraction, double repelling, double avoidance_range, double avoiding)   
    {    
        if (target == null) return;
        
        List<Entity> allInRange = universe.getEntities(this, visible_range);
        
        // make sure target is always known.  Consider using a Set here.
        if (!allInRange.contains(target))
        {
            allInRange.add(target);
        }
        
        for (Entity e : allInRange)
        {
            double force = 0;
            
            if (target == e)
            {
                force = target_attraction;    // Attracted to target
            }
            else
            {
                force = -repelling;  // Note negative - this repels 
            } 
            
            // But don't get too close!
            if (distanceTo(e) < avoidance_range) 
            {
                force = -avoiding;
            }            
         
            double angle2 = angleTo(e); 
            double Xdelta =  force * Math.cos(angle2); 
            double Ydelta = -force * Math.sin(angle2);   // Negative here because our y-axis is inverted      

            Xspeed += 0.1*Xdelta;
            Yspeed += 0.1*Ydelta;              
        }
    }
    */
}