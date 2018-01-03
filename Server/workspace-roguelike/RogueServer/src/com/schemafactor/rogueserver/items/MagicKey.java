package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Container;
import com.schemafactor.rogueserver.entities.Entity;

// A "magic" Key has a home Chest that it returns to after it has been used  (TODO, general Container?)
public class MagicKey extends Key
{    
    Container home = null;
    
    /** Creates a new instance of Key */
    public MagicKey(String description, Position whichDoor, Container home)
    {
       super(description, whichDoor); 
       this.home = home;
    }
    
    @Override
    public boolean useItem(Entity entity)
    {   
       boolean success = super.useItem(entity);  // Do normal key stuff
    
       if (success)
       {
           if (home != null)
           {
               entity.forceDrop(this);        // Remove from player's inventory
               home.setContainedItem(this);   // Return home
           }
       }
       
       return success;
    }
}