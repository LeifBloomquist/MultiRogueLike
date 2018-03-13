package com.schemafactor.rogueserver.entities.clients;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Arrays;

import org.java_websocket.WebSocket;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.EscapeSequences;
import com.schemafactor.rogueserver.common.ExtendedAscii;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.PETSCII;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.universe.Dungeon;

public class ClientWebSocket extends Client
{   
    private static final long serialVersionUID = 1L;
    WebSocket websocket = null;
    
    /** Creates a new instance of WebSocket Client */         
    public ClientWebSocket(WebSocket websocket)
    {
       super("WebSocket Client " + websocket.getRemoteSocketAddress().getAddress().getHostAddress(), new Position(15,15,0), entityTypes.CLIENT, Constants.CHAR_PLAYER_NONE);
       announceReceived = false;       
       this.websocket = websocket;       
   }
   
   public WebSocket getWebSocket()
   {
       return websocket;
   }
  
   // Send an update.  Can be called directly i.e. in response to a player action or change
   @Override
   public void updateNow()
   {     
       byte[] buffer = getUpdateByteArray();
       
       if (websocket.isOpen())
       {
           websocket.send(buffer);       
       }
       else
       {
           JavaTools.printlnTime("DEBUG: Attempt to send to closed WebSocket " + description);
           return;
       }
       
       lastUpdateSent = Instant.now();       
       return;
   }

    public void receiveUpdate(byte[] data)
    {
        switch (data[0])   // Packet type
        {
            case '1':   // As provided by sendAnnounce() in JavaScript code 
            {
                String name = "";
                try
                {
                    name = new String(data, "UTF-8");
                } 
                catch (UnsupportedEncodingException e)
                {
                    JavaTools.printlnTime("EXCEPTION parsing string: " + name + " " + e.getMessage());
                    return;
                }
                
                this.description = name.substring(1, description.length());
                
                JavaTools.printlnTime( "WebSocket Player Joined: " + description );
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
                
                byte ascii_char =  data[2];      
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
}