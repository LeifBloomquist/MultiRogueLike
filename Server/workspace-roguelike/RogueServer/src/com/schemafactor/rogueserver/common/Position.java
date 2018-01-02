package com.schemafactor.rogueserver.common;

public class Position implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
    
    public int x;
	public int y;
	public int z;  // Level
	
	public Position(int x, int y, int z) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position(Position pos)
	{
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
	}

	@Override
	public String toString()
    {
        return "X=" + x + " Y=" + y + " Z=" + z;
    }
	
	public boolean equals(Position other)
    {	    
        return ( (this.x == other.x) &&
                 (this.y == other.y) &&
                 (this.z == other.z) );            
    }
}
