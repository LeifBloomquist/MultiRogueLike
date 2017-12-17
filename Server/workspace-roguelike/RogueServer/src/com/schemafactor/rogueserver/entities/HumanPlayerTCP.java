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
  
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second as above
   @Override
   public void updateNow()
   {   
       String screen = Constants.ANSI_CLEAR;
       
       if (showingHelp)
       {
           screen += "Help  (Press H again to return to game)\r\n\r\n";
           screen += "QWE\r\n";
           screen += "ASD = Move\r\n";
           screen += "ZXC\r\n\r\n";
           screen += "SHIFT+Move = Attack\r\n\r\n";
           screen += "J = Pick up item (Left  Hand)\r\n";
           screen += "K = Pick up item (Right Hand)\r\n\r\n";
           screen += "SHIFT+J,K = Drop Item (Left, Right)\r\n\r\n";
           screen += "* = Use item at current location\r\n";
       }
       else
       {      
           int width = Constants.SCREEN_WIDTH + 2;
           char bordercell = ExtendedAscii.getAscii(219);
           char[] chars = new char[width];
           Arrays.fill(chars, bordercell);
           String border = new String(chars) + "\r\n";
           
           // Get the screen that is visible to this player
           byte[] visible = Dungeon.getInstance().getScreenCentered(position);              
           byte[][] rows = JavaTools.splitBytes(visible, Constants.SCREEN_WIDTH);
                 
           // Items currently held
           char held_left = 32;  // Blank
           char held_right = 32;  // Blank
          
           if (item_left != null)
           {
               held_left = PETSCII.getExtendedASCII( item_left.getCharCode() );
           }
    
           if (item_right != null)
           {
               held_left = PETSCII.getExtendedASCII( item_right.getCharCode() );
           }
           
           // Item currently seen
           char seen = PETSCII.getExtendedASCII( Dungeon.getInstance().getCell(position).getItemCharCode() );
           
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
                       screen += " Left:  " + held_left;
                   break;
                       
                   case 5:
                       screen += " Right: " + held_right;
                   break;  
                   
                   case 7:
                       screen += " Health: " + (int)health;
                   break;   
               }
               
               screen += "\r\n";
           }
           
           screen += border;       
           screen += "\r\n";    
           
           // TODO Chat messages
           screen += "...\r\n";
           
           // TODO network activity char
       }     
     
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