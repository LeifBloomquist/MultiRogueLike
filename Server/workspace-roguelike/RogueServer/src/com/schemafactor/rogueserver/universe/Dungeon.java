package com.schemafactor.rogueserver.universe;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.DummyEntity;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;
import com.schemafactor.rogueserver.entities.Position;

public class Dungeon 
{
    // Dungeon is a Singleton
    private static final Dungeon instance = new Dungeon();
 
    private Dungeon() {}
 
    public static Dungeon getInstance() 
    {
        return instance;
    }
        
    private Cell[][][] dungeonMapCells = null;   // Matrix of cells
    
    private int Xsize = -1;      // Cells
    private int Ysize = -1;      // Cells
    private int Zsize = -1;      // Levels
    
    List<Entity> allEntities = null;
    
    /**
     * 
     * @param size
     * @param allEntities
     */
    public void Create(int size, int depth)
    {
        // Create the entities
        allEntities = Collections.synchronizedList(new ArrayList<Entity>());
        
        // Create array        
        dungeonMapCells = new Cell[size][size][depth];   
        
        // Instantiate
        for (int x=0; x < size; x++)
        {
            for (int y=0; y < size; y++)
            {
            	 for (int z=0; z < depth; z++)
                 {
            	     dungeonMapCells[x][y][z] = new Cell();
                 }
            }
        }
                
        Xsize = size;
        Ysize = size;
        Zsize = depth;
    }
    
    public void Load(String filename, int level) throws FileNotFoundException
    {
    	ArrayList<String> temp_chars = new ArrayList<String>();
    	    	
    	Scanner scanner = new Scanner(new File(filename));
        scanner.useDelimiter(",|\r\n");
        
        while(scanner.hasNext())
        {
        	temp_chars.add(scanner.next());
        }
        scanner.close();
        
        JavaTools.printlnTime( "Number of cells loaded: " + temp_chars.size() );
       
        int index = 0;
           
        for (int yy=0; yy < Constants.DUNGEON_SIZE; yy++)
        {
            for (int xx=0; xx < Constants.DUNGEON_SIZE; xx++)
            {            
               Cell c = dungeonMapCells[xx][yy][level];
               c.setAttributes( Integer.parseInt(temp_chars.get(index++)) );
            }
        }
    }
    
    public void update()
    {
    	// Do timed animation here?
    }
    
    /** Get a full screen's worth of cell data, with out of range replaced by 0 for screen edges*/
    private byte[] getScreen(Position topleft)
    {
    	byte[] screen = new byte[Constants.SCREEN_SIZE];        
        int index=0;
        Position temp_pos = new Position(topleft);
        
        // 1.  Lowest Layer - Map
        
        for (int yy=0; yy < Constants.SCREEN_HEIGHT; yy++)
        {
        	temp_pos.y = topleft.y+yy;
            for (int xx=0; xx < Constants.SCREEN_WIDTH; xx++)
            {  
            	temp_pos.x = topleft.x+xx;
                screen[index++] = getCharCode(temp_pos);
            }
        }
        
        // 2. Next layer - Objects
        // TODO
        
        // 3.  Top layer - Entities
        
        List<Entity> onscreen = getEntitiesOnScreen(topleft);
        
        for (Entity e : onscreen)
        {
        	int rel_x = e.getXpos() - topleft.x;
        	int rel_y = e.getYpos() - topleft.y;
        	
        	try
        	{     	
        	    screen[(rel_y * Constants.SCREEN_WIDTH) + rel_x] = e.getCharCode();
        	}
        	catch (Exception ex)
        	{
        	    ;        	
        	}
        }        
        
        return screen;
    }   
    
    public byte[] getScreenCentered(Position center)
    {
        Position topleft = new Position(center);
        topleft.x -= (int)(Constants.SCREEN_WIDTH / 2);
        topleft.y -= (int)(Constants.SCREEN_HEIGHT / 2);
        return getScreen(topleft);
    }   

	public Cell getCell(Position p)
    {
    	try
    	{
    		return dungeonMapCells[p.x][p.y][p.z];
    	}
    	catch (Exception e) // all, usually out of range
    	{
    		return null;
    	}
    }
    
    private byte getCharCode(Position p)
    {
    	Cell c = getCell(p);
    	
    	if (c == null)
    	{
    		return 0;
    	}
    	return c.getCharCode();
    }

    public long getXsize() 
    {
        return Xsize;
    }

    public long getYsize() 
    {
        return Ysize;
    }  
    
    // For the online map
    public Color getCellColor(int x, int y, int z)
    {
        byte cbmcolor = dungeonMapCells[x][y][z].getCharColor();
        
        Color col = Color.BLACK;
        
        switch (cbmcolor)
        {            
            // C64 colors
            case Constants.COLOR_BLACK:      col = Color.BLACK;  break;
            case Constants.COLOR_WHITE:      col = Color.WHITE;  break;     
            case Constants.COLOR_RED:        col = Color.RED;  break;       
            case Constants.COLOR_CYAN:       col = Color.CYAN;  break;      
            case Constants.COLOR_PURPLE:     col = Color.MAGENTA;  break;    
            case Constants.COLOR_GREEN:      col = Color.GREEN;  break;     
            case Constants.COLOR_BLUE:       col = Color.BLUE;  break;      
            case Constants.COLOR_YELLOW:     col = Color.YELLOW;  break;    
            case Constants.COLOR_ORANGE:     col = Color.ORANGE;  break;    
            case Constants.COLOR_BROWN:      col = new Color(150, 75, 0);  break;     
            case Constants.COLOR_LIGHTRED:   col = Color.PINK;  break;  
            case Constants.COLOR_GREY1:      col = Color.DARK_GRAY;  break;     
            case Constants.COLOR_GREY2:      col = Color.GRAY;  break;     
            case Constants.COLOR_LIGHTGREEN: col = new Color(100, 255, 100);  break;
            case Constants.COLOR_LIGHTBLUE:  col = new Color(100, 100, 255);  break; 
            case Constants.COLOR_GREY3:      col = Color.LIGHT_GRAY;  break;     
            default:                         col = Color.BLACK; break;        
        }
        
        return col;        
    }

    public List<Entity> getEntities()
    {
        return allEntities;
    }
    
    /** Get a list of all entities matching the given type, excluding the one who is doing the inquiry (who). */
    public List<Entity> getEntities(Entity who, Entity.entityTypes type)
    {
        List<Entity> allOfType = new ArrayList<Entity>();
        
        for (Entity e : allEntities)
        {
            if (who == e) continue;
            
            if ((e.getType() == type) && !(e.removeMe()))
            {
                allOfType.add(e);                        
            }
        }        
        
        return allOfType;
    }
    
    /** Get a list of all entities within a certain radius, excluding the one who is doing the inquiry (who). */
    public List<Entity> getEntitiesRange(Entity who, double range)
    {
        List<Entity> allInRange = new ArrayList<Entity>();
        
        for (Entity e : allEntities)
        {
            if (who == e) continue;
            
            if ((who.distanceTo(e) <= range)  && !(e.removeMe()))
            {
                allInRange.add(e);                        
            }
        }        
        
        return allInRange;
    }
    
    /** Get a list of all entities within a certain radius of a position */
    private List<Entity> getEntitiesRange(Position pos, double range)
    {
        DummyEntity dummy = new DummyEntity(pos);
        return getEntitiesRange(dummy, range);       
    }
    
    
    // Get list of entities visible on screen from this top left coordinate
    private List<Entity> getEntitiesOnScreen(Position topleft)
    {
        List<Entity> allOnScreen = new ArrayList<Entity>();
    	 
	    for (Entity e : allEntities)
	    {
	        int dx = e.getXpos() - topleft.x;
	        int dy = e.getYpos() - topleft.y;
	        
	        if ( (dx < Constants.SCREEN_WIDTH ) && (dx >= 0) && 
                 (dy < Constants.SCREEN_HEIGHT) && (dy >= 0) 
               )
	        {
	        	allOnScreen.add(e); 
	        }
	    }
	    return allOnScreen;        
	}
     
    public List<Entity> getEntitiesOnScreenCentered(Position center)
    {
        Position topleft = new Position(center);
        topleft.x -= (int)(Constants.SCREEN_WIDTH / 2);
        topleft.y -= (int)(Constants.SCREEN_HEIGHT / 2);
        return getEntitiesOnScreen(topleft);
    }
    
    public void addEntity(Entity who)
    {
    	 try  
         {
         	synchronized(allEntities)
         	{
         		allEntities.add(who);
         	}         	
         }
    	 catch (Exception ex)
         {               
             JavaTools.printlnTime("EXCEPTION adding new entity " + who.getDescription() +": " + JavaTools.getStackTrace(ex) );
         }        	 
    }
}
