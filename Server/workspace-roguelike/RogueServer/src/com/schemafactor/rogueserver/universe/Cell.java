package com.schemafactor.rogueserver.universe;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Position;
import com.schemafactor.rogueserver.items.Item;

public class Cell 
{
    Entity entity = null;  // Which entity is in this cell, if any.  null if none.
    Item item = null;
        
    private byte charCode = 0;   // Character code shown on client screen
    private byte charColor = Constants.COLOR_BLACK;   // Foreground color code shown to the client  (though will likely use a lookup table)    
    
    public byte getCharCode() 
    {
        // Entities take top priority
        if (entity != null)
        {
            return entity.getCharCode();
        }
        
        // Then items        
        if (item != null)  // Cell is empty
        {
            return item.getCharCode();
        }

        // Unoccupied cell.
        return charCode;        
    }
    
    public byte getItemCharCode() 
    {    
        if (item != null)  // Cell is empty
        {
            return item.getCharCode();
        }

        // Unoccupied cell.
        return charCode;      
    }

    public byte getTrueCharCode() 
    {
        return charCode;        
    }
    
    public boolean canEnter() 
    {        
    	if (entity != null)  // Someone's already in this cell
    	{
    		return false;
    	}
    	
    	switch (charCode)
    	{
    	    // These cell types are allowed to be walked on
    	    case Constants.CHAR_EMPTY:
    	    case Constants.CHAR_STAIRS_DOWN:
            case Constants.CHAR_STAIRS_UP:
            case Constants.CHAR_CHEST:
    	        return true;
    	        
    	    default:
    	        return false;
    	}
    }    

    @Deprecated
    public byte getCharColor() 
    {
        return charColor;
    }
    
    /** Set the character cell attributes */
    public void setAttributes(int charCode)
    {
        this.charCode = (byte)(charCode & 0xFF);
    }
    
    /** Set the entity currently in this cell */
    public void setEntity(Entity e)
    {
        this.entity = e;
    }
    
    /** Get the entity currently in this cell */
    public Entity getEntity()
    {
        return entity;
    }
    
    public boolean addItem(Item i)
    {
        if (item == null)  // Cell is empty
        {
            item = i;
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Item getItem()
    {
        return item;  
    }
    
    public Item takeItem()
    {   
        Item taken = this.item;
        
        // If cell is empty, return null
        if (taken == null)
        {
            return null;
        }        
        
        // Some Items can't be picked up
        if (!taken.isMoveable())
        {
            return null;
        }       
        
        // Clear item
        this.item = null;
        
        // Return pointer to picker-upper
        return taken;
    }
}
