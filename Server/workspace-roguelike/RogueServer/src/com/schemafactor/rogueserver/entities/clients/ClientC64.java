package com.schemafactor.rogueserver.entities.clients;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Arrays;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.PETSCII;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.universe.Dungeon;

public class ClientC64 extends Client
{         
   /** Creates a new instance of C64 Client */
   public ClientC64(DatagramPacket packet)
   {
       // Random starting positions on Level 0 for multiple players  TODO
       super("C64 Client", new Position(5,5,0), entityTypes.CLIENT, Constants.CHAR_PLAYER_NONE);
       // [" + JavaTools.packetAddress(packet)+"]

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
               String raw_desc = PETSCII.toASCII(Arrays.copyOfRange(data, 2, data.length));   //  + " [" + JavaTools.packetAddress(packet) + "]";
               description = JavaTools.Sanitize(raw_desc);
               
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
               
               byte petscii_char = data[2];
               byte ascii_char = PETSCII.toASCII(petscii_char);     
               handleKeystroke(ascii_char);              
           }
           break;
           
           default:
           {
               JavaTools.printlnTime("Bad packet type " + data[0] + " from " + description);
               return;
           }
       }
   }
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   public void updateNow()
   {
       byte[] buffer = getUpdateByteArray();
       
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