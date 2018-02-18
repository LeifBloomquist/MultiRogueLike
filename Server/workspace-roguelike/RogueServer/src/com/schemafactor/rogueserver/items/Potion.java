package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;

public class Potion extends Item
{
    private float amount = 0;
    private float max_amount = 0;
    
    private static final float recharge_rate = 100f / (3600f * (1000f / (float) Constants.TICK_TIME));   // Recharge to 100 over an hour
    
    /** Creates a new instance of Potion */
    public Potion(int amount)
    {
       super("Potion", Constants.CHAR_ITEM_POTION, true, 0, 0); 
       this.amount = (float) amount;
       this.max_amount = (float) amount;
    }
    
    @Override
    public boolean useItem(Entity entity)
    {  
        final int factor=1;
        
       if (amount >= factor)
       {
           entity.addHealth(factor);
           amount -= factor;
           return true;
       }
       else
       {
           return false;
       }
    } 
    
    public void recharge()   // Called on every game update cycle
    {
        if (amount < max_amount)
        {
            amount += recharge_rate;
            // JavaTools.printlnTime("DEBUG: Potion recharging to " + amount );
        }
    }
}
