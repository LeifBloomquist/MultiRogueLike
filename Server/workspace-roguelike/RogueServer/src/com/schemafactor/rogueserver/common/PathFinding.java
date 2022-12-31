package com.schemafactor.rogueserver.common;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;

import com.schemafactor.rogueserver.dungeon.Dungeon;


public class PathFinding 
{
	Queue<Point> empty = null;
    
    // Variables used to track the number of steps taken
    int nodes_left_in_layer = 1;
    int nodes_in_next_layer = 0;
    
    // Matrix of values to track whether the nodes (cells) have been visited 
    boolean[][] visited;
    
    // Direction vectors.  TODO: Extend to allow diagonal movement
    int[] dx = {-1, +1,  0,  0};
    int[] dy = { 0,  0, +1, -1};    
    
    public Point[] findPath(final Dungeon map, final Point start, final Point destination, int z, int cellSearchDepth) 
    {
    	// Quick sanity check of positions
        if (isOutOfMap(map, start.x, start.y) || isOutOfMap(map, destination.x, destination.y))
        {
            return null;
        }
        
        JavaTools.printlnTime("Destination = " + destination);

        empty = new ArrayDeque<>();
        visited = new boolean[map.getXsize()][map.getYsize()];
        
        // Variables used to track the number of steps taken
        int move_count = 0;
        int nodes_left_in_layer = 1;
        nodes_in_next_layer = 0;
        
        // Variable to track if we reach the end
        boolean reached_end = false;
        
        // Solve function starts here  ==============================================

        // Start with the starting point and mark is as visited
        empty.add(start);
        visited[start.x][start.y] = true;
        
        while (empty.size() > 0)
        {
        	Point current = empty.remove();
        	
        	JavaTools.printlnTime("Checking " + current);
        	
        	if (current.equals(destination))   // Destination reached
        	{
        		reached_end = true;
        		break;
        	}
        	
        	explore_neighbors(map, current, destination, z);
        	
        	nodes_left_in_layer--;
        	
        	if (nodes_left_in_layer == 0)
        	{
        		nodes_left_in_layer = nodes_in_next_layer;
        		nodes_in_next_layer = 0;
        		move_count++;
        	}
        }
        	
        if (reached_end)
        {
        	JavaTools.printlnTime("Move Count = " + move_count);
        	return null; // move_count;        	
        }
        
        JavaTools.printlnTime("No path found");
        return null;
    }
    
    private boolean explore_neighbors(final Dungeon map, Point current, Point destination, int z) 
    {
    	// For each direction
    	for (int i=0; i < dx.length; i++)
    	{
    		int xx = current.x + dx[i];
    		int yy = current.y + dy[i];
    		
    		// Skip out of bounds conditions
    		if (isOutOfMap(map, xx, yy)) continue;
    		
    		// Skip visited or blocked cells *except* for the destination cell
    		if (visited[xx][yy]) continue;
    		if (isBlocked(map, xx, yy, z)) 
    		{
    			if ((xx == destination.x) && (yy == destination.y))
    			{
    				;  // Destination found!
    			}
    			else
    			{
    				continue;
    			}
    		}
    		
    		//map.getCell(xx, yy, z).setAttributes(Constants.CHAR_PORTAL);
    		
    		empty.add(new Point(xx,yy));
    		visited[xx][yy] = true;   // Set as visited now so it's not checked again later 
    		nodes_in_next_layer++;
    	}
    	return true;
    }

    private static boolean isOutOfMap(final Dungeon map, final int x, final int y) 
    {
    	 return (x < 0 || y < 0 || x > map.getXsize() || y > map.getXsize());
    }

    private static boolean isBlocked(final Dungeon map, final int x, final int y, final int z) 
    {	
    	return !map.getCell(x, y, z).canEnter(null);
    }
}
