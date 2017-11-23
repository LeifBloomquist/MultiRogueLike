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
   
   public HumanPlayer(String description, Position startposition, entityTypes type, byte charCode)
   {
       super(description, startposition, type, charCode, 1f);     
   }
   
   /** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
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
              moved = attemptPickup();
              break;
              
         case Constants.ACTION_DROP:
             moved = attemptDrop();
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