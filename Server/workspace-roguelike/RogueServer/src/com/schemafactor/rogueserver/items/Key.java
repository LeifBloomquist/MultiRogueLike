package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.universe.Cell;

public class Key extends Item
{
    private Cell myDoor = null;
    
    /** Creates a new instance of Key */
   public Key(String description, Cell whichDoor)
   {
       super(description, Constants.CHAR_ITEM_KEY, true, 0, 0); 
       this.myDoor = whichDoor; 
   }

    public Cell getMyDoor()
    {
        return myDoor;
    }
}