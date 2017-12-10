package com.schemafactor.rogueserver.entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.universe.Cell;
import com.schemafactor.rogueserver.universe.Dungeon;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;
import com.schemafactor.rogueserver.entities.Entity;

public abstract class HumanPlayer extends Entity
{        
   protected InetAddress userIP;                           // User IP Address
   protected boolean announceReceived = false;
   protected Instant lastUpdateReceived = Instant.now();   // For timeouts
   protected Instant lastUpdateSent = Instant.now();       // For periodic updates
   protected int lastActionCounter = -1;                   // Invalid
   
   // Mini state machine for escape sequences
   int escapeSequenceStep = 0;
   
   public HumanPlayer(String description, Position startposition, entityTypes type, byte charCode)
   {
       super(description, startposition, type, charCode, 1f);     
   }
   
   /** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
   }
   
   /** Update me with command from client */
   public void handleKeystroke(int inputchar)
   {   
       // Special handling for escape sequences
       if (escapeSequenceStep > 0)
       {
           boolean handled = handleEscapeSequence(inputchar);
           if (handled)
           {
               return;
           }           
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
               
           case 'l':
               handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_DOWN);
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
               
           case '=':
           case '+':
               handleAction(Constants.ACTION_PICKUP, Constants.HAND_RIGHT);
               break;
               
           case '-':
               handleAction(Constants.ACTION_PICKUP, Constants.HAND_LEFT);
               break;
               
           // Special cases for Cursor and Function Keys
           case 27:
               escapeSequenceStep = 1;
               return;
           
           default:
               JavaTools.printlnTime("Invalid command " + inputchar + " from " + description);
               return;
       }
       
       lastUpdateReceived = Instant.now();
   }

   
   private boolean handleEscapeSequence(int inputchar)
   {
       // Special handling for escape sequences
       if (escapeSequenceStep == 1)
       {
           switch (inputchar)
           {
               case 91:   //   '['
                   escapeSequenceStep = 2;
                   return true;
               
               default:
                   escapeSequenceStep = 0;    // Reset sequence and handle character normally
                   return false;                    
           }
       }
       
       if (escapeSequenceStep == 2)
       {
           switch (inputchar)
           {
               case 'A':
                   handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_NORTH);
                   escapeSequenceStep = 0;
                   return true;
                   
               case 'B':
                   handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_SOUTH);
                   escapeSequenceStep = 0;
                   return true;
               
               case 'C':
                   handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_EAST);
                   escapeSequenceStep = 0;
                   return true;
                   
               case 'D':
                   handleAction(Constants.ACTION_MOVE, Constants.DIRECTION_WEST);
                   escapeSequenceStep = 0;
                   return true;
                   
               case  49:  // Shifted case
                   escapeSequenceStep = 3;
                   return true;
               
               default:
                   escapeSequenceStep = 0;
                   return false;
           }
       }
       
       if (escapeSequenceStep == 3)
       {
           switch (inputchar)
           {
               case 59:
                   escapeSequenceStep = 4;
                   return true;
                   
               default:
                   escapeSequenceStep = 0;
                   return false;
           }
       }
       
       if (escapeSequenceStep == 4)
       {
           switch (inputchar)
           {
               case 50:
                   escapeSequenceStep = 5;
                   return true;
                   
               default:
                   escapeSequenceStep = 0;
                   return false;
           }
       }
       
       if (escapeSequenceStep == 5)
       {
           switch (inputchar)
           {
               case 'A':
                   handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_NORTH);
                   escapeSequenceStep = 0;
                   return true;
                   
               case 'B':
                   handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_SOUTH);
                   escapeSequenceStep = 0;
                   return true;
               
               case 'C':
                   handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_EAST);
                   escapeSequenceStep = 0;
                   return true;
                   
               case 'D':
                   handleAction(Constants.ACTION_ATTACK, Constants.DIRECTION_WEST);
                   escapeSequenceStep = 0;
                   return true;
                   
               default:
                   escapeSequenceStep = 0;    // Reset sequence and handle character normally
                   return false;                    
           }
       }
       
       
       return false;
   }

   protected void handleAction(byte action, byte parameter1) 
   {
       boolean moved = false;
       
       switch (action)
       {
          case Constants.ACTION_HEARTBEAT:
              JavaTools.printlnTime("DEBUG: heartbeat received from " + description);
              break;
              
          case Constants.ACTION_MOVE:
              moved = attemptMove(parameter1);
              break;
              
          case Constants.ACTION_PICKUP:
              moved = attemptPickup(parameter1);
              break;
              
         case Constants.ACTION_DROP:
             moved = attemptDrop(parameter1);
             break;
             
         case Constants.ACTION_ATTACK:
             moved = attemptAttack(parameter1);
             break;
              
              /*
          public static final byte ACTION_USE        = 2;
          public static final byte ACTION_DIG        = 3;
          public static final byte ACTION_EXAMINE    = 5;
          public static final byte ACTION_OPEN       = 6;
          public static final byte ACTION_CLOSE      = 7;
          public static final byte ACTION_CAST       = 8;        
          public static final byte ACTION_DROP       = 10;
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
}