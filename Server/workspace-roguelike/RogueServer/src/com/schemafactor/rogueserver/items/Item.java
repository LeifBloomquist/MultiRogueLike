package com.schemafactor.rogueserver.items;

public abstract class Item 
{
   public static enum itemTypes {NONE, WEAPON, KEY, TREASURE, LEVER }
   protected itemTypes myType = itemTypes.NONE;
	   
   protected String description;
   // Items don't have a "position" per se, the Entity carrying them or the Cell holding them has a pointer to the Item instance in question.
   
   protected byte charCode = 0;   // Character code shown on client screen   
   protected float maxDamage = 0;   
      
   /** Creates a new instance of Item */
   public Item(String description, itemTypes type, byte charCode, boolean moveable, float maxDamage)
   {
       this.description = new String(description);    
       this.myType = type;
       this.charCode = charCode;
       this.maxDamage = maxDamage;
       
       // Mark cell this item starts in
       //Dungeon.getInstance().getCell(startposition).setItem(this);
   }
   
   public String getDescription() 
   {
       return description;
   }

	public byte getCharCode() 
	{
		return charCode;
	}

	public itemTypes getType() 
	{		
		return myType;
	} 
	
	public float getMaxDamage() 
    {       
        return maxDamage;
    } 
}