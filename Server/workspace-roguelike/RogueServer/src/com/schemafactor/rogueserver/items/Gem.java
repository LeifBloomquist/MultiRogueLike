package com.schemafactor.rogueserver.items;

import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Rechargeable;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Gem extends Item implements Rechargeable
{
    private static final long serialVersionUID = 1L;
    
    Dungeon dungeon = Dungeon.getInstance();
    
    private float charges = 0;
    private float max_charges = 0;
    
    private static final float recharge_rate = 10f / (86400f * (1000f / (float) Constants.TICK_TIME));   // Recharge to 10 over a day

    /** Creates a new instance of TeleportGem */
    public Gem(int charges)
    {
       super("Blue Gem", Constants.CHAR_ITEM_GEM, true, 0, 0);
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
       List<Entity> in_range = dungeon.getEntitiesRange(entity.getPosition(), (int)charges);  // Includes the one using the item
       List<Entity> clients_in_range = dungeon.getEntitiesType(null, Entity.entityTypes.CLIENT, in_range);  // But not monsters, although that would be cool
       
       Position target_pos = dungeon.getRandomPosition();
       
       for (Entity e : clients_in_range)
       {
           e.attemptTeleport(target_pos);
       } 
    }

    public void recharge()   // Called on every game update cycle
    {
        if (charges < max_charges)
        {
            charges += recharge_rate;
            JavaTools.printlnTime("DEBUG: Teleport Gem recharging to " + charges );
        }
    }
}