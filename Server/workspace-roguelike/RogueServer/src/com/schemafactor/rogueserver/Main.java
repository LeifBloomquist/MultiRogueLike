package com.schemafactor.rogueserver;

import java.io.FileNotFoundException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.clients.Client;
import com.schemafactor.rogueserver.network.WebSocketListener;
import com.schemafactor.rogueserver.network.TCPListener;
import com.schemafactor.rogueserver.network.TCPListenerU64;
import com.schemafactor.rogueserver.network.UDPListener;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Main 
{
    // Some run-time statistics for monitoring
    static public double avg_ms = 0d;
    static public double avg_cpu = 0d;
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        JavaTools.printlnTime("-----------------------------------------------");
        JavaTools.printlnTime("Rogue Server (Mini) Version " + Constants.VERSION );
        
        JavaTools.onlyOneInstance("rogueserver");
        
        // Create the universe.
        JavaTools.printlnTime("Creating game dungeon...");
        Dungeon dungeon = Dungeon.getInstance();
        dungeon.Create(Constants.DUNGEON_SIZE, Constants.DUNGEON_DEPTH);
        
        // Load saved
        JavaTools.printlnTime("Loading game levels...");   // TODO, persistence
        
        String prefix = "";
        
        if (args.length > 0)
        {
            if (args[0].equals("-local"))
            {
                prefix += "C:/Leif/GitHub/MultiRogueLike/Server/data/mini/";
                JavaTools.printlnTime("Local Mode specified - Timeouts disabled.");
                Client.setDemoMode();
            }
            
            if (args[0].equals("-demo"))
            {
                JavaTools.printlnTime("Demo Mode specified - Timeouts disabled.");
                Client.setDemoMode();
            }
        }
                
        try 
        {
            dungeon.LoadTXT(prefix+"LevelTest0.txt", 0);
            dungeon.LoadTXT(prefix+"LevelTest1.txt", 1);
            dungeon.LoadTXT(prefix+"LevelTest2.txt", 2);
            dungeon.LoadTXT(prefix+"LevelTest3.txt", 3);
		} 
        catch (FileNotFoundException e) 
        {
        	JavaTools.printlnTime("FileNotFoundException!");
			e.printStackTrace();
			System.exit(0);
		}
        
        // Add some entities.
        JavaTools.printlnTime("Creating and placing default entities...");
        Spawner.spawnEntities(dungeon);
        Spawner.placeItems(dungeon);
        
        // Serialization test
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