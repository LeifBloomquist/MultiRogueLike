package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;

public class Gold extends Item
{
    private int amount = 0;
    
    /** Creates a new instance of Gold */
    public Gold(int amount)
    {
       super("Gold", Constants.CHAR_ITEM_GOLD, true, 0, 0); 
       this.amount = amount;
    }
}
