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
       // Send data packet to the client              
       byte[] buffer = new byte[527];       
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
       
       // End of packet marker
       buffer[offset++] = (byte)255;
       
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