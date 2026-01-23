package com.schemafactor.rogueserver;

import java.text.DecimalFormat;

/*
 * UpdaterThread.java
 *
 * Updates game state for all players.
 */

import java.util.ArrayList;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;

/**
 * @author Leif Bloomquist
 */
public class UpdaterThread implements Runnable
{   
    private Dungeon dungeon = null;    
    
    // Some run-time statistics for monitoring
    JavaTools.MovingAverage sma_ms = new JavaTools.MovingAverage(100);
    JavaTools.MovingAverage sma_cpu = new JavaTools.MovingAverage(100);
    static public double avg_ms = 0d;
    static public double avg_cpu = 0d;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    long reportcounter = 0;
     
    /** Creates a new instance of UpdaterThread */
    public UpdaterThread()
    {
        // Save references       
        dungeon = Dungeon.getInstance();        
    }                   
    
    /** Main updating thread (called from ScheduledThreadPoolExecutor in main(). */
    public void run()                       
    {
        Thread.currentThread().setName("Rogue Server Updater Thread");
        
        long startTime = System.nanoTime();
        List<Entity> entities = dungeon.getEntities();
        
        // 1. Update the dungeon.
        try
        {
            dungeon.update();
        }
        catch (Exception e)
        {               
            JavaTools.printlnTime( "EXCEPTION Updating Dungeon: " + JavaTools.getStackTrace(e) );
        } 
        
        // 2. Have each each entity take action and then update, if required                  
        try
        {
            synchronized (entities)
            {
            	// 2a. All Entities - take action (no updates)
            	
            	for (Entity e : entities)
	            {             
	                e.action(); 
	            }
            	
            	// 2b. If the actions resulted in updates, send them
            	
	        	for (Entity e : entities)
	            {             
	                e.update(); 
	            }
            }
        }
        catch (Exception ex)
        {               
            JavaTools.printlnTime("EXCEPTION Updating Entities: " + JavaTools.getStackTrace(ex) );
        }   
                
        // 3. Remove any entities that are flagged to be removed
        try
        {
            List<Entity> toBeRemoved = new ArrayList<Entity>();
            
            synchronized (dungeon.getEntities())
            {
	            for (Entity e : dungeon.getEntities())
	            { 
	               if (e.getRemoved())
	               {
	                   toBeRemoved.add(e);
	               }
	            }            
	            dungeon.getEntities().removeAll(toBeRemoved);
            }
        }
        catch (Exception ex)
        {               
            JavaTools.printlnTime("EXCEPTION removing entities: " + JavaTools.getStackTrace(ex) );
        }                          
        
        // 4. Add any new entities
        
           

        // 5. Gather some stats (read out in httpd server)
        long estimatedTime = System.nanoTime() - startTime;  
        double estimatedMilliseconds = estimatedTime/1000000d;
        sma_ms.newNum(estimatedMilliseconds);
        sma_cpu.newNum(estimatedMilliseconds / Constants.TICK_TIME);
        avg_ms = sma_ms.getAvg();
        avg_cpu = sma_cpu.getAvg()*100.0;  // %
        
        // CPU usage stats
        reportcounter += Constants.TICK_TIME;
        
        if (reportcounter >= 100000)  // 100 seconds
        {
        	if (avg_cpu > 2.0) // Over 2% considered "high" so log it
        	{
        		JavaTools.printlnTime("Average Update Time [ms]: " + df.format(avg_ms) + " | Average CPU Usage [%]: " + df.format(avg_cpu) + " | Current Player Count: " + dungeon.getNumPlayers() );
        	}
        	reportcounter=0;
        }
    }
}