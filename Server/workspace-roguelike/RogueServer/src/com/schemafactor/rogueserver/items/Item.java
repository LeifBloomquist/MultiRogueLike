package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.entities.Entity;

public abstract class Item 
{
    // Items don't have a "position" per se, the Entity carrying them or the Cell holding them has a pointer to the Item instance in question.
	   
   protected String description;
   protected byte    charCode  = 0;     // Character code shown on client screen   
   protected float   maxDamage = 0;     // Maximum damage this items can inflict if used by a weapon
   protected boolean moveable  = true;  // Can this item be picked up?  True if so
      
   /** Creates a new instance of Item */
   public Item(String description, byte charCode, boolean moveable, float maxDamage)
   {
       this.description = new String(description);
       this.charCode = charCode;
       this.maxDamage = maxDamage;
       this.moveable = moveable;
   }
   
   public String getDescription() 
   {
       return description;
   }

	public byte getCharCode() 
	{
		return charCode;
	}
	
	public float getMaxDamage() 
    {       
        return maxDamage;
    } 
	
	public boolean isMoveable() 
    {       
        return moveable;
    }

    public boolean useItem(Entity entity)
    {       
        return false;
    } 
}