package com.schemafactor.rogueserver.entities;

import java.awt.Color;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.vortexserver.entities.Entity.eTypes;

public abstract class Entity 
{
   public static enum entityTypes {NONE, HUMAN_PLAYER, NPC, SPIDER}
   protected entityTypes myType = entityTypes.NONE;
	   
   protected String description;
   protected Position position;
   
   protected byte charCode = 0;   // Character code shown on client screen
   
   /** Flag that this entity is to be removed at the end of this update cycle.  true=remove */
   protected boolean removeMeFlag = false;
     
   /** Creates a new instance of Entity */
   public Entity(String description, Position startposition, entityTypes type)
   {
       this.description = new String(description);
       this.position = startposition;
       this.myType = type;
   }
   
   public void move()
   {

   }
   
   abstract public void update(); 
   
   /** Return X,Y positions */
   public long getXpos()
   {
       return position.x;
   }
   
   public long getYpos()
   {
       return position.y;
   }
   
   public String getDescription() 
   {
       return description;
   }
   
   // Helper function to get distance to another Entity
   public double distanceTo(Entity target)
   {   
       if (target == null)
       {
           return Double.MAX_VALUE;
       }
       
       return Math.sqrt( Math.pow((this.getXpos() - target.getXpos()), 2) + Math.pow((this.getYpos() - target.getYpos()), 2)); 
   }

    public boolean removeMe() 
    {        
        return removeMeFlag;
    }

	public byte getCharCode() 
	{
		return charCode;
	}

	public entityTypes getType() 
	{		
		return myType;
	}
}