package com.schemafactor.rogueserver.entities.clients;

import java.io.PrintWriter;
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
    
    long test_counter = 0;
    
    /** Creates a new instance of WebSocket Client */         
    public ClientWebSocket(WebSocket websocket)
    {
       super("WebSocket Client", new Position(15,15,0), entityTypes.CLIENT, Constants.CHAR_PLAYER_NONE);
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
       websocket.send("Test Counter: " + test_counter);       
       test_counter++;
       
       lastUpdateSent = Instant.now();       
       return;
   }

public void receiveUpdate(byte[] message)
{
    // TODO Auto-generated method stub
    
}
   
   
   
}