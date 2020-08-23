package com.schemafactor.rogueserver.entities.monsters;

import java.time.Duration;
import java.time.Instant;

import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.Entity;

public abstract class Monster extends Entity
{  
    protected static final long serialVersionUID = 1L;

    // The entity this creature is currently chasing    
    Entity target = null;   
    
    // States
    protected enum States {IDLE, WANDERING, CHASING, ATTACKING, RETREATING};
    protected States State = States.IDLE;
    
    // Time in between moves.  May change based on state.  
    protected float actionTime = 1000f;   // Milliseconds   
    
    /** Creates a new instance of Monster */
    public Monster(String name, Position startposition, entityTypes type, byte charCode, float actionTime, float maxDamage)
    {
       super(name, startposition, type, charCode, maxDamage);  
       this.actionTime = actionTime;
       home = new Position(this.position);  // Copy
    }
    
    @Override
    public void action()
    { 
        Duration elapsed = Duration.between(lastAction, Instant.now());
        
        if (elapsed.toMillis() <= actionTime)   // Move at this rate
        {
            return;   // Not time to act yet           
        }
        else
        {
            takeAction();
        }
        
        // Check health
        checkHealth(null);
        
        // For server-controlled entities, remove from game immediately
        if (isDead()) 
        {
            removeMeFlag = true;
        }
    }

	@Override
	public void update() 
	{
		;
	}
    
    @Override
    public void updateNow()
    {
       ;        
    }
    
    @Override
    public void addMessage(String msg)
    {
        ;    // Monsters don't need the message queue - maybe in future?  
    }
    
    @Override
    public void playSound(byte id)
    {
        ;
    }
    
    abstract protected void takeAction();  // Action to take when actionTime occurs
}