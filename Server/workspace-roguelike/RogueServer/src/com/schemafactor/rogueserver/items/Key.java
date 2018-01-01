package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Position;

public class Key extends Item
{
    private Position myDoor = null;
    
    /** Creates a new instance of Key */
    public Key(String description, Position whichDoor)
    {
       super(description, Constants.CHAR_ITEM_KEY, true, 0, 0); 
       this.myDoor = whichDoor; 
    }
    
    zzz handle action here
}