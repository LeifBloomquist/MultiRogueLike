package com.schemafactor.rogueserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.clients.Client;
import com.schemafactor.rogueserver.network.WebSocketListener;
import com.schemafactor.rogueserver.network.TCPListener;
import com.schemafactor.rogueserver.network.TCPListenerU64;
import com.schemafactor.rogueserver.network.UDPListener;

public class Main 
{    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
    	String inifile = "";    	
    	String mode = "";
    	String prefix = "";
    	
        JavaTools.printlnTime("-----------------------------------------------");
        JavaTools.printlnTime("Rogue Server Version " + Constants.VERSION );
        
        JavaTools.onlyOneInstance("rogueserver");
        
        // Create the dungeon.
        JavaTools.printlnTime("Creating game dungeon...");
        Dungeon dungeon = Dungeon.getInstance();
        
        // Read in dungeon specifics
        
        switch (args.length)
        {		
        	case 1:
        		inifile = args[0];
        		break;
        	
        	case 2:
        		inifile = args[0];
        		mode = args[1];
        		break;
        		
        	default:
        		JavaTools.printlnTime("Must at least specify an INI file!  Usage: java -jar rogueserver.jar <ini> [-local | -demo]");
    			System.exit(1);        		
        }
        
        // Different modes for demos or local development
        if (mode.equals("-local"))
        {
        	String path = new File(inifile).getParent();
        	prefix += path + "\\";
            JavaTools.printlnTime("Local Mode specified - Timeouts disabled and local path set.");
            Client.setDemoMode();
        }
        
        if (mode.equals("-demo"))
        {
            JavaTools.printlnTime("Demo Mode specified - Timeouts disabled.");
            Client.setDemoMode();
        }
        
        // Initialize the dungeon and spawn all monsters/items as per INI file                 
        Spawner.initialize(dungeon, prefix, inifile);
        
        // Serialization test  // TODO, persistence
        // Persistence.Serialize(dungeon);        
       
        // Start the thread that updates everything at a fixed interval
        JavaTools.printlnTime("Creating update scheduler...");
        UpdaterThread ut = new UpdaterThread();        
        ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
        s.scheduleAtFixedRate(ut, 0, Constants.TICK_TIME, TimeUnit.MILLISECONDS );
        
        // Instantiate a TCP listener, runs in background thread
        JavaTools.printlnTime("Creating TCP listener (Telnet)...");
        TCPListener tcp = new TCPListener();
        tcp.start(Constants.LISTEN_PORT);
        
        // Instantiate a TCP listener, runs in background thread
        JavaTools.printlnTime("Creating TCP listener (Ultimate 64)...");
        TCPListenerU64 tcpU64 = new TCPListenerU64();
        tcpU64.start(Constants.LISTEN_PORT_U64);
        
        // Instantiate a WebSocket Server
        JavaTools.printlnTime("Creating WebSocket listener...");
        WebSocketListener ws = new WebSocketListener(Constants.WEBSOCKET_PORT);
        ws.start();
        JavaTools.printlnTime( "WebSocket Server started on port: " + ws.getPort() );
        
        // Instantiate a UDP listener, and let it take over.
        JavaTools.printlnTime("Creating UDP listener...");
        UDPListener udp = new UDPListener();
        udp.start(Constants.LISTEN_PORT);
    }
}