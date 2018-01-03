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
        spawnEntities(dungeon);
        placeItems(dungeon);
        
        // Serialization test
        // Persistence.Serialize(dungeon);
        
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
        
        // Instantiate a UDP listener, and let it take over.
        JavaTools.printlnTime("Creating UDP listener...");
        UDPListener udp = new UDPListener();
        udp.start(Constants.LISTEN_PORT);
    }

    private static void spawnEntities(Dungeon dungeon)
    {        
        // Level 0 (Entry) -----------------------------------------------------------------------------------------------------
        
        dungeon.addEntity( new Slime("Slimey", new Position(56,8,0)) );
        dungeon.addEntity( new Skeleton("Skeleton", new Position(89,11,0)) ); 
        dungeon.addEntity( new Spider("Spider", new Position(38,38,0)) );
        
        // Level 1 -------------------------------------------------------------------------------------------------------------
        
        for (int i=1; i<=20; i++)
        {
            dungeon.addEntity( new Spider("Spider", dungeon.getRandomPosition(1)) );            
            dungeon.addEntity( new Slime("Slime", dungeon.getRandomPosition(1)) );             
        } 
        
        // Level 2   -------------------------------------------------------------------------------------------------------------

        for (int i=1; i<=30; i++)
        {            
            dungeon.addEntity( new Bat("Bat", dungeon.getRandomPosition(2)) );             
        } 
        
        for (int i=1; i<=10; i++)
        {
            dungeon.addEntity( new Skeleton("Skeleton", dungeon.getRandomPosition(2)) ); 
        } 
        
        // Level 3   -------------------------------------------------------------------------------------------------------------
        
        for (int i=1; i<=10; i++)
        {
            dungeon.addEntity( new Ghost("Ghost", dungeon.getRandomPosition(3)) );
            dungeon.addEntity( new Skeleton("Skeleton",dungeon.getRandomPosition(3)) );
            dungeon.addEntity( new Spider("Spider", dungeon.getRandomPosition(3)) );            
            dungeon.addEntity( new Slime("Slime", dungeon.getRandomPosition(3)) );     
        }
    }

    // Add some test items.
    private static void placeItems(Dungeon dungeon)
    {
        // Level 0 -------------------------------------------------------------------------------------------------------------

        dungeon.placeItem( new Sign("Crooked Sign", "Adventure awaits! Find a key..."), 
                           new Position(17,12,0));
        
        dungeon.placeItem( new Note("Tattered Note", "Portals teleport to random areas"), 
                           new Position(87,45,0));
        
        // Key to the stairs
        Cell keystart = dungeon.getCell(new Position(87,38,0));
        MagicKey stairkey = new MagicKey("Shiny Key", new Position(56,17,0), keystart);
        keystart.placeItem(stairkey);

        // Key to the Portal Room, in a chest
        Chest portalchest = new Chest("Ornate Chest", null);
        MagicKey portalkey = new MagicKey("Glowing Key", new Position(38,41,0), portalchest);
        portalchest.setContainedItem(portalkey);
        dungeon.placeItem(portalchest, new Position(94,67,0));
        
        // Emergency Potions
        dungeon.placeItem( new Chest("Decorated Chest", new Potion(1000)), new Position(37,57,0));
        dungeon.placeItem( new Chest("Decorated Chest", new Potion(1000)), new Position(39,57,0));

        // Sign at the end
        dungeon.placeItem( new Sign("Large Sign", "Quest Complete!"), new Position(8,52,0));
        
        // Level 1  -------------------------------------------------------------------------------------------------------------        
        
        dungeon.placeItem( new Sword("Sword of Doom", 50, 20), dungeon.getRandomPosition(1) );
        dungeon.placeItem( new Shield("Large Shield", 50), dungeon.getRandomPosition(1) );
        
        for (int i=1; i<=10; i++)
        {        
            dungeon.placeItem( new Sword("Short Sword", 10, 5), dungeon.getRandomPosition(1));
            dungeon.placeItem( new Shield("Small Shield", 10), new Position(11,10,0));
        }
        
        // Level 2  -------------------------------------------------------------------------------------------------------------        
        
        for (int i=1; i<=10; i++)
        {        
            dungeon.placeItem( new Chest("Old Chest", new Sword("Long Sword", 20, 10)), dungeon.getRandomPosition(2));
            dungeon.placeItem( new Chest("Old Chest", new Shield("Shield", 20)), dungeon.getRandomPosition(2));
        }
        
        // Key to the Upstairs Room
        Cell endkeyloc = dungeon.getCell(new Position(24,6,2));
        MagicKey endkey = new MagicKey("Rusty Key", new Position(13,52,0), endkeyloc);
        endkeyloc.placeItem(endkey);
        
        // All Levels -------------------------------------------------------------------------------------------------------------        
        
        // Chests all through dungeon containing gold
        for (int i=1; i<=50; i++)
        {
            Position p = dungeon.getRandomPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            
            int gold = JavaTools.generator.nextInt(100);            
            dungeon.placeItem( new Chest("Chest", gold), p);
        }
        
        // Chests all through dungeon containing potions
        for (int i=1; i<=20; i++)
        {
            Position p = dungeon.getRandomPosition();
            if (p.z == 0) p.z++;  // Not on starting level
        
            int health = JavaTools.generator.nextInt(100);            
            dungeon.placeItem( new Chest("Chest", new Potion(health)), p);
        }
        
        // Empty chests to keep life interesting
        for (int i=1; i<=30; i++)
        {
            Position p = dungeon.getRandomPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            dungeon.placeItem( new Chest("Chest", null), p);
        }
    }
}