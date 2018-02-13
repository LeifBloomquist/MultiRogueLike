package com.schemafactor.rogueserver;

import java.io.FileNotFoundException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.monsters.Bat;
import com.schemafactor.rogueserver.entities.monsters.Ghost;
import com.schemafactor.rogueserver.entities.monsters.Skeleton;
import com.schemafactor.rogueserver.entities.monsters.Slime;
import com.schemafactor.rogueserver.entities.monsters.Spider;
import com.schemafactor.rogueserver.items.Chest;
import com.schemafactor.rogueserver.items.Key;
import com.schemafactor.rogueserver.items.MagicKey;
import com.schemafactor.rogueserver.items.Note;
import com.schemafactor.rogueserver.items.Potion;
import com.schemafactor.rogueserver.items.Shield;
import com.schemafactor.rogueserver.items.Sign;
import com.schemafactor.rogueserver.items.Sword;
import com.schemafactor.rogueserver.network.TCPListener;
import com.schemafactor.rogueserver.network.UDPListener;
import com.schemafactor.rogueserver.universe.Cell;
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
        JavaTools.printlnTime("Creating TCP listener...");
        TCPListener tcp = new TCPListener();
        tcp.start(Constants.LISTEN_PORT);
        
        // Instantiate a UDP listener, and let it take over.
        JavaTools.printlnTime("Creating UDP listener...");
        UDPListener udp = new UDPListener();
        udp.start(Constants.LISTEN_PORT);
    }
}