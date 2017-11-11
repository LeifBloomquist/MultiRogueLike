package com.schemafactor.rogueserver.entities;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.ExtendedAscii;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.PETSCII;
import com.schemafactor.rogueserver.universe.Dungeon;

public class HumanPlayerTCP extends HumanPlayer
{   
    PrintWriter output= null;
    
   /** Creates a new instance of Human Player 
 * @param output */         
   public HumanPlayerTCP(String login, PrintWriter output)
   {
       super(login, new Position(15,15,0), entityTypes.HUMAN_PLAYER, Constants.CHAR_PLAYER_NONE);
       
       announceReceived = true;
       this.output = output;
       
       userIP = null;   // Not needed for TCP connections
   }

/** Return the InetAddress, for comparisons */
   public InetAddress getAddress()
   {
       return userIP;
   }

   /** Update me with new data from client */
   public void receiveUpdate(byte[] data)
   {
       /*
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
       */
       
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
              
          case Constants.ACTION_PICKUP:
              moved = attemptPickup();
              break;
              
         case Constants.ACTION_DROP:
             moved = attemptDrop();
             break;
              
              /*
          public static final byte ACTION_USE        = 2;
          public static final byte ACTION_DIG        = 3;
          public static final byte ACTION_ATTACK     = 4;
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
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   public void updateNow()
   {        
       int width = Constants.SCREEN_WIDTH + 2;
       char bordercell = ExtendedAscii.getAscii(219);
       char[] chars = new char[width];
       Arrays.fill(chars, bordercell);
       String border = new String(chars);
       
       // Get the screen that is visible to this player
       byte[] visible = Dungeon.getInstance().getScreenCentered(position);              
       byte[][] rows = JavaTools.splitBytes(visible, Constants.SCREEN_WIDTH);
             
       // Item currently held
       char held = 32;  // Blank       
      
       if (item != null)
       {
           held = PETSCII.toExtendedASCII( item.getCharCode() );
       }
       
       String screen = Constants.ANSI_CLEAR;
       
       // TODO On screen messages
       screen += "Rogue Server Update " + new Date().toString() + "\n";       
       screen += border;
       
       for (int row=0; row < Constants.SCREEN_HEIGHT; row++)
       {
           screen += bordercell + PETSCII.toExtendedASCII(rows[row]) + bordercell;
           
           switch (row)
           {
               case 0: 
                   screen += " " + description;
               break;
               
               case 2:
                   screen += "I See: " + PETSCII.toExtendedASCII( Dungeon.getInstance().getCell(position).getCharCode() );
               break;
                   
               case 4:
                   screen += "Left:  " + held;
               break;
                   
               case 5:
                   screen += "Right: x";
               break;
               
           }
           
           screen += "\n";
       }
       
       screen += border;       
       screen += "\n";    
       
       // TODO Chat messages
       screen += "...\n";
       
       // TODO network activity char
     
       // Send the packet.
       sendUpdatePacket(screen);
       lastUpdateSent = Instant.now();
       
       return;
   }  
   
   private void sendUpdatePacket(String data)
   {       
       try
       {            
           output.print(data);
           output.flush();
       }
       catch (Exception e)
       {
           JavaTools.printlnTime("EXCEPTION sending update: " + JavaTools.getStackTrace(e));
       }
   }  
   
   
}