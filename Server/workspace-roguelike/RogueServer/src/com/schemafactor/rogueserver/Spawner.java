package com.schemafactor.rogueserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.vincentrussell.ini.Ini;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Rechargeable;
import com.schemafactor.rogueserver.dungeon.Cell;
import com.schemafactor.rogueserver.dungeon.Dungeon;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;
import com.schemafactor.rogueserver.entities.monsters.Bat;
import com.schemafactor.rogueserver.entities.monsters.Daemon;
import com.schemafactor.rogueserver.entities.monsters.Frog;
import com.schemafactor.rogueserver.entities.monsters.Ghost;
import com.schemafactor.rogueserver.entities.monsters.Monster;
import com.schemafactor.rogueserver.entities.monsters.Skeleton;
import com.schemafactor.rogueserver.entities.monsters.Slime;
import com.schemafactor.rogueserver.entities.monsters.Spider;
import com.schemafactor.rogueserver.entities.monsters.Zombie;
import com.schemafactor.rogueserver.items.Chest;
import com.schemafactor.rogueserver.items.Gem;
import com.schemafactor.rogueserver.items.MagicKey;
import com.schemafactor.rogueserver.items.Note;
import com.schemafactor.rogueserver.items.Potion;
import com.schemafactor.rogueserver.items.Ring;
import com.schemafactor.rogueserver.items.Shield;
import com.schemafactor.rogueserver.items.Sign;
import com.schemafactor.rogueserver.items.Sword;

public class Spawner
{
    // List of monsters that always respawn
    private static List<Monster> welcomingCommittee = Collections.synchronizedList(new ArrayList<Monster>());
    
    // List of all other monsters, that may respawn randomly    
    private static List<Monster> allMonsters = Collections.synchronizedList(new ArrayList<Monster>());
    
    // List of all rechargeable items    
    private static List<Rechargeable> allRechargeItems = Collections.synchronizedList(new ArrayList<Rechargeable>());
    
    // Dummy value to represent "All" levels
    private static final int ALL_LEVELS = -1;
    
    public static boolean spawn(Dungeon dungeon, String path, String inifile)
    {
    	String fullpath = path + inifile;
    			
    	Ini ini = new Ini();
    	try 
    	{
			ini.load(new FileInputStream(fullpath));
		} 
    	catch (FileNotFoundException e) 
    	{
    		JavaTools.printlnTime("EXCEPTION, can't find " + fullpath);
    		System.exit(2);
		} 
    	catch (IOException e) 
    	{
			JavaTools.printlnTime("EXCEPTION, can't load " + fullpath + " -- " + e.getMessage() );
			System.exit(3);
		}    	
        
        // The Dungeon itself
        long size = (long)ini.getValue("Dungeon", "DungeonSize");        
        long depth = (long)ini.getValue("Dungeon", "DungeonDepth");
        // int seed = (int)ini.getValue("Dungeon", "RandomSeed");                
        dungeon.Create((int)size, (int) depth);
        
        // Load Levels
        int levelnum=0;
        Collection<String> levels = ini.getKeys("Levels");
        for(String level_id : levels)
        {
        	String levelfile = path + (String)ini.getValue("Levels", level_id);
        	
        	try 
            {
        		 dungeon.LoadTXT(levelfile, levelnum++);
    		} 
            catch (FileNotFoundException e) 
            {
            	JavaTools.printlnTime("EXCEPTION, file not found " + levelfile);
    			e.printStackTrace();
    			System.exit(4);
    		}        	
        }
        
        // Place items to be initiated on All levels
        Map<String, Object> all = ini.getSection("All");
        
        for (var entry : all.entrySet()) 
        {
        	spawn(entry.getKey(), (String)entry.getValue(), ALL_LEVELS);
        }
        

        /* Examples
    	Collection<String> sections = ini.getSections();    	
    	Object value = ini.getValue("Level0", "Monster");
        Map<String, Object> value2 = ini.getSection("Level1");
        */
        
        return true;
        
    }
    	 
    private static void spawn(String type, String parameters, int level) 
    {
    	// Multiple Monsters
		if (type.startsWith("Monsters"))
		{
			;
		}
		
		// Multiple Items
		if (type.startsWith("Items"))
		{
			;
		}
		
		// Single Monsters
		if (type.startsWith("Monster"))
		{
			spawnMonster(parameters, level)
		}
		
		// Multiple Items
		if (type.startsWith("Item"))
		{
			;
		}		
	}

	@Deprecated
    public static boolean spawnEntities (Dungeon dungeon, String pathToIni)
    {    	
        JavaTools.generator.setSeed(12345678);
        
        // TODO move all this to an ini file
        
        Slime slimey = new Slime("Slime", new Position(56,8,0));
        Spider mike = new Spider("Spider", new Position(38,38,0));
        
        welcomingCommittee.add( slimey );
        welcomingCommittee.add( mike );
        
        // Level 0 (Entry) -----------------------------------------------------------------------------------------------------
        
        allMonsters.add( slimey ); 
        allMonsters.add( mike );
                
        // Level 1 -------------------------------------------------------------------------------------------------------------
        
        for (int i=1; i<=20; i++)
        {
            allMonsters.add( new Spider("Spider", dungeon.getRandomEmptyPosition(1)) );            
            allMonsters.add( new Slime("Slime", dungeon.getRandomEmptyPosition(1)) );             
        } 
        
        // Level 2   -------------------------------------------------------------------------------------------------------------

        for (int i=1; i<=30; i++)
        {            
            allMonsters.add( new Bat("Bat", dungeon.getRandomEmptyPosition(2)) );             
        } 
        
        for (int i=1; i<=8; i++)
        {
            allMonsters.add( new Skeleton("Skeleton", dungeon.getRandomEmptyPosition(2)) );
            allMonsters.add( new Frog("Frog", dungeon.getRandomEmptyPosition(2)) );
        } 
        
        
        // Level 3   -------------------------------------------------------------------------------------------------------------
        
        for (int i=1; i<=10; i++)
        {
            allMonsters.add( new Ghost("Ghost", dungeon.getRandomEmptyPosition(3)) );
            allMonsters.add( new Zombie("Zombie",dungeon.getRandomEmptyPosition(3)) );
            allMonsters.add( new Spider("Spider", dungeon.getRandomEmptyPosition(3)) );            
            allMonsters.add( new Slime("Slime", dungeon.getRandomEmptyPosition(3)) );
        }
        
        for (int i=1; i<=3; i++)
        {
            allMonsters.add( new Skeleton("Skeleton", dungeon.getRandomEmptyPosition(3)) );
        }
        
        allMonsters.add( new Daemon("Daemon", dungeon.getRandomEmptyPosition(3)) );
        
        // Add to dungeon --------------------------------------------------------------------------------------------------------
        
        for (Monster m : allMonsters)
        {
            dungeon.addEntity(m);
        }
		return true;
        
    }

    // Add some test items.
    @Deprecated
    public static void placeItems(Dungeon dungeon)
    {
       // Magic Word -----------------------------------------------------------------------------------------------------------
        
        String magic = "Magic Word: 12345";
        
        try
        {
            magic = new String(Files.readAllBytes(Paths.get("magic.txt")));
        }
        catch (IOException e)
        {
             JavaTools.printlnTime( "EXCEPTION reading magic file: " + e.getMessage());
             magic = "Magic Word: 54321";
        }        
        
        // Level 0 -------------------------------------------------------------------------------------------------------------

        dungeon.placeItem( new Sign("Crooked Sign", "Adventure awaits! Find a key..."), 
                           new Position(18,12,0));
        
        dungeon.placeItem( new Note("Tattered Note", "Portals teleport to random areas"), 
                           new Position(87,45,0));
        
        // Key to the stairs
        Cell keystart = dungeon.getCell(new Position(87,38,0));
        MagicKey stairkey = new MagicKey("Shiny Key", new Position(56,17,0), keystart);
        keystart.placeItem(stairkey);

        // Key to the Portal Room, in a chest
        Chest portalchest = new Chest("Ornate Chest", null);
        MagicKey portalkey = new MagicKey("Glowing Key", new Position(38,41,0), portalchest);
        portalchest.setContainedItem(portalkey);
        dungeon.placeItem(portalchest, new Position(94,67,0));
        
        // Emergency Potions
        Potion p1 = new Potion(500);
        Potion p2 = new Potion(500);
        
        allRechargeItems.add(p1);
        allRechargeItems.add(p2);
        
        dungeon.placeItem( new Chest("Decorated Chest", p1), new Position(37,57,0));
        dungeon.placeItem( new Chest("Decorated Chest", p2), new Position(39,57,0));

        // Sign at the end
        dungeon.placeItem( new Sign("Large Sign", magic), new Position(8,52,0));
        
        // Level 1  -------------------------------------------------------------------------------------------------------------        
       
        for (int i=1; i<=5; i++)
        {        
            dungeon.placeItem( new Sword("Short Sword", 5, 2), dungeon.getRandomEmptyPosition(1) );
            dungeon.placeItem( new Shield("Small Shield", 4), dungeon.getRandomEmptyPosition(1) );
        }
        
        // Level 2  -------------------------------------------------------------------------------------------------------------        
        
        dungeon.placeItem( new Sword("Sword of Doom", 20, 10), dungeon.getRandomEmptyPosition(2) );
        dungeon.placeItem( new Shield("Large Shield", 10), dungeon.getRandomEmptyPosition(2) );
        
        for (int i=1; i<=10; i++)
        {        
            dungeon.placeItem( new Chest("Old Chest", new Sword("Long Sword", 10, 4)), dungeon.getRandomEmptyPosition(2));
            dungeon.placeItem( new Chest("Old Chest", new Shield("Shield", 6)), dungeon.getRandomEmptyPosition(2));
        }
        
        // Key to the Upstairs Room
        Cell endkeyloc = dungeon.getCell(new Position(24,6,2));
        MagicKey endkey = new MagicKey("Rusty Key", new Position(13,52,0), endkeyloc);
        endkeyloc.placeItem(endkey);
        
        // Level 3  -------------------------------------------------------------------------------------------------------------        

        // The powerful Teleportation Gem - one only, in a chest
        
        Position p = dungeon.getRandomEmptyPosition(3);
        Gem gem = new Gem(10);
        dungeon.placeItem( new Chest("Chest", gem), p);
        allRechargeItems.add(gem);
        
        // Invisibility Ring
        
        p = dungeon.getRandomEmptyPosition(3);
        Ring ring = new Ring(100);
        dungeon.placeItem(ring, p);
        allRechargeItems.add(ring);
        
        // All Levels -------------------------------------------------------------------------------------------------------------        
        
        // Chests all through dungeon containing gold
        for (int i=1; i<=30; i++)
        {
            p = dungeon.getRandomEmptyPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            
            int gold = JavaTools.generator.nextInt(100);            
            dungeon.placeItem( new Chest("Wooden Chest", gold), p);
        }
        
        // Chests all through dungeon containing potions
        for (int i=1; i<=20; i++)
        {
            int health = JavaTools.generator.nextInt(100);    
            Potion potion = new Potion(health);
            
            allRechargeItems.add(potion);
            
            p = dungeon.getRandomEmptyPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            dungeon.placeItem( new Chest("Ornate Chest", potion ), p);
        }
        
        // Empty chests to keep life interesting
        for (int i=1; i<=20; i++)
        {
            p = dungeon.getRandomEmptyPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            dungeon.placeItem( new Chest("Chest", null), p);
        }
    }

    public static void respawn(Dungeon dungeon)
    {        
        // Respawn Monsters        
        List<Monster> monsters = new ArrayList<Monster>();
        monsters.addAll(welcomingCommittee);
        monsters.addAll(allMonsters);
        
        for (Monster m : monsters)
        {
           if (m.isDead())
           {
               List<Entity> nearby = Dungeon.getInstance().getEntitiesOnScreenCentered(m.getPosition());
               List<Entity> nearby_humans = Dungeon.getInstance().getEntitiesType(m, entityTypes.CLIENT, nearby);
               
               if (nearby_humans.size() == 0)
               {
                   JavaTools.printlnTime( "Respawning: " + m.getDescription());
                   m.respawn(); // Also adds self back to dungeon entities list
               }
           }
        }
        
        // Recharge Items
        for (Rechargeable p : allRechargeItems)
        {
           p.recharge();
        }
    }
}
