package com.schemafactor.rogueserver.items;

import java.util.Timer;
import java.util.TimerTask;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Container;
import com.schemafactor.rogueserver.entities.Entity;

// A "magic" Key has a home Container that it returns to after it has been used
// then relocks its Door after 10 seconds

public class MagicKey extends Key
{    
	private static final long serialVersionUID = 1L;
	
	Container home = null;
    Timer timer = new Timer();
    
    /** Creates a new instance of MagicKey */
    public MagicKey(String description, Position whichDoor, Container home)
    {
       super(description, whichDoor); 
       this.home = home;
    }
    
    @Override
    public boolean useItem(Entity entity)
    {   
       boolean success = super.unlock(entity);  // Magic Keys only unlock doors - no lock
    
       if (success)
       {
           JavaTools.printlnTime("DEBUG: Magic Key " + description + " unlocked door");
          
           // Start the countdown timer to relock the door
           
           TimerTask closeDoorTask = new TimerTask() 
           {
               @Override
               public void run() 
               {
                   MagicKey.this.relock();
               }
           };
          
           timer.schedule(closeDoorTask, Constants.DOOR_RELOCK_TIME);
           
           // Return the key home
           
           if (home != null)
           {
        	   synchronized(this)
        	   {
                   // 1. Return home
        		   success = home.setContainedItem(this);   
        		   
        		   if (!success)
        		   {
        			   JavaTools.printlnTime("DEBUG: Magic Key " + description + " FAILED to return home!");
        		   }
        		   
                   //  2. Remove from player's inventory
	               success = entity.forceDrop(this);        // Remove from player's inventory
	               
	               if (!success)
        		   {
        			   JavaTools.printlnTime("DEBUG: Magic Key " + description + " FAILED to drop from player inventory");
        		   }
        	   }
        	   
        	   // 3. Sanity check
        	   
        	   success = (home.getItem() == this);
        			   
        	   if (success)
        	   {
        		   JavaTools.printlnTime("DEBUG: Magic Key " + description + " successfully returned home");   
        	   }
        	   else
        	   {
        		   JavaTools.printlnTime("DEBUG: Magic Key " + description + " FAILED to return home");
        	   }
           }
           else
           {
        	   JavaTools.printlnTime("DEBUG: Magic Key " + description + " home==null?");
           }       
       }  
       else
       {
           //JavaTools.printlnTime("DEBUG: Magic Key " + description + " unlock door FAILED!");
       }
       
       return success;
    }
    
    public void relock()
    {        
        door.setAttributes(Constants.CHAR_DOOR_CLOSED);
        //JavaTools.printlnTime("DEBUG: Magic Key " + description + " relocked door");
    }
}