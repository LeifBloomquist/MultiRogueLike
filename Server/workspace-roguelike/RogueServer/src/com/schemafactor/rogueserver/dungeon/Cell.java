package com.schemafactor.rogueserver.dungeon;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.interfaces.Container;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.items.Item;

public class Cell implements java.io.Serializable, Container
{
    private static final long serialVersionUID = 1L;
    
    Entity entity = null;  // Which entity is in this cell, if any.  null if none.
    Item item = null;
        
    private byte charCode = 0;                        // Character code shown on client screen
    private byte charColor = Constants.COLOR_BLACK;   // Foreground color code shown to the client  (though client will likely use a lookup table)
    
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
        if (item != null)  // Cell is not empty
        {
            return item.getSeenCharCode();
        }

        // Unoccupied cell, return base char code.
        return charCode;      
    }

    public byte getTrueCharCode() 
    {
        return charCode;        
    }
    
    // Return true if this is cell can be entered.  Refer to Entity.attemptInspect() for text descriptions.
    public boolean canEnter(Entity who) 
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
            case Constants.CHAR_PORTAL:
            case Constants.CHAR_DOOR_OPEN:
            case Constants.CHAR_ITEM_CHEST:            
    	        return true;
    	        
    	    // Special cases (allowed)    	        
            case Constants.CHAR_SECRET_DOOR:
                if (who != null) who.addMessage("You found a secret door!");
                return true;                
    	    
    	    // Special cases (not allowed)
            case Constants.CHAR_DOOR_CLOSED:
            	  if (who != null) who.addMessage("Door is locked!");
                  return false;
    	    
            case Constants.CHAR_LAVA:
            	  if (who != null) 
            	  {
            		  if (who.getType() == Entity.entityTypes.CLIENT)
	          		  {
	            		  who.addMessage("You can't walk on lava!");
	            		  who.takeDamage(1);
	            		  who.playSound(Constants.SOUND_ATTACKED);
	          		  }
            	  }
                  return false;
                  
            case Constants.CHAR_BARRIER:
	          	  if (who != null)
	          	  {
	          		  if (who.getType() == Entity.entityTypes.MONSTER)
	          		  {
	          			return false;	          			  
	          		  }
	          		  else
	          		  {
	          			return true;  // OK for players
	          		  }
	          	  }
	          	  return false;                
                  
            // Everything else - not allowed
    	    default:
    	        return false;
    	}
    }
    
    // Return true if this is cell is completely empty.
    public boolean isEmpty(boolean drop) 
    {        
        // Is someone already in this cell?
        if (!drop)
        {
            if (entity != null)
            {
                return false;
            }
        }
        
        // Is there an item in this cell?
        if (item != null)  
        {
            return false;
        }
        
        // Is it an empty cell (no stairs, etc)?
        if (charCode == Constants.CHAR_EMPTY)
        {
            return true;
        }
        else
        {
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
    
    // Place an item into this cell.  Returns true on success, false if failure.  
    public boolean placeItem(Item i)
    {
        if (isEmpty(true))   // Cell is empty, except for entities
        {
            item = i;
            return true;
        }
        else
        {
            // Maybe a container?
            if ( (item != null) && (item.isContainer()) )
            {
                Container con = (Container)item;
                return con.placeItem(i);
            }
            return false;
        }
    }
    
    public Item getItem()
    {
        return item;  
    }
    
    public Item takeItem(Entity who)
    {   
        Item taken = this.item;
        
        // If cell is empty, return null
        if (taken == null)
        {
            return null;
        }
        
        // Is this Item a container?
        if (taken.isContainer())
        {
            Container con = (Container)taken;
            return con.takeItem(who);
        }
        
        // Some Items can't be picked up
        if (!taken.isMoveable())
        {
        	if (who!= null) who.addMessage("You can't pick up the " + taken.getDescription());
            return null;
        }       
        
        // Clear item
        this.item = null;
        
        // Return item pointer to picker-upper
        return taken;
    }

    @Override
    public byte getSeenCharCode()
    {       
        return getItemCharCode();
    }

    @Override
    public boolean setContainedItem(Item item)
    {
        this.item = item;
        return true;
    }
}
