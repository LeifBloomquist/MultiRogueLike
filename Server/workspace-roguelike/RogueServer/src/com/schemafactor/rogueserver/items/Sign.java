package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;

public class Sign extends Item
{
	private static final long serialVersionUID = 1L;
	String text = "";
    
    /** Creates a new instance of Sign */
   public Sign(String description, String text)
   {
       super(description, Constants.CHAR_ITEM_SIGN, false, 0, 5);
       this.text =  "\"" + text + "\""; 
   }
   
   @Override
   public boolean useItem(Entity entity)
   {       
      entity.addMessage(text);
      return true;
   } 
}
