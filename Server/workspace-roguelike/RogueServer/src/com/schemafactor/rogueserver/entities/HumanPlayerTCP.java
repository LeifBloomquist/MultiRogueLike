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
   public void receiveUpdate(int inputchar)
   {   
       switch (inputchar)   // Packet type
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
           
           default:
               JavaTools.printlnTime("Invalid command " + inputchar + " from " + description);
               return;
       }
       
       lastUpdateReceived = Instant.now();
   }

  
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   @Override
   public void updateNow()
   {        
       int width = Constants.SCREEN_WIDTH + 2;
       char bordercell = ExtendedAscii.getAscii(219);
       char[] chars = new char[width];
       Arrays.fill(chars, bordercell);
       String border = new String(chars) + "\r\n";
       
       // Get the screen that is visible to this player
       byte[] visible = Dungeon.getInstance().getScreenCentered(position);              
       byte[][] rows = JavaTools.splitBytes(visible, Constants.SCREEN_WIDTH);
             
       // Item currently held
       char held = 32;  // Blank       
      
       if (item != null)
       {
           held = PETSCII.getExtendedASCII( item.getCharCode() );
       }
       
       // Item currently seen
       char seen = PETSCII.getExtendedASCII( Dungeon.getInstance().getCell(position).getCharCode() );
       
       String screen = Constants.ANSI_CLEAR + ":";
       
       /*
       for (int i=0; i<=255; i++)
       {
           screen += "i="+i +"\t" + ExtendedAscii.getAscii(i);
       }
       */
       
       
       // TODO On screen messages
       screen += "Rogue Server Update " + new Date().toString() + "\r\n";       
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
                   screen += " I See: " + seen;
               break;
                   
               case 4:
                   screen += " Left:  " + held;
               break;
                   
               case 5:
                   screen += " Right: x";
               break;               
           }
           
           screen += "\r\n";
       }
       
       screen += border;       
       screen += "\r\n";    
       
       // TODO Chat messages
       screen += "...\r\n";
       
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