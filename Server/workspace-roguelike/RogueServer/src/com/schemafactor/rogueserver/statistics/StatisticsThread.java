package com.schemafactor.rogueserver.statistics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

/*
 * StatisticsThread.java
 *
 * Updates game statistics and writes to temporary files
 */

import java.util.List;

import javax.imageio.ImageIO;

import com.schemafactor.rogueserver.UpdaterThread;
import com.schemafactor.rogueserver.common.CellColours;
import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;

/**
 * @author Leif Bloomquist
 */
public class StatisticsThread implements Runnable
{   
    private Dungeon dungeon = null;
    private UpdaterThread updater = null;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    
//  private static String statsPrefix - "/run/rogue/stats/";    // TODO make this a config item
    private static String statsPrefix = "C:\\rogue\\stats\\";
    private static int PIXELSPERCELL = 2;
     
    /** Creates a new instance of StatisticsThread */
    public StatisticsThread(UpdaterThread ut)
    {
        // Save references       
        dungeon = Dungeon.getInstance();
        updater = ut;
    }                   
    
    /** Main updating thread (called from ScheduledThreadPoolExecutor in main(). */
    public void run()                       
    {
        Thread.currentThread().setName("Rogue Server Statistics Thread");
        
        writeStats();
        writeMaps();
    }
    
    public void writeStats()
    {        
        String msg = "<html><head><title>Rogue Server Statistics</title></head>" +               
                "<body><h2>Rogue Server Version " + Constants.VERSION + "</h2>\n" +
                "Server time: " + JavaTools.GetCurrentDateTimeStamp() +
                "<hr><p>";
        
        msg += "<h2>CPU Usage</h2>\n";
        msg += "Average Update Time [ms]: " + df.format(updater.avg_ms) + "&nbsp;&nbsp;&nbsp; Average CPU Usage [%]: " + df.format(updater.avg_cpu);

        msg += getPlayers();

        msg += "</body></html>\n";
        msg += getRefresh();

        // Write the file
        Path filePath = Path.of(statsPrefix + "index.html"); 
        
        try 
        {
            Files.writeString(filePath, msg);
        } 
        catch (IOException e) 
        {
        	 JavaTools.printlnTime("EXCEPTION writing stats file: " + e.getMessage());
        }
    }
    
    public void writeMaps()
    {    	
    	for (int l=0; l < dungeon.getZsize(); l++)
        {
        	writeMapImage(l);
        }
    	
        String msg = "<html><head><title>Rogue Server Debug Maps</title></head>" +               
                "<body><h2>Rogue Server Version " + Constants.VERSION + "</h2>\n" +      
                "Server time: " + JavaTools.GetCurrentDateTimeStamp() +
                "<hr><p>";
        
        msg += "<table><th><tr>";
        for (int l=0; l < dungeon.getZsize(); l++)
        {
        	msg += "<th> Level " + l + "</th>";
        }
        
        msg += "</tr></th><tr>";
        
        for (int l=0; l < dungeon.getZsize(); l++)
        {
        	msg += "<td><img src=\"level" + l + ".png\"</td>";
        }
        
        msg += "</tr></th><tr>";

        msg += "</body></html>\n";
        msg += getRefresh();

        // Write the file
        Path filePath = Path.of(statsPrefix + "maps.html");        
        
        try 
        {
            Files.writeString(filePath, msg);
        } 
        catch (IOException e) 
        {
        	 JavaTools.printlnTime("EXCEPTION writing stats file: " + e.getMessage());
        }
    }    
    
    private String getPlayers()
    { 
        List<Entity> allPlayers = dungeon.getEntities(null, entityTypes.CLIENT);
        
        String msg = "<h2>Current Players (" + allPlayers.size() + " total)</h2>";
        
        msg += "<table border=\"1\">" +
               "<tr><th>Player Name</th><th>Location X</th><th>Location Y<th>Location Z</th></th></tr>";
        
        for (Entity e : allPlayers)
        {
            msg += "<tr><td>" + e.getDescription() + "</td><td>" + e.getPosition().x + "</td><td>" + e.getPosition().y + "</td><td>" + e.getPosition().z +"</td></tr>";
        }
        
        msg += "</table>";
        return msg;
    }
    
    private void writeMapImage(int level) 
    {
    	BufferedImage map_Image = new BufferedImage(dungeon.getXsize()*PIXELSPERCELL, dungeon.getYsize()*PIXELSPERCELL, BufferedImage.TYPE_INT_RGB);        
    	Graphics2D gO = map_Image.createGraphics();        

		// 1. Plot the map
    	try
    	{
    		for (int x=0; x<dungeon.getXsize()*PIXELSPERCELL; x++)
    		{
    			for (int y=0; y<dungeon.getYsize()*PIXELSPERCELL; y++)
    			{
    				int charcode = dungeon.getCell(x/PIXELSPERCELL, y/PIXELSPERCELL, level).getCharCode();
    				Color col = CellColours.getCellColour(charcode & 0xFF);
    			 	map_Image.setRGB(x, y, col.getRGB() );
    			}
    		}    		
    	}
    	catch (Exception e) 
		{           
			JavaTools.printlnTime("EXCEPTION creating map image: " + e.getMessage());
			return;
		}
    	    	
    	
		//for (int x=0; x<dungeon.getXsize()/Constants.; x++)
		//{
		//for (int y=0; y<dungeon.getYsize(); y++)
		//{
	//	map_Image.setRGB( x, y, universe.getCellColor(x, y).getRGB() );                         
	//	}
	//	}
    	

    	// 2. Add entity labels to map?

		// Copy list to get around the dreaded Concurrent modification exception  (shallow copy)
		// List<Entity> entitiesCopy = new ArrayList<Entity>(dungeon.getEntities());     

		//for (Entity e : entitiesCopy)
		//{
		//	gO.setColor( e.getRGBColor() );
		//	gO.fillOval( (int)e.getXcell(), (int) e.getYcell(), 10, 10);
		//	gO.drawString( e.getDescription(), (int) e.getXcell() + 15, (int) e.getYcell() );                   
		//}

		// Clean up graphics
		gO.dispose();

		// 3. Convert to PNG and save		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try 
		{
			ImageIO.write(map_Image, "png", baos);
			OutputStream outputStream = new FileOutputStream(statsPrefix + "level" + level + ".png");
			baos.writeTo(outputStream);
		} 
		catch (IOException e) 
		{           
			JavaTools.printlnTime("EXCEPTION writing map file: " + e.getMessage());
		}
    }    
    
    private String getRefresh()
    { 
    	//return "\n<script> function autoRefresh() { window.location = window.location.href; } setInterval('autoRefresh()', " + Constants.STATS_UPDATE_TIME + "); </script>\n";
    	return "\n  <script type=\"text/javascript\">        setInterval(abc, 1000);        function abc() {        var xhttp = new XMLHttpRequest();        xhttp.onreadystatechange = function() {        if (this.readyState == 4 && this.status == 200) {        document.getElementById(\"container\").innerHTML = this.responseText;        }        };        xhttp.open(\"GET\", \"notify.asp\", true);        xhttp.send();        }    </script>     <body id=\"container\" onload=\"javascript:abc()\">";
    }
    
    
}
