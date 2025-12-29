package com.schemafactor.rogueserver.entities.clients;

import java.io.DataOutputStream;
import java.time.Duration;
import java.time.Instant;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;

public class ClientU64 extends ClientC64
{   
   private static final long serialVersionUID = 1L;
   DataOutputStream output = null;
    
   private static final long U64_THROTTLE_TIME    = 20;   // Milliseconds     
    
   /** Creates a new instance of C64 Client from TCP Connection (i.e. Ultimate 64) */
   public ClientU64(byte[] data, DataOutputStream output, Position start)
   {          
       super("U64 Client", data, null, start);
       this.output = output;
       
       sendUpdatePacket(getMessagesByteArray(), true);  // Since output is null in the super() call above...
   }
   
   // Send an update.
   @Override
   public void updateNow()
   {         
       byte[] buffer = getUpdateByteArray(true, Constants.PACKET_UPDATE_SIZE - (Constants.MESSAGE_LENGTH * 4));  // Screen Only
       
       // Send the packet.
       sendUpdatePacket(buffer, false);
       
       // Clear timers and flags.      
       updateMeFlag = false;
       
       return;
   }   
   
   @Override
   public void addMessage(String msg)
   {
       messageQueue.add(msg);
       
       byte[] buffer = getMessagesByteArray();
       
       // Send the packet.  On U64 send messages packet separately.
       sendUpdatePacket(buffer, true);
   }
   
   // On screen messages
   private byte[] getMessagesByteArray()
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
   
   private void sendUpdatePacket(byte[] data, boolean nolimit)
   {
       if (output == null) return;
       
       // Need to Rate Limit on U64
       Duration elapsed = Duration.between(lastUpdateSent, Instant.now());
       
       if (elapsed.toMillis() < U64_THROTTLE_TIME)  // 
       {
    	   JavaTools.printlnTime("WARNING! Sending U64 data too often??  elapsed (ms)=" + elapsed.toMillis() );
    	   //return;
       }       
       
       try
       {            
           output.write(data);
           output.flush();
           if (!nolimit) lastUpdateSent = Instant.now();
       }
       catch (Exception e)
       {
           JavaTools.printlnTime("EXCEPTION writing packet to TCP [Ultimate 64] stream: " + e.getMessage());
           removeMe();
       }
   }
}
