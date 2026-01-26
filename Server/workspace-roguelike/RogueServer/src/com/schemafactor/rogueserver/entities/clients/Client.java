package com.schemafactor.rogueserver.entities.clients;

import java.net.InetAddress;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.EscapeSequences;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.NonBlockingFixedSizeQueue;

public abstract class Client extends Entity
{        
   private static final long serialVersionUID = 1L;
   
   // Various client parameters
   protected InetAddress userIP;                           // User IP Address
   protected boolean announceReceived = false;             // Have received first ("announce") packet from client
   protected Instant lastUpdateReceived = Instant.now();   // For timeouts
   protected Instant lastUpdateSent = Instant.now();       // For periodic updates
   protected int lastActionCounter = -1;                   // Invalid
   
   // Mini state machine for escape sequences
   ArrayList<Integer> escapeSequence = new ArrayList<Integer>();
   
   // Queue of messages
   NonBlockingFixedSizeQueue<String> messageQueue = new NonBlockingFixedSizeQueue<String>(Constants.MESSAGE_QUEUE_MAX);
   
   // Current Sound effect counter
   byte soundCounter = 0;
   
   // Current Sound Effect ID
   byte soundFXID = Constants.SOUND_NONE;

   // Idle Time (since last command)
   int idleTime = 0;
   
   // Demo mode?
   static boolean demoMode = false;   
   
   public Client(String description, Position startposition, entityTypes type, byte charCode)
   {
       super(description, startposition, type, charCode, 1f, 100f);
       
       this.addMessage("The Dungeon of the Rogue Daemon");
       this.addMessage("Server version: " + Double.toString(Constants.VERSION) + "   (H for help)");       
       
       myState = entityStates.CHOOSING_AVATAR;
   }
   
   /** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
   }
   
   /** Update me with command from client */
   public void handleKeystroke(int inputchar)
   {   
       // Special handling for escape sequences (ignore timing)
       if (escapeSequence.size() > 0)
       {
           boolean handled = handleEscapeSequence(inputchar);
           if (handled)
           {
              return;                      
           }
       }
       
       // Filter out commands coming in too fast.
       Duration elapsed = Duration.between(lastUpdateReceived, Instant.now());     
               
       if (elapsed.toMillis() <= Constants.CLIENT_ACTION_TIME_LIMIT)   // Too fast!
       {    
           //JavaTools.printlnTime("DEBUG: TOO Fast!   Elapsed millis = " + elapsed.toMillis());
           return;   // Not time to act yet   TODO, Why does this jump to 230 ms?
       }
       else
       {     
          // JavaTools.printlnTime("DEBUG: Elapsed millis = " + elapsed.toMillis());
       }
       
       lastUpdateReceived = Instant.now();
       
       // Respond to commands based on client state
       
       switch (myState)
       {
       		case NEW:
       			break;
			
			case CHOOSING_AVATAR:
				
				switch (inputchar)
		        {
			       case '1':
			    	   charCode = Constants.CHAR_PLAYER_GENERIC;
			    	   myState = entityStates.PLAYING;
			           break;
			       
			       case '2':
			    	   charCode = Constants.CHAR_PLAYER_MAGE;
			    	   myState = entityStates.PLAYING;
			           break;
			           
			       case '3':
			    	   charCode = Constants.CHAR_PLAYER_FIGHTER;
			    	   myState = entityStates.PLAYING;
			           break;
			           
			       case '4':
			    	   charCode = Constants.CHAR_PLAYER_FIGHTER2;
			    	   myState = entityStates.PLAYING;
			           break;
			        
			       default:
			    	   return;  // Ignore all other input until an avatar is chosen
		           } 
				break;
				
			case PLAYING:
				
				switch (inputchar)
		        {       
		           case 'q':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_NW);
		               break;
		           
		           case 'w':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_NORTH);
		               break;
		               
		           case 'e':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_NE);
		               break;
		               
		           case 'a':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_WEST);
		               break;
		               
		           case 's':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_SOUTH);
		               break;
		               
		           case 'd':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_EAST);
		               break;
		               
		           case 'z':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_SW);
		               break;
		               
		           case 'x':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_SOUTH);
		               break;
		               
		           case 'c':
		               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_SE);
		               break;               
		               
		           case 'h':
		        	   myState = entityStates.HELP;
		               needsUpdate();
		               break;
		               
		           case 'j':
		               handleAction(Constants.ACTION_PICKUP, Constants.HAND_LEFT);
		               break;
		               
		           case 'k':
		               handleAction(Constants.ACTION_PICKUP, Constants.HAND_RIGHT);
		               break;
		               
		           case '*':  // This is sent by joystick when fire-button pressed while centered.  Not currently used, was too fast to be useful.
		        	   break;
		        	   
		           case 'u':
		           case 'U':
		               handleAction(Constants.ACTION_USE, Constants.HAND_NONE);
		               break;
		          
		           case ',':
		           case 'l':
		               handleAction(Constants.ACTION_USE, Constants.HAND_LEFT);
		               break;
		          
		           case '.':
		           case 'r':
		               handleAction(Constants.ACTION_USE, Constants.HAND_RIGHT);
		               break;
		               
		           case 'i':
		               handleAction(Constants.ACTION_INSPECT, Constants.HAND_NONE);
		               break;
		               
		           case 'Q':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_NW);
		               break;
		           
		           case 'W':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_NORTH);
		               break;
		               
		           case 'E':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_NE);
		               break;
		               
		           case 'A':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_WEST);
		               break;
		               
		           case 'S':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_SOUTH);
		               break;
		               
		           case 'D':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_EAST);
		               break;
		               
		           case 'Z':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_SW);
		               break;
		               
		           case 'X':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_SOUTH);
		               break;
		               
		           case 'C':
		               handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_SE);
		               break;
		               
		           case 'J':
		           case 'n':
		           case 'N':
		               handleAction(Constants.ACTION_DROP, Constants.HAND_LEFT);
		               break;
		               
		           case 'K':
		           case 'm':
		           case 'M':               
		               handleAction(Constants.ACTION_DROP, Constants.HAND_RIGHT);
		               break; 
		               
		           case '!': // DEBUG special way to commit suicide to debug end of game
		               health = 0;
		               checkHealth(this);
		               break;

		           // Special cases for Cursor and Function Keys
		           case EscapeSequences.ESC:
		               escapeSequence.clear();
		               escapeSequence.add(inputchar);
		               return;
		           
		           default:
		               //JavaTools.printlnTime("DEBUG: Invalid command " + inputchar + " from " + description);
		               return;
			       }
				
				break;
				
			case DEAD:
				
				switch (inputchar)
	            {
	               case 'y':    // Restart Game 
	               case 'Y':
                       respawn();
                       break; 

	               case 'n':    // End Game 
	               case 'N':
	                   removeMe();
	            }
				break;
				
			case HELP:
				
				switch (inputchar)
	            {
	               case 'h': 
	               case 'H':
                       myState = entityStates.PLAYING;
                       break;
	            }				
				break;			
			
			default:
				break;
       
       }   
   }

   // Special handling for escape sequences
   private boolean handleEscapeSequence(int inputchar)
   {  
       boolean complete = false;
       
       escapeSequence.add(inputchar);
       
       int[] esc = escapeSequence.stream().mapToInt(Integer::intValue).toArray(); 
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_UP))
       {
           handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_NORTH);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_DOWN))
       {
           handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_SOUTH);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_RIGHT))
       {
           handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_EAST);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_LEFT))
       {
           handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_WEST);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_SHIFT_UP))
       {
           handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_NORTH);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_SHIFT_DOWN))
       {
           handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_SOUTH);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_SHIFT_RIGHT))
       {
           handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_EAST);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_SHIFT_LEFT))
       {
           handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_WEST);
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_F1))
       {
    	   myState = entityStates.HELP;
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_F2))
       {
           myState = entityStates.PLAYING;
           complete = true;
       }
       
       if (complete)
       {
           escapeSequence.clear();
           return true;           
       }
       else
       {
           // Hack to break out of invalid escape sequences.  TODO, improve this.  Tri tree?           
           if (esc.length >= EscapeSequences.ESCAPE_F2.length)
           {
               escapeSequence.clear();
           }             
           return false;
       }
   }

   protected void handleAction(byte action, byte parameter1) 
   {
       boolean moved = false;
       
       switch (action)
       {
          case Constants.ACTION_HEARTBEAT:
              //JavaTools.printlnTime("DEBUG: heartbeat received from " + description);
              break;
              
          case Constants.ACTION_USE:
              moved = attemptUse(parameter1);
              break;
              
          case Constants.ACTION_MOVE:
              moved = attemptMove(parameter1);
              break;
              
          case Constants.ACTION_ATTACK:
              moved = attemptAttack(parameter1);
              break;
              
          case Constants.ACTION_PICKUP:
              moved = attemptPickup(parameter1);
              break;
              
         case Constants.ACTION_DROP:
             moved = attemptDrop(parameter1);
             break;
             
         case Constants.ACTION_INSPECT:
             moved = attemptInspect(true); 
             break;
              
              /*

          public static final byte ACTION_DIG        = 3;
          public static final byte ACTION_OPEN       = 6;
          public static final byte ACTION_CLOSE      = 7;
          public static final byte ACTION_CAST       = 8;
          */
              
          default:
             // JavaTools.printlnTime("Unknown action code " + action + " from " + description);
             break;  
       }    
       
       // Regardless of the outcome of the action, update the client immediately
       updateNow();
       
       // Update other entities in the area
       finishMove(moved);
   }
   
   @Override
   public void action()
   { 
	   ;    // Client updates are provided through user input asynchronously
   }
  
   @Override
   public void update()
   { 
	   Duration elapsed = Duration.between(lastUpdateSent, Instant.now());
       
       if (elapsed.toMillis() >= Constants.UPDATE_TIME)  // Force an update every 1.000 second
       {
    	   needsUpdate();
       }
       
       // Update if required.
       if (updateMeFlag)
       {
     	   updateNow();
       }
       
       // Increment and Timeout.  This is reset in receiveUpdate() above.     
       checkTimeout();
       
       // Check health
       checkHealth(null);
   }
      
   // Increment and check the timeout
   private void checkTimeout()
   {
       if (demoMode) return;   // Disable Timeout in demo mode
       
		Duration elapsed = Duration.between(lastUpdateReceived, Instant.now());
	    idleTime = elapsed.getSeconds();
		
		if (idleTime > Constants.NETWORK_WARNING)
	    {	        
	       addMessage("Warning: Timeout in 60 seconds");             
       }   
	    
	    if (idleTime > Constants.NETWORK_TIMEOUT)
	    {	        
	        if (!removeMeFlag)
	        {
	        	addMessage("You Have Timed Out");
	            JavaTools.printlnTime( "Player Timed Out: " + description );
	            gameOver(null);
	            myState = entityStates.DISCONNECTED;
	            removeMe();	            
	        }             
       }      
   }
   
   protected String getMessage(int index)
   {
       String msg = messageQueue.elementAt(messageQueue.size() - index - 1);
       if (msg == null)
       {
           return "";
       }
       else
       {
           return msg; 
       }
   }       
   
   @Override
   public void addMessage(String msg)
   {
       messageQueue.add(msg);
   }
   
   @Override
   public void playSound(byte id)
   {
       soundCounter++;
       soundFXID = id;
   }  
   
   public static void setDemoMode()
   {
       demoMode = true;
   }

   public int getIdleTime()
   {
       return idleTime;
   }
   
   // Used by C64 and WebSocket clients
   protected byte[] getUpdateByteArray()
   {
       return getUpdateByteArray(false, Constants.PACKET_UPDATE_SIZE);   
   }

   // Used by U64 clients
   protected byte[] getUpdateByteArray(boolean skipmessages, int size)
   { 
       // Send data packet to the client              
       byte[] buffer = new byte[size];       
       buffer[0] = Constants.PACKET_UPDATE;
       
       int offset = 1;
       
       switch (myState)
       {			
			case CHOOSING_AVATAR:
				
				 byte[] screen = new byte[] {
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ','C','H','O','O','S','E',' ','A','V','A','T','A','R',':',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ','1',' ',Constants.CHAR_PLAYER_GENERIC,' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ','2',' ',Constants.CHAR_PLAYER_MAGE,' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ','3',' ',Constants.CHAR_PLAYER_FIGHTER,' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ','4',' ',Constants.CHAR_PLAYER_FIGHTER2,' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				           ' ', ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
				    	   };
				 
				 System.arraycopy( screen, 0, buffer, offset, Constants.SCREEN_SIZE);
				 break;
				 
			case HELP:
				
				String help_screen = "";
		          
	            help_screen += "Help (H to Exit)     ";
	            help_screen += "                     ";
	            help_screen += "QWE                  ";
	            help_screen += "ASD = Move           ";
	            help_screen += "ZXC                  ";
	            help_screen += "                     ";
	            help_screen += "SHIFT+Move = Attack  ";
	            help_screen += "                     ";
	            help_screen += "J = Pick up (Left)   ";
	            help_screen += "K = Pick up (Right)  ";
	            help_screen += "N/M = Drop Left/Right";
	            help_screen += "I = Inspect item     ";
	            help_screen += "U = Use item (Seen)  ";
	            help_screen += "L = Use item (Left)  ";
	            help_screen += "R = Use item (Right) ";
	            help_screen += "                     ";
	            help_screen += "H = Help             ";
	                      
	            if (help_screen.length() != Constants.SCREEN_SIZE)
	            {
	               JavaTools.printlnTime("EXCEPTION: Mismatch in Help string size!!");
	               throw new RuntimeException("Mismatch in Help string size!!");
	            }
	           
	            System.arraycopy( help_screen.toUpperCase().getBytes(), 0, buffer, offset, Constants.SCREEN_SIZE );
				break;
				 
			case DEAD:
				String death_screen = "";
		        String lava = "   " + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + "    ";
		           
		        death_screen += "                     ";
		        death_screen += "                     ";
		        death_screen += "                     ";
		        death_screen += "                     ";
		        death_screen += lava;
		        death_screen += "   " + (char)Constants.CHAR_LAVA + " GAME  OVER " + (char)Constants.CHAR_LAVA + "    ";           
		        death_screen += lava;
		        death_screen += "                     ";
		        death_screen += "                     ";
		        death_screen += "   You have died!    ";
		        death_screen += "                     ";
		        death_screen += "  Play Again  Y/N?   ";
		        death_screen += "                     ";
		        death_screen += "                     ";           
		        death_screen += "                     ";
		        death_screen += "                     ";
		        death_screen += "                     ";
		                      
		        if (death_screen.length() != Constants.SCREEN_SIZE)
		        {
		            JavaTools.printlnTime("EXCEPTION: Mismatch in End Game string size!!");
		            throw new RuntimeException("Mismatch in  End Game string size!!");
		        }
		           
		        System.arraycopy( death_screen.toUpperCase().getBytes(), 0, buffer, offset, Constants.SCREEN_SIZE );
				break;
				
			case PLAYING:
				// Get the screen that is visible to this player
		        System.arraycopy( Dungeon.getInstance().getScreenCentered(position), 0, buffer, offset, Constants.SCREEN_SIZE );          
				break;
				
			default:
				break;
		       
       }
       
       offset += Constants.SCREEN_SIZE;
       
       if (!skipmessages)
       {       
           // TODO, refactor this into a separate function
           // On screen messages
           for (int i=3; i >= 0; i--)
           {
               byte[] message = getMessage(i).toUpperCase().getBytes();
               System.arraycopy( message, 0, buffer, offset, Math.min(message.length, Constants.MESSAGE_LENGTH) );
               offset += Constants.MESSAGE_LENGTH;
           }
       }
       
       // Item underneath current position
       buffer[offset++] = Dungeon.getInstance().getCell(position).getItemCharCode();
       
       // Item currently held (left)
       if (item_left != null)
       {
           buffer[offset++] = item_left.getCharCode();
       }
       else
       {
           buffer[offset++] = Constants.CHAR_EMPTY;
       }
       
       // Item currently held (right)
       if (item_right != null)
       {
           buffer[offset++] = item_right.getCharCode();
       }
       else
       {
           buffer[offset++] = Constants.CHAR_EMPTY;
       }
       
       // Health Value
       int ih = (int)health;  // Round
       String sh = String.format("%1$-3d", ih);  // To String with padding
       byte[] bh = sh.getBytes();
       System.arraycopy( bh, 0, buffer, offset, 3 );
       offset += 3;
       
       // Sound Effects
       buffer[offset++] = soundCounter;
       buffer[offset++] = soundFXID;
       
       // Number of players
       buffer[offset++] = (byte) Dungeon.getInstance().getNumPlayers();
       
       // XP
       String sxp = String.format("%1$-6d", XP);  // To String with padding
       byte[] bxp = sxp.getBytes();
       System.arraycopy( bxp, 0, buffer, offset, 6 );
       offset += 6;
       
       // End of packet marker
       buffer[offset++] = (byte)255;
       
       return buffer;
    }
}
