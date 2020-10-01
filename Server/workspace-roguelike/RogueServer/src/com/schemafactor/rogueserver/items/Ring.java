package com.schemafactor.rogueserver.items;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Rechargeable;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Ring extends Item implements Rechargeable
{
    private static final long serialVersionUID = 1L;
    
    private Entity wearer = null;
    
    private float charges = 0;
    private float max_charges = 0;
    
    private static final float recharge_rate = 1000f / (86400f * (1000f / (float) Constants.TICK_TIME));   // Recharge to 1000 over a day

    /** Creates a new instance of Gem */
    public Ring(int charges)
    {
       super("Ring", Constants.CHAR_ITEM_RING, true, 0, 0);
       this.charges = charges;
       this.max_charges = charges;      
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
    		charges -= recharge_rate;
    		
    		if (charges <= 0)
    		{
    			visible();
    		}    		
    	}
    	else
    	{
    		if (charges < max_charges)
            {
    			 charges += recharge_rate;
            }           
        }
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