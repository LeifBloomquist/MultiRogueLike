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
import com.schemafactor.rogueserver.dungeon.Dungeon;

public class ClientC64 extends Client
{         
   /** Creates a new instance of C64 Client from UDP Packet (i.e. C64 with CS8900) */
   public ClientC64(String description, byte[] data, InetAddress address)
   {
       // Random starting positions on Level 0 for multiple players  TODO
       super(description, new Position(5,5,0), entityTypes.CLIENT, Constants.CHAR_PLAYER_NONE);

       userIP = address;       
       receiveUpdate(data);
   }

   /** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
   }

   /** Update me with new data from client */
   public void receiveUpdate(byte[] data)
   {   
       switch (data[0])   // Packet type
       {
           case Constants.CLIENT_ANNOUNCE:
           {
               String raw_desc = PETSCII.toASCII(Arrays.copyOfRange(data, 2, data.length));
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
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second
   public void updateNow()
   {
       byte[] buffer = getUpdateByteArray();
       
       // Send the packet.
       sendUpdatePacket(buffer);
       
       // Clear timers and flags.
       lastUpdateSent = Instant.now();
       updateMeFlag = false;
       
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