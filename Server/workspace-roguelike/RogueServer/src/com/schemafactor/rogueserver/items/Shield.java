package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;

public class Shield extends Item
{
    /** Creates a new instance of Shield */
   public Shield(String description, float maxProtection)
   {
       super(description, Constants.CHAR_ITEM_SHIELD, true, 0, maxProtection); 
   }
}