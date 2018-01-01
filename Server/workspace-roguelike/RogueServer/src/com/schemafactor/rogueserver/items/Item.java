package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.entities.Entity;

public abstract class Item implements java.io.Serializable
{
    // Items don't have a "position" per se, the Entity carrying them or the Cell holding them has a pointer to the Item instance in question.
	   
   private static final long serialVersionUID = 1L;
    
   protected String  description;
   protected byte    charCode   = 0;     // Character code shown on client screen   
   protected float   maxDamage  = 0;     // Maximum damage this items can inflict if used by a weapon
   protected float   maxProtect = 0;     // Maximum damage this item can deflect
   protected boolean moveable   = true;  // Can this item be picked up?  True if so
   protected boolean isContainer= false; // Can this item contain other Items?  True if so
      
   /** Creates a new instance of Item 
 * @param maxProtection */
   public Item(String description, byte charCode, boolean moveable, float maxDamage, float maxProtection)
   {
       this.description = new String(description);
       this.charCode = charCode;
       this.moveable = moveable;
       this.maxDamage = maxDamage;
       this.maxProtect = maxProtection;
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

    public float getMaxProtection() 
    {       
        return maxProtect;
    } 
	
	public boolean isMoveable() 
    {       
        return moveable;
    }

    public boolean useItem(Entity entity)
    {       
        return false;
    }

    public boolean isContainer()
    {
        return this.isContainer;
    } 
    
    public Item getContainedItem()
    {
        return null;
    }

    public void clearContainedItem()
    {
        ;
    } 
}