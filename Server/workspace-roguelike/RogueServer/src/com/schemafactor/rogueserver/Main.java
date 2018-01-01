package com.schemafactor.rogueserver;

import java.io.FileNotFoundException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Position;
import com.schemafactor.rogueserver.entities.monsters.Skeleton;
import com.schemafactor.rogueserver.entities.monsters.Slime;
import com.schemafactor.rogueserver.entities.monsters.Spider;
import com.schemafactor.rogueserver.items.Chest;
import com.schemafactor.rogueserver.items.Key;
import com.schemafactor.rogueserver.items.Note;
import com.schemafactor.rogueserver.items.Shield;
import com.schemafactor.rogueserver.items.Sign;
import com.schemafactor.rogueserver.items.Sword;
import com.schemafactor.rogueserver.network.TCPListener;
import com.schemafactor.rogueserver.network.UDPListener;
import com.schemafactor.rogueserver.universe.Dungeon;
import com.schemafactor.rogueserver.universe.Persistence;

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
        JavaTools.printlnTime("Rogue Server Version " + Constants.VERSION );
        
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
                prefix += "C:/Leif/GitHub/MultiRogueLike/Server/data/test/";
            }
        }
                
        try 
        {
            dungeon.LoadTXT(prefix+"LevelTest0.txt", 0);
            dungeon.LoadTXT(prefix+"LevelTest1.txt", 1);
            dungeon.LoadTXT(prefix+"LevelTest2.txt", 2);
            dungeon.LoadTXT(prefix+"LevelTest3.txt", 3);
            dungeon.LoadTXT(prefix+"LevelTest4.txt", 4);
		} 
        catch (FileNotFoundException e) 
        {
        	JavaTools.printlnTime("FileNotFoundException!");
			e.printStackTrace();
			System.exit(0);
		}
        
        // Add some entities.
        JavaTools.printlnTime("Creating and placing default entities...");
        spawnEntities(dungeon);
        placeItems(dungeon);
        
        // A mini http server to show stats through a browser
        //JavaTools.printlnTime("Creating debug httpd server...");
        //VortexDebugServer vdbg = new VortexDebugServer(80);
                
        // Start the thread that updates everything at a fixed interval
        JavaTools.printlnTime("Creating update scheduler...");
        UpdaterThread ut = new UpdaterThread();        
        ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
        s.scheduleAtFixedRate(ut, 0, Constants.TICK_TIME, TimeUnit.MILLISECONDS );
        
        // Instantiate a TCP listener, runs in background thread
        JavaTools.printlnTime("Creating TCP listener...");
        TCPListener tcp = new TCPListener();
        tcp.start(Constants.LISTEN_PORT);
        
        // Serialization test
       // Persistence.Serialize(dungeon);
        
        // Instantiate a UDP listener, and let it take over.
        JavaTools.printlnTime("Creating UDP listener...");
        UDPListener udp = new UDPListener();
        udp.start(Constants.LISTEN_PORT);
    }

    private static void spawnEntities(Dungeon dungeon)
    {        
        // Level 0 (Entry)
        
       // dungeon.addEntity( new Spider("Spider Mike", new Position(7,7,0)) );
        dungeon.addEntity( new Slime("Slimey Fred", new Position(56,8,0)) );
        dungeon.addEntity( new Skeleton("Skeleton Pete", new Position(90,10,0)) );        
        
        // Level 1
        
        for (int i=1; i<=50; i++)
        {
            dungeon.addEntity( new Spider("Spider 1-" + i, new Position( JavaTools.generator.nextInt(dungeon.getXsize() / 4),  // /4 to put them near the player when first coming down the stairs
                                                                         JavaTools.generator.nextInt(dungeon.getYsize() / 4),
                                                                         1)) );
            
            dungeon.addEntity( new Slime("Slime 1-" + i, new Position( JavaTools.generator.nextInt(dungeon.getXsize() / 4),
                                                                       JavaTools.generator.nextInt(dungeon.getYsize() / 4),
                                                                       1)) );             
        } 
        
        // Level 2

        for (int i=1; i<=100; i++)
        {
            dungeon.addEntity( new Skeleton("Skeleton 2-" + i, new Position( JavaTools.generator.nextInt(dungeon.getXsize()),
                                                                             JavaTools.generator.nextInt(dungeon.getYsize()),
                                                                             2)) );
            
            dungeon.addEntity( new Slime("Slime 2-" + i, new Position( JavaTools.generator.nextInt(dungeon.getXsize()),
                                                                       JavaTools.generator.nextInt(dungeon.getYsize()),
                                                                       2)) );             
        } 
    }
    
    private static void placeItems(Dungeon dungeon)
    {
        // Add some test items.
        dungeon.placeItem( new Sword("Short Sword", 5, 5), 
                           new Position(10,10,0));    
      
        dungeon.placeItem( new Shield("Wooden Shield", 1, 10), 
                           new Position(11,10,0));    

        dungeon.placeItem( new Key("Rusty Key", null), 
                           new Position(12,10,0));  
        
        dungeon.placeItem( new Sword("Sword of Doom", 20, 10), 
                           new Position(33,7,1));        

        dungeon.placeItem( new Sign("Crooked Sign", "Welcome to Level 0"), 
                           new Position(8,4,0));
        
        dungeon.placeItem( new Note("Tattered Note", "The first Note. Hello!"), 
                           new Position(9,4,0));
        
        dungeon.placeItem( new Chest("Fancy Chest", 
                           new Key("Shiny Key", null)), 
                           new Position(10,5,0));
    }
}
