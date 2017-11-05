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
        // (TODO, could be Entity?)    
        
        if (item == null)  // Cell is empty
        {
            return charCode;
        }
        else
        {
            return item.getCharCode();
        }        
    }
    
    public boolean canEnter() 
    {        
    	if (entity != null)  // Someone's already in this cell
    	{
    		return false;
    	}
    	
    	if (charCode != 0)   // TODO, use proper attributes/lookup tables
    	{
    		return false;
    	}
        
    	return true;
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
        this.item = null;
        return taken;
    }
}
