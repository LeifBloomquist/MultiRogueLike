package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;

public class Note extends Item
{
    String text = "";
    
    /** Creates a new instance of Note */
   public Note(String description, String text)
   {
       super(description, Constants.CHAR_ITEM_NOTE, true, 0, 0); 
       this.text =  "\"" + text + "\""; 
   }
   
   @Override
   public boolean useItem(Entity entity)
   {       
      entity.addMessage(text);
      return true;
   } 
}