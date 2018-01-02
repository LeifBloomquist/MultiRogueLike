package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;

public class Potion extends Item
{
    private int amount = 0;
    
    /** Creates a new instance of Potion */
    public Potion(int amount)
    {
       super("Potion", Constants.CHAR_ITEM_POTION, true, 0, 0); 
       this.amount = amount;
    }
    
    @Override
    public boolean useItem(Entity entity)
    {       
       if (amount > 0)
       {
           entity.addHealth(1);
           amount--;
           return true;
       }
       else
       {
           return false;
       }
    } 
}
