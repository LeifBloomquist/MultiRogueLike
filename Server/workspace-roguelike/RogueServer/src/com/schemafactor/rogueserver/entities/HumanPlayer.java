package com.schemafactor.rogueserver.entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.universe.Dungeon;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;
import com.schemafactor.rogueserver.entities.Entity;

public class HumanPlayer extends Entity
{        
   private InetAddress userIP;       // User IP Address
   private int timeoutCounter=0;     // Counter, in milliseconds, for timeouts for dropped connections
   
   private boolean announceReceived = false;
  
   /** Creates a new instance of Human Player */
   public HumanPlayer(DatagramPacket packet)
   {
       // Random starting positions for multiple players
       super("Human Player [" + JavaTools.packetAddress(packet)+"]", new Position(20,20,0));
       this.myType = entityTypes.HUMAN_PLAYER;

       userIP = packet.getAddress();
       receiveUpdate(packet);
   }
   
   public void sendUpdate(byte[] data)
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
      
   /** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
   }
   
   // Increment and check the timeout
   public void checkTimeout()
   {
       if (timeoutCounter < 10000) timeoutCounter += Constants.TICK_TIME;
       
       if (timeoutCounter > 2000)   // Two seconds 
       {
    	   /*
           if (!removeMeFlag)
           {
               removeMeFlag = true;
               JavaTools.printlnTime( "Player Timed Out: " + description );
           } 
           */              
       }       
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
        	   // TODO Determine player action
             
               // Cheat a little and do the position math here.  Eventually move to client?
               //move();         
           }
           break;
           
           default:
           {
               JavaTools.printlnTime("Bad packet type " + data[0] + " from " + userIP.toString());
               return;
           }
       }
                
       // Reset timeout
       timeoutCounter = 0;
   }

   @Override
   public void update()
   {                     
       // Send data packet to the client              
       byte[] message = new byte[500];  
       message[0] = Constants.PACKET_UPDATE;
       message[1] = 0; // Unused
       
       int offset = 6;
              
       // Get the screen that is visible to this player
       
       // And now, the first 20 lines (=800 bytes) of the screen.       
       System.arraycopy( Dungeon.getInstance().getScreen(position), 0, message, offset, Constants.SCREEN_SIZE);
       
       // Send the packet.
       sendUpdate(message);           
           
       // Increment and Timeout.  This is reset in receiveUpdate() above.     
       checkTimeout();
       return;
   }        
}