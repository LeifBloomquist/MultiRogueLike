package com.schemafactor.rogueserver.common;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

import com.schemafactor.rogueserver.dungeon.Dungeon;


public class PathFinding 
{
	final boolean DEBUG = true;
	
	Queue<Point> empty = null;
    
    // Variables used to track the number of steps taken
    int nodes_left_in_layer = 1;
    int nodes_in_next_layer = 0;
    
    // Matrix of values to track whether the nodes (cells) have been visited 
    boolean[][] visited;
    
    // Matrix of links to previous cell coordinates, for reconstructing the path 
    Point[][] prev;
    
    // Direction vectors.  TODO: Extend to allow diagonal movement
    int[] dx = {-1, +1,  0,  0};
    int[] dy = { 0,  0, +1, -1};    
    
    // Breadth-first path search.  TODO, A*?    
    public Point[] findPath(final Dungeon map, final Point start, final Point destination, int z, int cellSearchDepth) 
    {
    	// Quick sanity check of positions
        if (isOutOfMap(map, start.x, start.y) || isOutOfMap(map, destination.x, destination.y))
        {
            return null;
        }
        
        if (DEBUG) JavaTools.printlnTime("Destination = " + destination);

        empty = new ArrayDeque<>();
        visited = new boolean[map.getXsize()][map.getYsize()];
        prev = new Point[map.getXsize()][map.getYsize()];
        
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
        	
        	if (DEBUG) JavaTools.printlnTime("Checking " + current);
        	
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
        		
        		if (move_count > cellSearchDepth)  // No match found in search range
        		{
        			return null;
        		}
        	}
        }
        	
        if (reached_end)
        {        	
        	return reconstruct_path(start, destination, map);        	
        }
        
        if (DEBUG) JavaTools.printlnTime("No path found");
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
    				;  // Destination found!  Don't skip it otherwise no end point will be found
    			}
    			else
    			{
    				continue;
    			}
    		}
    		
    		if (DEBUG) map.getCell(xx, yy, z).setAttributes(Constants.CHAR_PORTAL);
    		
    		// Save previous cell for recreating the path at the end
    		prev[xx][yy] = new Point(current);
    		
    		/// Add the new cell for next iteration
    		empty.add(new Point(xx,yy));
    		
    		// Set as visited now so it's not checked again later
    		visited[xx][yy] = true;    
    		
    		// Count the node for next iteration
    		nodes_in_next_layer++;
    	}
    	return true;
    }
	
	
	// Use the prev[][] array to reconstruct the path
    private Point[] reconstruct_path(Point start, Point destination, Dungeon map) 
    {
    	ArrayList<Point> path = new ArrayList<Point>();
    	
    	// Start at the end and work back
    	Point step = new Point(destination);
    	
    	while (!step.equals(start))    // We don't want to include the starting point
    	{
    		int prev_x = prev[step.x][step.y].x;
    		int prev_y = prev[step.x][step.y].y;
    		
    		step = new Point(prev_x, prev_y);
    		path.add(step);
    		
    		if (DEBUG) map.getCell(prev_x, prev_y, 0).setAttributes(Constants.CHAR_ITEM_CHEST);
    	}
    	
    	// Remove the last point as it's always the start point
    	path.remove(path.size()-1);
    	
    	// Reverse it
    	Collections.reverse(path);
    	
		return (Point[]) path.toArray(new Point[0]);
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
