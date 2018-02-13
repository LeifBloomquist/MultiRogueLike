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

public abstract class HumanPlayer extends Entity
{        
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
   
   public HumanPlayer(String description, Position startposition, entityTypes type, byte charCode)
   {
       super(description, startposition, type, charCode, 1f);
       
       this.addMessage("Welcome to the Rogue Test Server");
       this.addMessage("Server version: " + Double.toString(Constants.VERSION) );       
   }
   
  private void reInitPlayer()
  {
      health = 100;
      this.position = Dungeon.getInstance().getClosestEmptyCell(start_position, Constants.EMPTY_CELL_SEARCH_DEPTH);
      
      this.addMessage("Restarted...");
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
       JavaTools.printlnTime("DEBUG: Elapsed millis = " + elapsed.toMillis());
       
       lastUpdateReceived = Instant.now();
       
       if (elapsed.toMillis() <= Constants.CLIENT_ACTION_TIME)   // Too fast!
       {             
           //return;   // Not time to act yet   TODO, FIX
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
               
           case 'y':    // Restart Game 
           case 'Y':
               if (isDead())
               {
                   reInitPlayer();
                   updateNow();
               }
               break; 

           case 'n':    // End Game 
           case 'N':
               if (isDead())
               {
                   removeMe();
               }               
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
	            removeMe();
	            JavaTools.printlnTime( "Player Timed Out: " + description );
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
}