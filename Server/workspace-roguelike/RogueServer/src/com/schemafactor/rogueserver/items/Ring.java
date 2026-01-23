package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.interfaces.Rechargeable;
import com.schemafactor.rogueserver.entities.Entity;

public class Ring extends Item implements Rechargeable
{
    private static final long serialVersionUID = 1L;
    
    private Entity wearer = null;
    
    private float charge = 0;
    private float max_charge = 0;
    
    private static final float recharge_rate = 100000f / (86400f * (1000f / (float) Constants.TICK_TIME));   // Recharge to 100000f over a day

    /** Creates a new instance of Ring */
    public Ring(String description, int charge)
    {
       super("Ring", Constants.CHAR_ITEM_RING, true, 0, 0);
       this.charge = charge;
       this.max_charge = charge;      
    }
    
    @Override
    public boolean useItem(Entity entity)
    {
    	wearer = entity;
    	wearer.addMessage("You are invisible!");
    	wearer.isInvisible = true;
    	return true;        
    }
    
    @Override
    public void dropped()
    {
    	visible();    	
    }

    public void recharge()   // Called on every game update cycle
    {
    	if (wearer != null)
    	{
    		charge -= recharge_rate;
    		
    		if (charge <= 0)
    		{
    			visible();
    		}    		
    	}
    	else
    	{
    		if (charge < max_charge)
            {
    			 charge += recharge_rate;
            }           
        }
    	
    	// JavaTools.printlnTime("DEBUG: Ring Charge=" + charge);
    }
    
    private void visible()
    {
    	if (wearer != null)
    	{
    		wearer.isInvisible = false;
    		wearer.addMessage("You are visible!");
    		wearer = null;    		
    	}
    }
}