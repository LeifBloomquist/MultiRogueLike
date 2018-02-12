package com.schemafactor.rogueserver.items;

import java.util.Timer;
import java.util.TimerTask;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Container;
import com.schemafactor.rogueserver.entities.Entity;

// A "magic" Key has a home Container that it returns to after it has been used
// then relocks its Door after 10 seconds

public class MagicKey extends Key
{    
    Container home = null;
    
    Timer timer = new Timer();
    
    TimerTask closeDoorTask = new TimerTask() 
    {
        @Override
        public void run() 
        {
            MagicKey.this.relock();
        }
    };
    
    /** Creates a new instance of Key */
    public MagicKey(String description, Position whichDoor, Container home)
    {
       super(description, whichDoor); 
       this.home = home;
    }
    
    @Override
    public boolean useItem(Entity entity)
    {   
       boolean success = super.unlock(entity);  // Magic Keys only unlock doors
    
       if (success)
       {
           timer.schedule(closeDoorTask, Constants.DOOR_RELOCK_TIME); 
           
           if (home != null)
           {
               entity.forceDrop(this);        // Remove from player's inventory
               home.setContainedItem(this);   // Return home
           }
       }
       
       return success;
    }
    
    public boolean relock()
    {
        return super.lock(null);
    }
}