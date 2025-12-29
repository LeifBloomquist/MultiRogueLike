/**
 * 
 */
package com.schemafactor.rogueserver.webserver;

//import java.awt.Color;
//import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.entities.Entity;

/**
 * @author Leif
 *
 * This class is a simple representation of an Entity for purposes of JSON and web display.
 */
public class SimpleEntity 
{
	public long x;      // These are pixels, and refers to the top-left corner of the object (sprite, etc.)
	public long y;
	public String name;
	public String color;
	
	public SimpleEntity(Entity original)
	{
		this.x = Math.round( original.getPosition().x );
		this.y = Math.round( original.getPosition().y );
		this.name = original.getDescription();
		//this.color = JavaTools.toHexString( original.getRGBColor() );		 
	}
}
