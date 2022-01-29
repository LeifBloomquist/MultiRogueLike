package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.interfaces.Container;
import com.schemafactor.rogueserver.entities.Entity;

public class Chest extends Item implements java.io.Serializable, Container
{    
    private Item myItem = null;
    private boolean opened = false;
    
    /** Creates a new instance of Chest */
    public Chest(String description, Item inside)
    {
       super(description, Constants.CHAR_ITEM_CHEST, false, 0, 0); 
       this.myItem = inside;
       this.isContainer = true;
    }
    
    /** Creates a new instance of Chest containing gold*/
    public Chest(String description, int gold)
    {
       super(description, Constants.CHAR_ITEM_CHEST, false, 0, 0); 
       this.myItem = new Gold(gold);
       this.isContainer = true;
    }
    
    @Override
    public boolean useItem(Entity entity)
    { 
       opened = !opened;
       
       if (opened)
       {
           entity.addMessage(this.description + " opened.");
       }
       else
       {
           entity.addMessage(this.description + " closed.");
       }
       
       return true;   // maybe false on locked?  TODO
    }
    
    @Override
    public byte getSeenCharCode()
    {
        if (!opened)
        {
            return charCode;
        }
        else
        {
            if (myItem != null)   // Not empty?
            {
                return myItem.charCode;
            }
            else
            {
                return Constants.CHAR_ITEM_CHEST_OPEN; 
            }
        }
    }
    
    @Override
    public byte getCharCode() 
    {
        if (opened)
        {
            return Constants.CHAR_ITEM_CHEST_OPEN;
        }
        else
        {
            return Constants.CHAR_ITEM_CHEST;
        }
    }

    @Override
    public Item getItem()
    {
        if (opened)
        {
            return myItem;
        }
        else
        {
            return null;
        }
    }
    
    // Only used by "Magic" items
    public boolean setContainedItem(Item magicitem)
    {
        myItem = magicitem;
        return true;
    }
    
    @Override
    public boolean placeItem(Item item)
    {
        if (opened)
        {
            if (myItem == null)  // Empty
            {
                myItem = item;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public Item takeItem()
    {
        if (!opened)   // Chests have to be open
        {
            return null;
        }
        
        Item taken = this.myItem;
        
        // If cell is empty, return null
        if (taken == null)
        {
            return null;
        }   
        
        // Clear item
        this.myItem = null;
        
        // Return to picker-upper
        return taken;
    }
}
