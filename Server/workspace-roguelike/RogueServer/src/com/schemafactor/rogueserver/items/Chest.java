package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;

public class Chest extends Item
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
    public byte getCharCode() 
    {
        if (!opened)
        {
            return charCode;
        }
        else
        {
            if (myItem != null)
            {
                return myItem.charCode;
            }
            else
            {
                return charCode;  // TODO, character for open but empty chests? 
            }
        }
    }

    @Override
    public Item getContainedItem()
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
    
    @Override
    public void clearContainedItem()
    {
        myItem = null;
    } 
    
    @Override
    public boolean placeItem(Item item)
    {
        if (opened)
        {
            myItem = item;
            return true;
        }
        else
        {
            return false;
        }
    }
}