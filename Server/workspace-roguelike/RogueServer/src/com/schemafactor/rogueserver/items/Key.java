package com.schemafactor.rogueserver.items;

import java.util.ArrayList;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.dungeon.Cell;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;

public class Key extends Item
{
    private Position myDoor = null;
    Cell door = null;
    
    Dungeon dungeon = Dungeon.getInstance();
    
    /** Creates a new instance of Key */
    public Key(String description, Position whichDoor)
    {
       super(description, Constants.CHAR_ITEM_KEY, true, 0, 0); 
       this.myDoor = whichDoor;
       this.door = dungeon.getCell(whichDoor);
    }
    
    @Override
    public boolean useItem(Entity entity)
    {   
       if (myDoor == null)   // No door assigned
       {
           return false; 
       }
       
       return lockUnlock(entity);
    }
    
    private boolean isDoorNearby(Entity entity)
    {
        List<Position> nearby = (ArrayList<Position>) Dungeon.getInstance().getNeighbors(entity.getPosition());
        
        for (Position pos : nearby)
        {
            if (pos.equals(myDoor)) 
            {
               return true;
            }
        }
        
        return false;
    }

    private boolean lockUnlock(Entity entity)
    {        
        if (door.getTrueCharCode() == Constants.CHAR_DOOR_CLOSED)
        {
            return unlock(entity);
        }

        if (door.getTrueCharCode() == Constants.CHAR_DOOR_OPEN)
        {
            return lock(entity);
        }

        JavaTools.printlnTime("DEBUG: Invalid character code when (un)locking door at " + myDoor.toString());
        return false;
    }
    
    protected boolean lock(Entity entity)
    {
        if (!isDoorNearby(entity))
        {
           return false;
        }    

        if (door.getTrueCharCode() == Constants.CHAR_DOOR_OPEN)
        {
            door.setAttributes(Constants.CHAR_DOOR_CLOSED);
            
            if (entity != null)
            {
                entity.addMessage("Door locked.");
            }
            return true;
        }

        JavaTools.printlnTime("DEBUG: Invalid character code when locking door at " + myDoor.toString());
        return false;
    }
    
    protected boolean unlock(Entity entity)
    {
        if (!isDoorNearby(entity))
        {
           return false;
        }
        
        if (door.getTrueCharCode() == Constants.CHAR_DOOR_CLOSED)
        {
            door.setAttributes(Constants.CHAR_DOOR_OPEN);
            
            if (entity != null)
            {
                entity.addMessage("Door unlocked.");
            }
            return true;
        }
        
        if (door.getTrueCharCode() == Constants.CHAR_DOOR_OPEN)  // Already open
        {
            return true;        
        }

        JavaTools.printlnTime("DEBUG: Invalid character code when unlocking door at " + myDoor.toString());
        return false;
    }
}