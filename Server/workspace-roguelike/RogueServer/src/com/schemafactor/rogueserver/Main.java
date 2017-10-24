package com.schemafactor.rogueserver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Position;
import com.schemafactor.rogueserver.entities.Spider;
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
        JavaTools.printlnTime("Rogue Server Version " + Constants.VERSION );
        
        JavaTools.onlyOneInstance("rogueserver");
        
        // Create the universe.
        JavaTools.printlnTime("Creating game universe...");
        Dungeon.getInstance().Create(Constants.DUNGEON_SIZE, Constants.DUNGEON_DEPTH);
        
        // Load saved
        JavaTools.printlnTime("Loading game persistence...");
        try 
        {
			Dungeon.getInstance().Load("C:/Leif/GitHub/MultiRogueLike/Server/data/test/LevelTest0.csv", 0);
		} 
        catch (FileNotFoundException e) 
        {
        	JavaTools.printlnTime("FileNotFoundException!");
			e.printStackTrace();
			System.exit(0);
		}
        
        // Add some entities.
        JavaTools.printlnTime("Creating default entities...");
        
        // For now, add some spiders        
        Dungeon.getInstance().addEntity( new Spider("Spider Fred", new Position(90,20,0)) );
        Dungeon.getInstance().addEntity( new Spider("Spider Mike", new Position(7,7,0)) );
        
        /*
        for (int i=1; i<=Constants.ASTEROID_COUNT; i++)
        {
            allEntities.add(new Asteroid("Asteroid #" + i, JavaTools.generator.nextInt((int)Universe.getInstance().getXsize()),
                                                           JavaTools.generator.nextInt((int)Universe.getInstance().getYsize()) ));                    
        } 
        */     
        
        
        // A mini http server to show stats through a browser
        //JavaTools.printlnTime("Creating debug httpd server...");
        //VortexDebugServer vdbg = new VortexDebugServer(80);
                
        // Start the thread that updates everything at a fixed interval
        JavaTools.printlnTime("Creating update scheduler...");
        UpdaterThread ut = new UpdaterThread();        
        ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
        s.scheduleAtFixedRate(ut, 0, Constants.TICK_TIME, TimeUnit.MILLISECONDS );      
        
        // Instantiate a UDP listener, and let it take over.
        JavaTools.printlnTime("Creating UDP listener...");
        UDPListener udp = new UDPListener();
        udp.start(3006);
    }
}
