package com.schemafactor.rogueserver.universe;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;

public class Cell 
{
    Entity entity = null;  // Which entity is in this cell, if any.  null if none.
        
    private byte charCode = 0;   // Character code shown on client screen
    private byte charColor = Constants.COLOR_BLACK;   // Foreground color code shown to the client  (though will likely use a lookup table)    
    
    public byte getCharCode() 
    {
        return charCode;
    }
    
    public boolean canEnter() 
    {
    	if (entity != null)
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
}
