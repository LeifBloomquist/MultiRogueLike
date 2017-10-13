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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.HumanPlayer;
import com.schemafactor.rogueserver.universe.Dungeon;

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
        while (true)   // Always loop and try to recover in case of exceptions
        {  
            try
            {
                Thread.currentThread().setName("Rogue UDP Listener Thread");
                
                byte[] buf = new byte[50];
                
                @SuppressWarnings("resource")
				DatagramSocket socket = new DatagramSocket(port);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    
                JavaTools.printlnTime( "Server address: " + InetAddress.getLocalHost() );
                JavaTools.printlnTime( "Waiting for packets on port " + port );
                
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
        
        Dungeon universe = Dungeon.getInstance();        
                
        // Determine player
        // Copy list to get around the dreaded Concurrent modification exception  (shallow copy)
        List<Entity> entitiesCopy = new ArrayList<Entity>(universe.getEntities());
        
        for (Entity e : entitiesCopy)
        {         
            if ( e.getType().equals(Entity.entityTypes.HUMAN_PLAYER) )
            {
                HumanPlayer hp = (HumanPlayer)e;
                
                if ( hp.getAddress().equals( packet.getAddress()) )   // Match found.  There's probably a faster way to do this, hashtable etc.
                {
                    hp.receiveUpdate(packet);
                    return;
                }                    
            }
        }
        
        // No match, create new user and add to vector
        JavaTools.printlnTime( "Creating player from " + JavaTools.packetAddress(packet) );
        HumanPlayer who = new HumanPlayer(packet);
        
        try
        {
            universe.newEntities.add(who);
        }
        catch (Exception e)
        {
            JavaTools.printlnTime( "EXCEPTION adding new player: " + JavaTools.getStackTrace(e) );
        }
        
        return;  
   }
}