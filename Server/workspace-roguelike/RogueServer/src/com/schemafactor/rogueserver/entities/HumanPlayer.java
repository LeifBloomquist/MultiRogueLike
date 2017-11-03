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

public class HumanPlayer extends Entity
{        
   private InetAddress userIP;       // User IP Address
   private boolean announceReceived = false;
   private Instant lastUpdateReceived = Instant.now();   // For timeouts
   private Instant lastUpdateSent = Instant.now();       // For periodic updates
   private int lastActionCounter = -1;                   // Invalid
  
   /** Creates a new instance of Human Player */
   public HumanPlayer(DatagramPacket packet)
   {
       // Random starting positions on Level 0 for multiple players  TODO
       super("Human Player [" + JavaTools.packetAddress(packet)+"]", new Position(5,5,0), entityTypes.HUMAN_PLAYER, Constants.CHAR_PLAYER_NONE);

       userIP = packet.getAddress();
       receiveUpdate(packet);
   }
         
   /** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
   }

   /** Update me with new data from client */
   public void receiveUpdate(DatagramPacket packet)
   {
       byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());    
       
       switch (data[0])   // Packet type
       {
           case Constants.CLIENT_ANNOUNCE:
           {
               description = JavaTools.fromPETSCII(Arrays.copyOfRange(data, 2, data.length)) + " [" + JavaTools.packetAddress(packet) + "]";
               
               if (!announceReceived)
               {
                   JavaTools.printlnTime( "Player Joined: " + description );                   
               }
               
               announceReceived = true;
           }
           break;
           
           case Constants.CLIENT_UPDATE:
           {             
        	   int actioncounter=data[1];        	   
        	   if (lastActionCounter == actioncounter) // Duplicate?
        	   {
        		  return;
        	   }
        	   
        	   lastActionCounter = actioncounter;        	   
        	   handleAction(data[2], data[3]);              
           }
           break;
           
           default:
           {
               JavaTools.printlnTime("Bad packet type " + data[0] + " from " + description);
               return;
           }
       }
       
       lastUpdateReceived = Instant.now();
   }

   private void handleAction(byte action, byte parameter1) 
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
	   		  
	   		  /*
	      public static final byte ACTION_USE        = 2;
	      public static final byte ACTION_DIG        = 3;
	      public static final byte ACTION_ATTACK     = 4;
	      public static final byte ACTION_EXAMINE    = 5;
	      public static final byte ACTION_OPEN       = 6;
	      public static final byte ACTION_CLOSE      = 7;
	      public static final byte ACTION_CAST       = 8;
	      public static final byte ACTION_PICKUP     = 9;
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
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   public void updateNow()
   { 
       // Send data packet to the client              
       byte[] message = new byte[482];           // 1 + 441 + 40 = 482
       message[0] = Constants.PACKET_UPDATE;
       
       int offset = 1;
              
       // Get the screen that is visible to this player
       System.arraycopy( Dungeon.getInstance().getScreenCentered(position), 0, message, offset, Constants.SCREEN_SIZE );
       
       // TODO, messages, etc.
       
       // Send the packet.
       sendUpdatePacket(message);
       lastUpdateSent = Instant.now();
       
       return;
   }  
   
   private void sendUpdatePacket(byte[] data)
   {       
       try
       {            
           // Initialize a datagram packet with data and address
           DatagramPacket packet = new DatagramPacket(data, data.length, userIP, 3000); 

           // Create a datagram socket, send the packet through it, close it
           DatagramSocket dsocket = new DatagramSocket();
           dsocket.send(packet);
           dsocket.close();
       }
       catch (Exception e)
       {
           JavaTools.printlnTime("EXCEPTION sending update: " + JavaTools.getStackTrace(e));
       }
   }  
}