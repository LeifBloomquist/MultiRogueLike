package com.schemafactor.rogueserver.entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.PETSCII;
import com.schemafactor.rogueserver.universe.Dungeon;

public class HumanPlayerUDP extends HumanPlayer
{         
   /** Creates a new instance of Human Player */
   public HumanPlayerUDP(DatagramPacket packet)
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
               description = PETSCII.toASCII(Arrays.copyOfRange(data, 2, data.length)) + " [" + JavaTools.packetAddress(packet) + "]";
               
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
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   public void updateNow()
   { 
       // Send data packet to the client              
       byte[] buffer = new byte[500];       
       buffer[0] = Constants.PACKET_UPDATE;
       
       int offset = 1;
              
       // Get the screen that is visible to this player
       System.arraycopy( Dungeon.getInstance().getScreenCentered(position), 0, buffer, offset, Constants.SCREEN_SIZE );
       offset += Constants.SCREEN_SIZE;
       
       // TODO 40 bytes - On screen messages
       byte[] message = new byte[Constants.MESSAGE_LENGTH];
       System.arraycopy( message, 0, buffer, offset, Constants.MESSAGE_LENGTH );
       offset += Constants.MESSAGE_LENGTH;
       
       // Item underneath current position
       buffer[offset++] = Dungeon.getInstance().getCell(position).getCharCode();
       
       // Item currently held
       if (item != null)
       {
           buffer[offset++] = item.getCharCode();
       }
       else
       {
           buffer[offset++] = 0;
       }
       
       // Send the packet.
       sendUpdatePacket(buffer);
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