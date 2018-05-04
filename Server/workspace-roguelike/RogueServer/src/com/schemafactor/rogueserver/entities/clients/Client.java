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
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.NonBlockingFixedSizeQueue;
import com.schemafactor.rogueserver.universe.Dungeon;

public abstract class Client extends Entity
{        
   private static final long serialVersionUID = 1L;
   
   protected InetAddress userIP;                           // User IP Address
   protected boolean announceReceived = false;
   protected Instant lastUpdateReceived = Instant.now();   // For timeouts
   protected Instant lastUpdateSent = Instant.now();       // For periodic updates
   protected int lastActionCounter = -1;                   // Invalid
   
   // Mini state machine for escape sequences
   ArrayList<Integer> escapeSequence = new ArrayList<Integer>();
   
   // Flag to say help is being shown
   boolean showingHelp = false;
   
   // Queue of messages
   NonBlockingFixedSizeQueue<String> messageQueue = new NonBlockingFixedSizeQueue<String>(Constants.MESSAGE_QUEUE_MAX);
   
   // Current Sound effect counter
   byte soundCounter = 0;
   
   // Current Sound Effect ID
   byte soundFXID = Constants.SOUND_NONE;
   
   public Client(String description, Position startposition, entityTypes type, byte charCode)
   {
       super(description, startposition, type, charCode, 1f);
       
       this.addMessage("Welcome to the Rogue Test Server");
       this.addMessage("Server version: " + Double.toString(Constants.VERSION) );       
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
      
       // Yes/No at Game over
       
       if (isDead())
       {
           switch (inputchar)
           {
               case 'y':    // Restart Game 
               case 'Y':
                       respawn();
                       updateNow();
                       break; 

               case 'n':    // End Game 
               case 'N':
                   removeMe();
           }
           
           return;
       }
       
       // Normal keystrokes       
       
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
               showingHelp = !showingHelp;
               updateNow();
               break;
               
           case 'j':
               handleAction(Constants.ACTION_PICKUP, Constants.HAND_LEFT);
               break;
               
           case 'k':
               handleAction(Constants.ACTION_PICKUP, Constants.HAND_RIGHT);
               break;
               
           case '*':
               handleAction(Constants.ACTION_USE, Constants.HAND_NONE);
               break;
          
           case ',':
               handleAction(Constants.ACTION_USE, Constants.HAND_LEFT);
               break;
          
           case '.':
               handleAction(Constants.ACTION_USE, Constants.HAND_RIGHT);
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
               handleAction(Constants.ACTION_DROP, Constants.HAND_LEFT);
               break;
               
           case 'K':
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
               JavaTools.printlnTime("DEBUG: Invalid command " + inputchar + " from " + description);
               return;
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
           showingHelp = true;
           complete = true;
       }
       
       if (Arrays.equals(esc, EscapeSequences.ESCAPE_F2))
       {
           showingHelp = false;
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
              JavaTools.printlnTime("DEBUG: heartbeat received from " + description);
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
             

              
              /*

          public static final byte ACTION_DIG        = 3;
          public static final byte ACTION_EXAMINE    = 5;
          public static final byte ACTION_OPEN       = 6;
          public static final byte ACTION_CLOSE      = 7;
          public static final byte ACTION_CAST       = 8;
          */
              
          default:
             JavaTools.printlnTime("Unknown action code " + action + " from " + description);
             break;  
       }    
       
       // Regardless of the outcome of the action, update the client
       updateNow();
       
       // Update other entities in the area
       finishMove(moved);
   }
  
   @Override
   public void update()
   { 
	   Duration elapsed = Duration.between(lastUpdateSent, Instant.now());
       
       if (elapsed.getSeconds() >= Constants.UPDATE_TIME)  // Send an update every 1 second
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
		Duration elapsed = Duration.between(lastUpdateReceived, Instant.now());
	    
	    if (elapsed.getSeconds() > Constants.NETWORK_TIMEOUT)
	    {	        
	        if (!removeMeFlag)
	        {
	            JavaTools.printlnTime( "Player Timed Out: " + description );
	            gameOver(null);
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
   
   // Used by C64 and WebSocket clients
   protected byte[] getUpdateByteArray()
   { 
       // Send data packet to the client              
       byte[] buffer = new byte[528];       
       buffer[0] = Constants.PACKET_UPDATE;
       
       int offset = 1;
              
       if (showingHelp)
       {
           String screen = "";
          
           screen += "Help (F1 to Exit)    ";
           screen += "                     ";
           screen += "QWE                  ";
           screen += "ASD = Move           ";
           screen += "ZXC                  ";
           screen += "                     ";
           screen += "SHIFT+Move = Attack  ";
           screen += "                     ";
           screen += "J = Pick up (Left)   ";
           screen += "K = Pick up (Right)  ";
           screen += "SHIFT+J,K = Drop     ";
           screen += "                     ";
           screen += "* = Use item (Seen)  ";
           screen += ", = Use item (Left)  ";
           screen += ". = Use item (Right) ";
           screen += "                     ";
           screen += "F1 = Help            ";
                      
           if (screen.length() != Constants.SCREEN_SIZE)
           {
               JavaTools.printlnTime("EXCEPTION: Mismatch in Help string size!!");
               throw new RuntimeException("Mismatch in Help string size!!");
           }
           
           System.arraycopy( screen.toUpperCase().getBytes(), 0, buffer, offset, Constants.SCREEN_SIZE );
           
       }
       else if (isDead())
       {
           String screen = "";
           String lava = "   " + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + (char)Constants.CHAR_LAVA + "    ";
           
           screen += "                     ";
           screen += "                     ";
           screen += "                     ";
           screen += "                     ";
           screen += lava;
           screen += "   " + (char)Constants.CHAR_LAVA + " GAME  OVER " + (char)Constants.CHAR_LAVA + "    ";           
           screen += lava;
           screen += "                     ";
           screen += "                     ";
           screen += "   You have died!    ";
           screen += "                     ";
           screen += "  Play Again  Y/N?   ";
           screen += "                     ";
           screen += "                     ";           
           screen += "                     ";
           screen += "                     ";
           screen += "                     ";
                      
           if (screen.length() != Constants.SCREEN_SIZE)
           {
               JavaTools.printlnTime("EXCEPTION: Mismatch in End Game string size!!");
               throw new RuntimeException("Mismatch in  End Game string size!!");
           }
           
           System.arraycopy( screen.toUpperCase().getBytes(), 0, buffer, offset, Constants.SCREEN_SIZE );
       }
       else // All good
       {      
           // Get the screen that is visible to this player
           System.arraycopy( Dungeon.getInstance().getScreenCentered(position), 0, buffer, offset, Constants.SCREEN_SIZE );          
       }
       
       offset += Constants.SCREEN_SIZE;
       
       // On screen messages
       for (int i=3; i >= 0; i--)
       {
           byte[] message = getMessage(i).toUpperCase().getBytes();
           System.arraycopy( message, 0, buffer, offset, Math.min(message.length, Constants.MESSAGE_LENGTH) );
           offset += Constants.MESSAGE_LENGTH;
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
       String sh = String.format("%1$3d", ih);  // To String with padding
       byte[] bh = sh.getBytes();
       System.arraycopy( bh, 0, buffer, offset, 3 );
       offset += 3;
       
       // Sound Effects
       buffer[offset++] = soundCounter;
       buffer[offset++] = soundFXID;
       
       // Number of players
       buffer[offset++] = (byte) Dungeon.getInstance().getNumPlayers();
       
       // End of packet marker
       buffer[offset++] = (byte)255;
       
       return buffer;
    }
}