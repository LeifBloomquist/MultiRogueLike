package com.schemafactor.rogueserver.items;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Rechargeable;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;

public class Gem extends Item implements Rechargeable
{
    private static final long serialVersionUID = 1L;
    
    Dungeon dungeon = Dungeon.getInstance();
    
    private float charges = 0;
    private float max_charges = 0;
    
    private static final float recharge_rate = 1000f / (86400f * (1000f / (float) Constants.TICK_TIME));   // Recharge to 1000 over a day

    /** Creates a new instance of Gem */
    public Gem(int charges)
    {
       super("Blue Gem", Constants.CHAR_ITEM_GEM, true, 0, 0);
       this.charges = charges;
       this.max_charges = charges;      
    }
    
    @Override
    public boolean useItem(Entity entity)
    {
        final int factor=1;
        
        if (charges >= factor)
        {
            doTeleport(entity);
            charges -= factor;
            return true;
        }
        else
        {
            return false;
        }
    }        
        
    private void doTeleport(Entity entity)
    {
       Position target_pos = dungeon.getRandomEmptyPosition();
       
       // Teleport self, always
       boolean success = entity.attemptTeleport(target_pos);
       
       if (!success) return;
       
       // If successful, teleport other nearby, if enough charge       
       List<Entity> in_range = dungeon.getEntitiesRange(entity, (int)charges);  // Does not include self
       List<Entity> clients_in_range = dungeon.getEntitiesType(null, Entity.entityTypes.CLIENT, in_range);  // But not monsters, although that would be cool      
       
       for (Entity e : clients_in_range)
       {
    	   Position nearby_pos = dungeon.getClosestEmptyCell(target_pos);
           e.attemptTeleport(nearby_pos);
       } 
    }

    public void recharge()   // Called on every game update cycle
    {
        if (charges < max_charges)
        {
            charges += recharge_rate;
        }
    }
}