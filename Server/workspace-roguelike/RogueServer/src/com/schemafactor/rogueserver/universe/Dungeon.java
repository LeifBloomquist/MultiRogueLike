package com.schemafactor.rogueserver.universe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Position;
import com.schemafactor.rogueserver.universe.Cell;

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
     * Thread-safe queue of new entities that have been created (and added to the allEntities list)
     */
    public BlockingQueue<Entity> newEntities = new ArrayBlockingQueue<Entity>(1000);
    
    /**
     * 
     * @param size
     * @param allEntities
     */
    public void Create(int size, int depth, List<Entity> allEntities)
    {
        // Save the entities
        this.allEntities = allEntities;
        
        // Create array        
        dungeonMapCells = new Cell[depth][size][size];   
        
        // Instantiate
        for (int x=0; x < size; x++)
        {
            for (int y=0; y < size; y++)
            {
            	 for (int z=0; z < depth; z++)
                 {
            	     dungeonMapCells[z][x][y] = new Cell();
                 }
            }
        }
                
        Xsize = size;
        Ysize = size;
        Zsize = depth;

        // Put a special marker at the origin.  On each level?
        dungeonMapCells[0][0][0].setAttributes(100);
    }
    
    public void update()
    {
    	// Do timed animation here?
    }
    
    /** Get a full screen's worth of cell data, with wraparound */
    public byte[] getScreen(Position pos)
    {
    	byte[] screen = new byte[Constants.SCREEN_SIZE];
        
        int index=0;
        
        // Faster way to do this?
        for (int yy=0; yy < Constants.SCREEN_HEIGHT; yy++)
        {
            for (int xx=0; xx < Constants.SCREEN_WIDTH; xx++)
            {            
               Cell c = (Cell)JavaTools.getArrayWrap(dungeonMapCells[pos.z], pos.x+xx, pos.y+yy);
               screen[index] = c.getCharCode();
               index++;
            }
        }
        
        return screen;
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
    public List<Entity> getEntities(Entity who, double range)
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
}
