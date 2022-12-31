package com.schemafactor.rogueserver.dungeon;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import com.schemafactor.rogueserver.Spawner;
import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.entities.DummyEntity;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.items.Chest;
import com.schemafactor.rogueserver.items.Item;

public class Dungeon implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

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
    
    // All the entities in the dungeon
    private List<Entity> allEntities = null;
    
    // An array of lists of positions
    private ArrayList<Position>[] emptyCells = null;
    
    // A dummy entity needed for some behind-the-scenes calls
    DummyEntity dummy = new DummyEntity();
    
    /**
     * 
     * @param size
     * @param allEntities
     */
    @SuppressWarnings("unchecked") // for emptyCells
	public void Create(int size, int depth)
    {
        // Create the entities
        allEntities = Collections.synchronizedList(new ArrayList<Entity>());
        
        // Create arrays      
        dungeonMapCells = new Cell[size][size][depth];
        emptyCells = new ArrayList[depth];
        
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

    public void LoadTXT(String filename, int level) throws FileNotFoundException
    {
        ArrayList<String> lines = new ArrayList<String>();
                
        Scanner scanner = new Scanner(new File(filename));
        scanner.useDelimiter("\r\n");
        
        while(scanner.hasNext())
        {
            lines.add(scanner.next());
        }
        scanner.close();
        
        JavaTools.printlnTime( "Level " + level + " number of lines loaded: " + lines.size() );
           
        for (int y=0; y < lines.size(); y++)
        {   
           char[] line=lines.get(y).toCharArray();
           
           for (int x=0; x < line.length; x++)
           {  
               Cell cell = dungeonMapCells[x][y][level];               
               int charcode = 0;
               
               switch (line[x])
               {
                   case '.': 
                       charcode = Constants.CHAR_DIRT;
                       break;
                       
                   case '#': 
                       charcode = Constants.CHAR_BRICKWALL;
                       break;
                       
                   case ' ': 
                       charcode = Constants.CHAR_EMPTY;
                       break;
                       
                   case 'c':    // Hardcoded chests - this will be phased out
                       charcode = Constants.CHAR_EMPTY;
                       cell.placeItem( new Chest("Chest", null) );
                       break;
                       
                   case '^': 
                       charcode = Constants.CHAR_STAIRS_UP;
                       break;
                       
                   case 'v':
                   case 'V': 
                       charcode = Constants.CHAR_STAIRS_DOWN;
                       break;

                   case 'd': 
                       charcode = Constants.CHAR_DOOR_CLOSED;
                       break;

                   case 'O': 
                       charcode = Constants.CHAR_PORTAL;
                       break;

                   case 'l': 
                       charcode = Constants.CHAR_LAVA;
                       break;

                   case 'w': 
                       charcode = Constants.CHAR_WATER;
                       break;
                       
                   case 's': 
                       charcode = Constants.CHAR_SECRET_DOOR;
                       break;
                       
                   case ':': 
                       charcode = Constants.CHAR_DEBRIS;
                       break;
                       
                   default:
                       charcode = '?';
                       break;                   
               }
           
               cell.setAttributes( charcode );
           }
        }
    }    
    
    
    public void update()
    {
        Spawner.respawn(this);
        
    	// Do timed animation here?
    }
    
    /** Get a full screen's worth of cell data, with out of range replaced by "dirt" for screen edges*/
    private byte[] getScreen(Position topleft)
    {
    	byte[] screen = new byte[Constants.SCREEN_SIZE];        
        int index=0;
        Position temp_pos = new Position(topleft);
        
        // Returns cell character codes, replaced by item or Entity codes if applicable
        
        for (int yy=0; yy < Constants.SCREEN_HEIGHT; yy++)
        {
        	temp_pos.y = topleft.y+yy;
            for (int xx=0; xx < Constants.SCREEN_WIDTH; xx++)
            {  
            	temp_pos.x = topleft.x+xx;
                screen[index++] = getCharCode(temp_pos);
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
	
	public Position getClosestEmptyCell(Position start)
    {
		if (start==null) return null;
		
        // Easiest case - Starting cell	    
	    try
	    {
	        if (dungeonMapCells[start.x][start.y][start.z].isEmpty(false))
            {
                return start;
            }
	    }
	    catch (ArrayIndexOutOfBoundsException aioobe)  // Out of map bounds
	    {
	        return null;
	    }
	    
	    // Check squares fanning outwards	    
	    for (int size=1; size < Constants.EMPTY_CELL_SEARCH_DEPTH; size++ )
	    {
            List<Position> square = getCellsSquare(start, size);        
            Collections.shuffle(square);   // So it's not always upper-right first            
            
            for (Position pos : square)
            {
	        	if (dungeonMapCells[pos.x][pos.y][pos.z].isEmpty(false))
	            {
	                return pos;
	            }
            }
            
            // If still no matches, fan further outwards on next loop
        }
        
        return null;  // No empty cells found
    }
	
	// This is depth-first...use breadth-first above for truly closest cell
	@Deprecated
	private Position getClosestEmptyCellDFS(Position start, int depth)
    {        
        // Easiest case - Starting cell
	    
	    try
	    {
	        if (dungeonMapCells[start.x][start.y][start.z].isEmpty(false))
            {
                return start;
            }
	    }
	    catch (ArrayIndexOutOfBoundsException aioobe)  // Out of map bounds
	    {
	        return null;
	    }
    
        depth--;
        if (depth <= 0)   // End of search depth reached
        {
            return null;
        }
    
        // Recursively test all cells around
        List<Position> neighbors = getNeighborCells(start);        
        Collections.shuffle(neighbors);   // So it's not always upper-right first      
    
        for (Position pos : neighbors)
        {
            Position result = getClosestEmptyCellDFS(pos, depth);
    
            if (result != null)
            {
                return result;
            }
        }

        return null;  // No empty cells found
    }

    // Get neighbors on same level, also tests boundaries.  This is essentially the same as getCellsSquare() with size=1 (TODO consolidate)
    public List<Position> getNeighborCells(Position center)
    {
        List<Position> neighbors = new ArrayList<Position>();
    
        if ((center.y > 0) && (center.x > 0))
        {
            neighbors.add( new Position(center.x - 1, center.y-1, center.z) );
        }
    
        if ((center.y > 0))
        {
            neighbors.add( new Position(center.x, center.y-1, center.z) );
        }
    
        if ((center.y > 0) && (center.x < Constants.DUNGEON_SIZE))
        {
            neighbors.add( new Position(center.x+1, center.y-1, center.z) );
        }
    
        if ((center.x > 0))
        {
            neighbors.add( new Position(center.x-1, center.y, center.z) );
        }
    
        if ((center.x < Constants.DUNGEON_SIZE))
        {
            neighbors.add( new Position(center.x+1, center.y, center.z) );
        }
    
        if ((center.y < Constants.DUNGEON_SIZE) && (center.x > 0))
        {
            neighbors.add( new Position(center.x-1, center.y+1, center.z) );
        }
    
        if ((center.y < Constants.DUNGEON_SIZE))
        {
            neighbors.add( new Position(center.x, center.y+1, center.z) );
        }
    
        if ((center.y < Constants.DUNGEON_SIZE) && (center.x < Constants.DUNGEON_SIZE))
        {
            neighbors.add( new Position(center.x+1, center.y+1, center.z) );
        }
    
        return neighbors;
    }
    
    // Get cells "size" cells away from the center (for crude BFS)
    private List<Position> getCellsSquare(Position center, int size)
    {
        List<Position> square = new ArrayList<Position>();
        
        int startx = Math.max(0,center.x-size);
        int endx = Math.min(center.x+size, this.Xsize-1);
        int starty = Math.max(0,center.y-size);
        int endy = Math.min(center.y+size, this.Ysize-1);
        
        // Top
        for (int x=startx; x < endx; x++)
        {
        	square.add( new Position(x, starty, center.z) );
        }
        
        // Bottom
        for (int x=startx; x < endx; x++)
        {
        	square.add( new Position(x, endy, center.z) );
        }
        
        // Left (note +1 since first cell added as part of Top)
        for (int y=starty+1; y < endy; y++)
        {
        	square.add( new Position(startx, y, center.z) );
        }
        
        // Right (note <= to get last cell)
        for (int y=starty; y <= endy; y++)
        {
        	square.add( new Position(endx, y, center.z) );
        }
        
        return square;
    }
    
    private byte getCharCode(Position p)
    {
    	Cell c = getCell(p);
    	
    	// Invalid position - return "Dirt" (for area outside the dungeon)
    	if (c == null)
    	{
    		return Constants.CHAR_DIRT;
    	}
    	
    	// Return the character code, could be cell base code or item code or Entity Code
    	return c.getCharCode();
    }

    public int getXsize() 
    {
        return Xsize;
    }

    public int getYsize() 
    {
        return Ysize;
    }  
    
    public int getZsize() 
    {
        return Zsize;
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
    
    public int getNumEntities()
    {
        if (allEntities != null)
        {
            return allEntities.size();
        }
        else
        {
            return 0;
        }
    }   
    
    public int getNumPlayers()
    {
        List<Entity> players = getEntities(null, Entity.entityTypes.CLIENT);
            
        if (players != null)
        {
            return players.size();
        }
        else
        {
            return 0;
        }
    }   
    
    /** Get a list of all entities matching the given type, excluding the one who is doing the inquiry (who). */
    public List<Entity> getEntities(Entity who, Entity.entityTypes type)
    {
        return getEntitiesType(who, type, allEntities);
    }
    
    /** Get a sublist of all entities matching the given type, excluding the one who is doing the inquiry (who). */
    public List<Entity> getEntitiesType(Entity who, Entity.entityTypes type, List<Entity> entities)
    {   
        List<Entity> matching = new ArrayList<Entity>();
        
        for (Entity e : entities)
        {
            if (who == e) continue;
            if (e.getRemoved()) continue;
            
            if (e.getType() == type)
            {
                matching.add(e);                        
            }
        }        
        
        return matching;
    }
    
    /** Get a list of all entities within a certain radius, excluding the one who is doing the inquiry (who). */
    public List<Entity> getEntitiesRange(Entity who, double range)
    {
        List<Entity> allInRange = new ArrayList<Entity>();
        
        for (Entity e : allEntities)
        {
            if (who == e) continue;
            if (e.getRemoved()) continue;
            if (e.isInvisible ) 
        	{
            	continue;
        	}
            
            if (who.distanceTo(e) <= range)
            {
                allInRange.add(e);                        
            }
        }
        
        return allInRange;
    }
        
    // Get list of entities on screen from this top left coordinate  (even if invisible)
    private List<Entity> getEntitiesOnScreen(Position topleft)
    {
        List<Entity> allOnScreen = new ArrayList<Entity>();
    	 
	    for (Entity e : allEntities)
	    {
	        // Skip entities on other levels
	        if (topleft.z != e.getPosition().z)
	        {
	            continue;
	        }
	        
	        Position ep = e.getPosition(); 
	        
	        int dx = ep.x - topleft.x;
	        int dy = ep.y - topleft.y;
	        
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
         		if (!allEntities.contains(who))
         		{
         		    allEntities.add(who);
         		}
         	}         	
         }
    	 catch (Exception ex)
         {               
             JavaTools.printlnTime("EXCEPTION adding new entity " + who.getDescription() +": " + JavaTools.getStackTrace(ex) );
         }        	 
    }
    
    public void placeItem(Item i, Position start)
    {
        Position place = getClosestEmptyCell(start);
        
        if (place != null) 
        {
        	getCell(place).placeItem(i);
        }        
    }
    
    public void determineEmptyCells() 
    {	
    	for (int z=0; z < getZsize(); z++)
    	{
     		emptyCells[z] = new ArrayList<Position>();
    				
    		for (int x=0; x < getXsize(); x++)
    		{
    			for (int y=0; y < getYsize(); y++)
                 {
            	     if (dungeonMapCells[x][y][z].isEmpty(false))
    	    		 {
            	    	 emptyCells[z].add(new Position(x, y, z));    	    	  
    	    		 }
                 }
            }
        }
	}    
    
    public Position getRandomEmptyPosition()
    {
        int z=JavaTools.generator.nextInt(getZsize());
        return getRandomEmptyPosition(z);
    }
    
    public Position getRandomEmptyPosition(int z)
    {
    	for (int i=0; i< Constants.EMPTY_CELL_SEARCH_DEPTH; i++ )
    	{
    		Position p = emptyCells[z].get(JavaTools.generator.nextInt(emptyCells[z].size()));
    		
    	    if (getCell(p).canEnter(dummy))
    	    {
    			return p;
    		}
    	}
    	
    	return null;
    }
    
    public Position getRandomPosition()
    {
    	return getRandomPosition(JavaTools.generator.nextInt(getZsize()));
    }    
    
    public Position getRandomPosition(int z)
    {
        Position p = new Position( 
                JavaTools.generator.nextInt(getXsize()),  
                JavaTools.generator.nextInt(getYsize()),
                z);
        return p;
    }	
}
