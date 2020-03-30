package com.schemafactor.rogueserver.entities.clients;

import java.io.DataOutputStream;
import java.time.Instant;

import com.schemafactor.rogueserver.common.JavaTools;

public class ClientU64 extends ClientC64
{   
   DataOutputStream output = null;
    
   /** Creates a new instance of C64 Client from TCP Connection (i.e. Ultimate 64) */
   public ClientU64(byte[] data, DataOutputStream output)
   {       
       super(data, null);
       this.output = output;
   }
   
   // Send an update.  Can be called directly i.e. in response to a player action or change, or once per second
   @Override
   public void updateNow()
   {
       byte[] buffer = getUpdateByteArray();
       
       // Send the packet.
       sendUpdateMessage(buffer);
       lastUpdateSent = Instant.now();
       
       return;
   }  
   
   private void sendUpdateMessage(byte[] data)
   {
       try
       {            
           output.write(data);
           output.flush();
       }
       catch (Exception e)
       {
           JavaTools.printlnTime("EXCEPTION writing to TCP [Ultimate 64] stream: " + e.getMessage());
           removeMe();
       }
   }  
}