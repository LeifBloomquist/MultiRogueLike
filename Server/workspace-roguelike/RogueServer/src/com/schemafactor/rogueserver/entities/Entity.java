package com.schemafactor.rogueserver.entities;

import java.time.Instant;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.items.Item;
import com.schemafactor.rogueserver.universe.Cell;
import com.schemafactor.rogueserver.universe.Dungeon;

public abstract class Entity implements java.io.Serializable
{
   protected static final long serialVersionUID = 1L;

   public static enum entityTypes {NONE, CLIENT, NPC, MONSTER}
   protected entityTypes myType = entityTypes.NONE;
   
   protected String description;
   protected Position position;      // Current position
   protected Position home = null;   // Home.  For respawning or returning to spawn point
   
   protected byte charCode = 0;   // Character code shown on client screen
   
   /** Flag that this entity is to be updated on this update cycle.  true=update */
   protected boolean updateMeFlag = false;   
   
   /** Flag that this entity is to be removed at the end of this update cycle.  true=remove */
   protected boolean removeMeFlag = false;
   
   public boolean isInvisible = false;
   
   protected Instant lastAction = Instant.now();         // Used by server-controlled entities
   
   protected Item item_left  = null;  // Currently carried item in left hand
   protected Item item_right = null;  // Currently carried item in right hand
   
   protected float health = 100f;
   protected float maxHealth = health;
   float baseDamage = 0f;   // How much damage this entity can do on attack without weapons
     
   /** Creates a new instance of Entity */
   public Entity(String description, Position startposition, entityTypes type, byte charCode, float baseDamage, float maxHealth)
   {
	   if (startposition == null) return; // Dummy entities
	   
       this.description = new String(description);       
       this.position = Dungeon.getInstance().getClosestEmptyCell(startposition, Constants.EMPTY_CELL_SEARCH_DEPTH);
       this.home = startposition;
       
       this.myType = type;
       this.charCode = charCode;
       this.baseDamage = baseDamage;
       this.maxHealth = maxHealth;
       
       // Mark cell this entity starts in
       Dungeon.getInstance().getCell(this.position).setEntity(this);
   }
   
   public void respawn()
   {
       position = new Position(home);
       this.removeMeFlag = false;
       this.needsUpdate();
       health = maxHealth;
       Dungeon.getInstance().addEntity(this);  // Re-add to main list
       this.addMessage("Restarted...");
   }
   
   /**
    * @param direction
    * @return true on success, false if blocked
    */
   protected boolean attemptMove(byte direction) 
   {       
       Cell current_cell = Dungeon.getInstance().getCell(this.position); 
       Position destination = getTargetPosition(current_cell, direction);
       Cell dest_cell = Dungeon.getInstance().getCell(destination);  
       
       if (dest_cell == null)
       {
           JavaTools.printlnTime("DEBUG: " + description + " attempted to move out of map?  Last known location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           this.addMessage("Blocked!!");
           return false;
       }
       
       if (dest_cell.canEnter(this))
       {
           // Update cell references
           this.position = destination;
           dest_cell.setEntity(this);
           current_cell.setEntity(null);
           
           //JavaTools.printlnTime("DEBUG: " + description + " moved to location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           this.playSound(Constants.SOUND_PLAYER_STEP);
           attemptInspect(false);  // Report what is in the new cell, if anything
           return true;
       }
       else
       {
           this.playSound(Constants.SOUND_BLOCKED);
           return false;
       }    
   }
   
   // Teleport to closest empty cell to a random position.
   public boolean attemptTeleport()
   {
       Position target_pos = Dungeon.getInstance().getRandomEmptyPosition();
       return attemptTeleport(target_pos);
   }
   
   // Teleport to closest empty cell to the specified position.
   public boolean attemptTeleport(Position target_pos) 
   {       
       Cell current_cell = Dungeon.getInstance().getCell(this.position); 
       Position dest_pos = Dungeon.getInstance().getRandomEmptyPosition();
       Cell dest_cell = Dungeon.getInstance().getCell( dest_pos );       
       
       if (dest_cell.canEnter(this))
       {
           // Update cell references
           this.position = dest_pos;
           dest_cell.setEntity(this);
           current_cell.setEntity(null);
           
           //JavaTools.printlnTime("DEBUG: " + description + " moved to location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           this.playSound(Constants.SOUND_TELEPORT);
           this.addMessage("Teleported!");
           return true;
       }
       else
       {
           JavaTools.printlnTime("DEBUG: " + description + " was blocked teleporting to X=" + dest_pos.x + " Y=" + dest_pos.y + " Z=" + dest_pos.z);
           this.addMessage("Teleport failed!");
           this.playSound(Constants.SOUND_BLOCKED);
           return false;
       }    
   }
   
   
   /**
    * @param direction
    * @return true on success, false if failed
    */
   protected boolean attemptAttack(byte direction) 
   {   
       if (direction == Constants.DIRECTION_NONE)  // Can't attack self
       {
           return false;
       }
       
       Cell current_cell = Dungeon.getInstance().getCell(this.position); 
       Position destination = getTargetPosition(current_cell, direction);
       Cell dest_cell = Dungeon.getInstance().getCell(destination);  
       
       if (dest_cell == null)
       {
           JavaTools.printlnTime("DEBUG: " + description + " attempted to attack out of map?  Last known location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           return false;
       }
       
       Entity target = dest_cell.getEntity();
       
       if (target != null) // A real target
       {
           target.attackedBy(this);           
           JavaTools.printlnTime("DEBUG: " + description + " attacked " + target.getDescription() );
           return true;
       }
       else // Empty cell
       {
           this.addMessage("You attack the darkness!");
           this.playSound(Constants.SOUND_MISS);
           return false;
       }    
   }
   
   private float getAttackRoll()
   {
       float max_damage = baseDamage;
       
       if (item_left != null)
       {
           max_damage += item_left.getMaxDamage();
       }
       
       if (item_right!= null)
       {
           max_damage += item_right.getMaxDamage();
       }
       
       return max_damage * JavaTools.generator.nextFloat();     
   }
   
   private float getProtection()
   {
       float max_protect = 0;
       
       if (item_left != null)
       {
           max_protect += item_left.getMaxProtection();
       }
       
       if (item_right!= null)
       {
           max_protect += item_right.getMaxProtection();
       }
       
       return max_protect * JavaTools.generator.nextFloat();     
   }
   
   private void attackedBy(Entity attacker)
   {       
       float damage = attacker.getAttackRoll();
       float protection = this.getProtection();
       
       // TODO, tweak this.  Really, the combat system boils down to this single line
       damage -= protection;
       if (damage < 0) damage = 0f;  
       
       int idamage = (int)Math.floor(damage);       
       
       if (idamage > 0)
       {
           this.health -= idamage;
           this.playSound(Constants.SOUND_ATTACKED);
           attacker.playSound(Constants.SOUND_ATTACK);
           this.addMessage(attacker.description + " hits you for " + idamage + " damage!");
           attacker.addMessage("You hit " + this.description + " for " + idamage + " damage!");
       }
       else
       {
           this.playSound(Constants.SOUND_MISS);
           attacker.playSound(Constants.SOUND_MISS);
           this.addMessage("You block the " + attacker.description + "!");
           attacker.addMessage( this.description + " blocks!");           
       }       

       checkHealth(attacker);
   }  

   protected void checkHealth(Entity attacker)
   {       
       if (health <= 0 )
       {
           health = 0;
           gameOver(attacker);           
       }    
   }
   
   public boolean isDead()
   {       
      return (health <= 0);
   }

   public void gameOver(Entity attacker)
   {
       if (attacker != null)
       {
           String obituary = description + " was killed by " + attacker.description + "!";
           this.addMessage(obituary);
           attacker.addMessage(obituary);
           JavaTools.printlnTime(obituary);
       }
       
       // TODO:  This is a bit hacky.  Remove monsters instantly, but keep clients in game to keep sending updates.       
       if (myType == entityTypes.CLIENT)
       {
           // Remove from map only.   This clears the cell for the drops below.
           Dungeon.getInstance().getCell(position).setEntity(null);
       }
       else
       {
           removeMe();
       }
       
       // Drop currently carried items       
       Cell left =  Dungeon.getInstance().getCell( Dungeon.getInstance().getClosestEmptyCell(this.position, Constants.EMPTY_CELL_SEARCH_DEPTH) );       
       if (left != null) 
       {
           left.placeItem(item_left);
           item_left = null;
       }
       else
       {
           JavaTools.printlnTime("DEBUG: " + description + " Can't drop left item!! ");
       }
       
       Cell right =  Dungeon.getInstance().getCell( Dungeon.getInstance().getClosestEmptyCell(this.position, Constants.EMPTY_CELL_SEARCH_DEPTH) );       

       if (right != null) 
       {
           right.placeItem(item_right);  
           item_right = null;
       }
       else
       {
           JavaTools.printlnTime("DEBUG: " + description + " Can't drop right item!! ");
       }
   }

   private Position getTargetPosition(Cell current_cell, byte direction)
   {
       int dx=0;
       int dy=0;
       int dz=0;
       
       switch (direction)
       {
          case Constants.DIRECTION_NONE:          
              break;
              
          case Constants.DIRECTION_NORTH:
              dy=-1;
              break;
              
          case Constants.DIRECTION_NE:
              dy=-1;
              dx=+1;
              break;
              
          case Constants.DIRECTION_EAST:
              dx=+1;
              break;
              
          case Constants.DIRECTION_SE:
              dy=+1;
              dx=+1;
              break;
              
          case Constants.DIRECTION_SOUTH:
              dy=+1;
              break;
                
          case Constants.DIRECTION_SW:
              dy=+1;
              dx=-1;
              break;
              
          case Constants.DIRECTION_WEST:
              dx=-1;
              break;              
              
          case Constants.DIRECTION_NW:   
              dx=-1;
              dy=-1;
              break;
          
          case Constants.DIRECTION_UP:
              if (current_cell.getTrueCharCode() == Constants.CHAR_STAIRS_UP)
              {
                  dz = -1;
              }
              break;
              
          case Constants.DIRECTION_DOWN:
             if (current_cell.getTrueCharCode() == Constants.CHAR_STAIRS_DOWN)
             {
                 dz = 1;
             }
             break;
             
          default:
             JavaTools.printlnTime("DEBUG: Unknown move direction code " + direction + " from " + description);
             return this.position; // No Change            
       }
       
       return new Position(this.position.x+dx, this.position.y+dy, this.position.z+dz);
    }

    protected boolean attemptPickup(int hand)
    {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);
        
       if (hand == Constants.HAND_LEFT)
       {
           if (item_left != null)  // Already carrying something
           {
               return false;
           }
           
           item_left = current_cell.takeItem();    
           if (item_left != null)
           {
              this.addMessage("Picked up the " + item_left.getDescription() + ".");
              return true;
           }
       }
       
       if (hand == Constants.HAND_RIGHT)
       {
           if (item_right != null)  // Already carrying something
           {
               return false;
           }
           
           item_right = current_cell.takeItem();    
           if (item_right != null)
           {
              this.addMessage("Picked up the " + item_right.getDescription() + ".");
              return true;
           }
       }       
       
       return false;
   }
       
   // Drop an item at the current location.  True on success
   protected boolean attemptDrop(int hand)
   {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);    
       
       if (hand == Constants.HAND_LEFT)
       {
           if (item_left == null)  // Not carrying anything
           {
               return false;
           }
           
           boolean success = current_cell.placeItem(item_left);  
           
           if (success)
           {
               this.addMessage("Dropped the " + item_left.getDescription() + ".");
               item_left.dropped();
               item_left = null;   // No longer carrying the item              
               return true;
           }
       }
       
       if (hand == Constants.HAND_RIGHT)
       {
           if (item_right == null)  // Not carrying anything
           {
               return false;
           }
           
           boolean success = current_cell.placeItem(item_right);  
           
           if (success)
           {
               this.addMessage("Dropped the " + item_right.getDescription()+ ".");
               item_right.dropped();
               item_right = null;   // No longer carrying the item               
               return true;
           }
       }
      
      return false;
   }
   
   public boolean attemptUse(byte parameter1)
   {
       switch (parameter1)
       {
           case Constants.HAND_NONE:
               return attemptUseCell();
               
           case Constants.HAND_LEFT:
               return attemptUseItem(item_left);
               
           case Constants.HAND_RIGHT:
               return attemptUseItem(item_right);
           
           default:
               return false;
       }       
   }
   
   // Attempt to use the item in the cell under this entity.  True on success, false on failure. 
   private boolean attemptUseCell()
   {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);
       
       // First - use any Item in this cell
       if (current_cell.getItem() != null)
       {
           return current_cell.getItem().useItem(this);
       }
       
       // Empty cell - take action based on cell type 
       
       byte code = current_cell.getTrueCharCode();
       
       switch (code)
       {
            case Constants.CHAR_STAIRS_UP:
                return attemptMove(Constants.DIRECTION_UP);
                
            case Constants.CHAR_STAIRS_DOWN:
                return attemptMove(Constants.DIRECTION_DOWN);       

            case Constants.CHAR_PORTAL:
                return attemptTeleport();       

            // Default do nothing
       }
       
       return false;
   }
   
   // Attempt to inspect the item in the cell under this entity.  True on success, false on failure. 
   public boolean attemptInspect(boolean report_empty)
   {
       Cell current_cell = Dungeon.getInstance().getCell(this.position);
       
       // First - use any Item in this cell
       if (current_cell.getItem() != null)
       {
           addMessage("You see a " + current_cell.getItem().getDescription() + "." );
           return true;
       }
       
       // Empty cell        
       
       byte code = current_cell.getTrueCharCode();
       
       switch (code)   // Refer to Cell.canEnter() for allowable codes 
       {
           case Constants.CHAR_EMPTY:
               if (report_empty)
               {
                   addMessage("You see nothing here.");       
               }
               return false;
           
           case Constants.CHAR_STAIRS_DOWN:
               addMessage("You see stairs going down.");
               return true; 
           
            case Constants.CHAR_STAIRS_UP:
                addMessage("You see stairs going up.");
                return true;

            case Constants.CHAR_PORTAL:
                addMessage("You see a glowing portal.");
                return true;
                
            case Constants.CHAR_DOOR_OPEN:
                if (report_empty)
                {
                    addMessage("You see an open door.");       
                }
                return true;
                
            case Constants.CHAR_DOOR_CLOSED:
                addMessage("You see a closed door.");       
                return true;
                
            case Constants.CHAR_ITEM_CHEST:              
                addMessage("You see a chest.");       
                return true;
                
            case Constants.CHAR_SECRET_DOOR:              
                if (report_empty)
                {
                    addMessage("You have found a secret door!");       
                }
                return true;
                
            default:  // Should never see this
                addMessage("You see nothing interesting."); 
                return false;
       }
   }
   
   // Attempt to use a carried item 
   private boolean attemptUseItem(Item item)
   {
       if (item == null)
       {
           return false;
       }
       
       return item.useItem(this);
   }
   
   abstract public void action();                // Called on every game loop prior to updates
   abstract public void update();                // Called on every game loop
   abstract protected void updateNow();          // Called from update().  Other entities can force an update through needsUpdate()
   abstract public void addMessage(String msg);  // Add a message to this Entity's message queue
   abstract public void playSound(byte id);      // Play a sound with this Entity

   /** Return position */
   public Position getPosition()
   {
       return position;
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
       
       if (this.getPosition().z != target.getPosition().z)  // Don't see targets on other levels  
       {
           return Double.MAX_VALUE;
       }
       
       return Math.sqrt( Math.pow((this.getPosition().x - target.getPosition().x), 2) + 
                         Math.pow((this.getPosition().y - target.getPosition().y), 2) ); 
   }
   
   protected byte getDirectionTo(Entity target)
   {
       // Ignore targets that have been removed
       if (target == null) return Constants.DIRECTION_NONE;
       
       return getDirectionTo(target.getPosition());
   }
   
   protected byte getDirectionTo(Position target_pos)
   {
       // Ignore null positions
       if (target_pos == null) return Constants.DIRECTION_NONE;
       
       // Ignore targets not on same level
       if (this.getPosition().z != target_pos.z) return Constants.DIRECTION_NONE;
               
       if ((this.getPosition().x == target_pos.x) && (this.getPosition().y == target_pos.y)) return Constants.DIRECTION_NONE;
       if ((this.getPosition().x >  target_pos.x) && (this.getPosition().y == target_pos.y)) return Constants.DIRECTION_WEST;
       if ((this.getPosition().x <  target_pos.x) && (this.getPosition().y == target_pos.y)) return Constants.DIRECTION_EAST;
       if ((this.getPosition().x == target_pos.x) && (this.getPosition().y >  target_pos.y)) return Constants.DIRECTION_NORTH;
       if ((this.getPosition().x == target_pos.x) && (this.getPosition().y <  target_pos.y)) return Constants.DIRECTION_SOUTH;
       if ((this.getPosition().x >  target_pos.x) && (this.getPosition().y >  target_pos.y)) return Constants.DIRECTION_NW;
       if ((this.getPosition().x <  target_pos.x) && (this.getPosition().y <  target_pos.y)) return Constants.DIRECTION_SE;
       if ((this.getPosition().x >  target_pos.x) && (this.getPosition().y <  target_pos.y)) return Constants.DIRECTION_SW;
       if ((this.getPosition().x <  target_pos.x) && (this.getPosition().y >  target_pos.y)) return Constants.DIRECTION_NE;
       
       // Should never reach here, but just in case
       return Constants.DIRECTION_NONE;
   }

   public void removeMe() 
   {   
        // Clear cell this entity is removed from
        Dungeon.getInstance().getCell(position).setEntity(null);
        removeMeFlag = true;
   }
    
   public boolean getRemoved() 
   {   
        return removeMeFlag;
   }
   
   public void needsUpdate() 
   {           
        updateMeFlag = true;
   }

	public byte getCharCode() 
	{
		if (isInvisible)
		{
			return Constants.CHAR_EMPTY;
		}
		else
		{
			return charCode;
		}
	}

	public entityTypes getType() 
	{		
		return myType;
	}
	
	protected void finishMove(boolean moved)
    {
	    lastAction = Instant.now();   
	    
        // If we moved, tell other entities in area to update        
        if (moved)
        {                     
            List<Entity> onscreen = Dungeon.getInstance().getEntitiesOnScreenCentered(this.position);
            
            // Don't update self
            onscreen.remove(this);
            
            for (Entity e : onscreen)
            {
                e.needsUpdate();
            }            
        }        
    }

    public void addHealth(int h)
    {
        health += h;
        
        if (health > maxHealth)
        {
           health=maxHealth;
        }
    }

    // Forcibly drop (remove) items from inventory
    public void forceDrop(Item magicitem)
    {
        if (magicitem.equals(item_left))
        {
            item_left = null;
        }
        
        if (magicitem.equals(item_right))
        {
            item_right = null;
        }
    }   
}