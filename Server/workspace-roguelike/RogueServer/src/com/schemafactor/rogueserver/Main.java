package com.schemafactor.rogueserver;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Position;
import com.schemafactor.rogueserver.entities.Spider;
import com.schemafactor.rogueserver.network.UDPListener;
import com.schemafactor.rogueserver.universe.Dungeon;
import com.schemafactor.rogueserver.UpdaterThread;

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
        
        // ArrayList of all entities.
        ArrayList<Entity> allEntities = new ArrayList<Entity>();               
        
        // Create the universe.
        JavaTools.printlnTime("Creating game universe...");
        Dungeon.getInstance().Create(Constants.DUNGEON_SIZE, Constants.DUNGEON_DEPTH, allEntities);
        
        // Add some entities.
        JavaTools.printlnTime("Creating default entities...");
        
        // For now, add a single spider.
        Spider spider = new Spider("Spider Fred", new Position(10,10,0));
        allEntities.add(spider);
        
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
