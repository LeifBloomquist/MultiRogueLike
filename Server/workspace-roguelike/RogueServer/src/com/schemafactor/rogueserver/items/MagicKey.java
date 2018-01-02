package com.schemafactor.rogueserver.items;

import java.util.ArrayList;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Cell;
import com.schemafactor.rogueserver.universe.Dungeon;

// A "magic" Key has a home Chest that it returns to after it has been used  (TODO, general Container?)
public class MagicKey extends Key
{    
    Chest home = null;
    
    /** Creates a new instance of Key */
    public MagicKey(String description, Position whichDoor, Chest home)
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