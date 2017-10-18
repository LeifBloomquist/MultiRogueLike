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
   private int lastActionCounter = -1; // Invalid
  
   /** Creates a new instance of Human Player */
   public HumanPlayer(DatagramPacket packet)
   {
       // Random starting positions on Level 0 for multiple players  TODO
       super("Human Player [" + JavaTools.packetAddress(packet)+"]", new Position(20,20,0), entityTypes.HUMAN_PLAYER);

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
               description = JavaTools.fromPETSCII(Arrays.copyOfRange(data, 2, data.length)) + " [" +JavaTools.packetAddress(packet) + "]";
               
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
	   switch (action)
	   {
	   	  case Constants.ACTION_HEARTBEAT:
	   		  JavaTools.printlnTime("DEBUG: heartbeat received from " + description);
	   		  break;
	   		  
	   	  case Constants.ACTION_MOVE:
	   		  attemptMove(parameter1);
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
	   sendUpdate();
   }

   private void attemptMove(byte direction) 
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
	   		  // if current cell type == stairs_up...
	   		  break;
	   		  
	   	  case Constants.DIRECTION_DOWN:
	   		 // if current cell type == stairs_down...
	   		 break;
	   		 
	   	  default:
	   		 JavaTools.printlnTime("Unknown move direction code " + direction + " from " + description);
	   		 break;  
	   }
	   
	   Position destination = new Position(this.position.x+dx, this.position.y+dy, this.position.z+dz);	   
	   Cell dest_cell = Dungeon.getInstance().getCell(destination);
	   
	   if (dest_cell.canEnter())
	   {
		   this.position = destination;
		   JavaTools.printlnTime("DEBUG: " + description + " moved to location X=" + position.x + " Y=" + position.y + " X=" + position.x);
		   // set dest_cell entity here?
	   }
	   else
	   {
		   ;
		   JavaTools.printlnTime("DEBUG: " + description + " was blocked moving to X=" + destination.x + " Y=" + destination.y + " X=" + destination.x);
	   }
	
   }

@Override
   public void update()
   { 
	   Duration elapsed = Duration.between(lastUpdateSent, Instant.now());
       
       if (elapsed.getSeconds() >= 1)  // Send an update every 1 second
       {
    	   sendUpdate();
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
	            removeMeFlag = true;
	            JavaTools.printlnTime( "Player Timed Out: " + description );
	        }             
	    }      
	}
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   public void sendUpdate()
   { 
       // Send data packet to the client              
       byte[] message = new byte[482];           // 1 + 441 + 40 = 482
       message[0] = Constants.PACKET_UPDATE;
       
       int offset = 1;
              
       // Get the screen that is visible to this player
       
       // And now, viewport  
       // TODO Center on player position
       System.arraycopy( Dungeon.getInstance().getScreen(position), 0, message, offset, Constants.SCREEN_SIZE );
       
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