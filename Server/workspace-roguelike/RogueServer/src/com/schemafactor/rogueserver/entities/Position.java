package com.schemafactor.rogueserver.entities;

public class Position 
{
	public int x;
	public int y;
	public int z;  // Level
	
	public Position(int x, int y, int z) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
