package com.schemafactor.rogueserver;

/*
 * UpdaterThread.java
 *
 * Updates game state for all players.
 */

import java.util.ArrayList;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

/**
 * @author Leif Bloomquist
 */
public class UpdaterThread implements Runnable
{   
    private Dungeon dungeon = null;    
    
    JavaTools.MovingAverage sma_ms = new JavaTools.MovingAverage(100);
    JavaTools.MovingAverage sma_cpu = new JavaTools.MovingAverage(100);
     
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
        
        // 1. Update the universe/dungeon.
        try
        {
            dungeon.update();
        }
        catch (Exception e)
        {               
            JavaTools.printlnTime( "EXCEPTION Updating Dungeon: " + JavaTools.getStackTrace(e) );
        }   
        
        
        // 2. Update each entity                  
        try
        {
            synchronized (dungeon.getEntities())
            {
	        	for (Entity e : dungeon.getEntities())
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
	               if (e.removeMe())
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
        Main.avg_ms = sma_ms.getAvg();
        Main.avg_cpu = sma_cpu.getAvg();
    }
}