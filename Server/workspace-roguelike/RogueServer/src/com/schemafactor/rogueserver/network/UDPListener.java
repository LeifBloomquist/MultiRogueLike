package com.schemafactor.rogueserver.network;

/*
 * UDPListener.java
 *
 * Created on December 14, 2012, 2:56 PM
 *
 * Listens for UDP packets from clients and updates the internal model of the playing field.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.clients.Client;
import com.schemafactor.rogueserver.entities.clients.ClientC64;

/**
 * @author Leif Bloomquist
 */
public class UDPListener
{    
    /** Creates a new instance of UDPListener */
    public UDPListener()
    {
        ;
    }
    
    public void start(int port)
    {
        Thread.currentThread().setName("Rogue UDP Listener Thread (Main Thread)");
        
        while (true)   // Always loop and try to recover in case of exceptions
        {  
            try
            {   
                byte[] buf = new byte[50];
                
                @SuppressWarnings("resource")
				DatagramSocket socket = new DatagramSocket(port);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    
                JavaTools.printlnTime( "Server address: " + InetAddress.getLocalHost() );
                JavaTools.printlnTime( "UDP server is waiting for packets on port " + port );
                
                /* Loop Forever, waiting for packets. */      
                while (true) 
                {                            
                    socket.receive(packet);  // This blocks!                
                    handlePacket(packet);    // Handle it              
                }
                
                //socket.close();
            }
            catch (SocketException ex)
            {
                JavaTools.printlnTime( "Socket Exception: " + JavaTools.getStackTrace(ex));
            }
            catch (IOException ex)
            {
                JavaTools.printlnTime( "IO Exception: " + JavaTools.getStackTrace(ex));
            }
            
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                ;
            }
        }
    }
    
    /**
     *  Handle a received packet.
     */    
    private void handlePacket(DatagramPacket packet)
    {   
        // Check Checksum - Future
        
        Dungeon dungeon = Dungeon.getInstance();        
                
        // Determine player        
        List<Entity> clients = dungeon.getEntities(null, Entity.entityTypes.CLIENT);
        
        for (Entity e : clients)
        {   
            try
            {
                ClientC64 c64 = (ClientC64)e;
                
                if ( c64.getAddress().equals( packet.getAddress()) )   // Match found.  There's probably a faster way to do this, hashtable, HashSet etc.
                {
                    byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                    c64.receiveUpdate(data);
                    return;
                }
            }
            catch (ClassCastException ex)  // Fail silently on cast exceptions.  TODO, messy
            {                
                continue;
            }
            catch (Exception ex) 
            {
                JavaTools.printlnTime("EXCEPTION: " + ex.getMessage() + " when receiving from C64 UDP client");
                continue;
            }
        }
        
        // No match, create new user and add to list
        JavaTools.printlnTime( "Creating new player from " + JavaTools.packetAddress(packet) + " [UDP]");
        byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
        Client c64 = new ClientC64("C64 Client", data, packet.getAddress(), dungeon.getPlayerSpawnPosition());        
        dungeon.addEntity(c64);
        
        return;  
   }
}