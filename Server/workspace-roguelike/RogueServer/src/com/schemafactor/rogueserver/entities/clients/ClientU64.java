package com.schemafactor.rogueserver.entities.clients;

import java.io.DataOutputStream;
import java.time.Instant;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;

public class ClientU64 extends ClientC64
{   
    private static final long serialVersionUID = 1L;
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
       byte[] buffer = getUpdateByteArray(true, 368);  // Screen Only
       
       // Send the packet.
       sendUpdateMessage(buffer);
       lastUpdateSent = Instant.now();
       
       return;
   }  
   
   private void sendUpdateMessage(byte[] data)
   {
       if (output == null) return;
       
       try
       {            
           output.write(data);
           output.flush();
           //JavaTools.printlnTime("Wrote " + data.length + " bytes" );
       }
       catch (Exception e)
       {
           JavaTools.printlnTime("EXCEPTION writing to TCP [Ultimate 64] stream: " + e.getMessage());
           removeMe();
       }
   }  
   
   @Override
   public void addMessage(String msg)
   {
       messageQueue.add(msg);
       
       byte[] buffer = getMessagesByteArray();
       
       // Send the packet.
       sendUpdateMessage(buffer);
   }
   
   // On screen messages
   protected byte[] getMessagesByteArray()
   { 
       // Send data packet to the client              
       byte[] buffer = new byte[161];
       buffer[0] = Constants.PACKET_MESSAGES;
       
       int offset = 1;
       
       for (int i=3; i >= 0; i--)
       {
           byte[] message = getMessage(i).toUpperCase().getBytes();
           System.arraycopy( message, 0, buffer, offset, Math.min(message.length, Constants.MESSAGE_LENGTH) );
           offset += Constants.MESSAGE_LENGTH;
       }
       
       return buffer;
   }
}
