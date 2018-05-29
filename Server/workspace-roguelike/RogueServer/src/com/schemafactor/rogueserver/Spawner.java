package com.schemafactor.rogueserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.common.Position;
import com.schemafactor.rogueserver.common.interfaces.Rechargeable;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.entities.Entity.entityTypes;
import com.schemafactor.rogueserver.entities.monsters.Bat;
import com.schemafactor.rogueserver.entities.monsters.Ghost;
import com.schemafactor.rogueserver.entities.monsters.Monster;
import com.schemafactor.rogueserver.entities.monsters.Skeleton;
import com.schemafactor.rogueserver.entities.monsters.Slime;
import com.schemafactor.rogueserver.entities.monsters.Spider;
import com.schemafactor.rogueserver.items.Chest;
import com.schemafactor.rogueserver.items.Gem;
import com.schemafactor.rogueserver.items.MagicKey;
import com.schemafactor.rogueserver.items.Note;
import com.schemafactor.rogueserver.items.Potion;
import com.schemafactor.rogueserver.items.Shield;
import com.schemafactor.rogueserver.items.Sign;
import com.schemafactor.rogueserver.items.Sword;
import com.schemafactor.rogueserver.universe.Cell;
import com.schemafactor.rogueserver.universe.Dungeon;

public class Spawner
{
    // List of monsters that always respawn
    private static List<Monster> welcomingCommittee = Collections.synchronizedList(new ArrayList<Monster>());
    
    // List of all other monsters, that may respawn randomly    
    private static List<Monster> allMonsters = Collections.synchronizedList(new ArrayList<Monster>());
    
    // List of all potions    
    private static List<Rechargeable> allRechargeItems = Collections.synchronizedList(new ArrayList<Rechargeable>());
    
    public static void spawnEntities(Dungeon dungeon)
    {   
        Slime slimey = new Slime("Slime", new Position(56,8,0));
        Skeleton mrbones = new Skeleton("Skeleton", new Position(89,11,0));
        Spider mike = new Spider("Spider", new Position(38,38,0));
        
        welcomingCommittee.add( slimey );
        welcomingCommittee.add( mrbones );
        welcomingCommittee.add( mike );
        
        // Level 0 (Entry) -----------------------------------------------------------------------------------------------------
        
        dungeon.addEntity( slimey );
        dungeon.addEntity( mrbones ); 
        dungeon.addEntity( mike );
        
        // Level 1 -------------------------------------------------------------------------------------------------------------
        
        for (int i=1; i<=20; i++)
        {
            allMonsters.add( new Spider("Spider", dungeon.getRandomPosition(1)) );            
            allMonsters.add( new Slime("Slime", dungeon.getRandomPosition(1)) );             
        } 
        
        // Level 2   -------------------------------------------------------------------------------------------------------------

        for (int i=1; i<=30; i++)
        {            
            allMonsters.add( new Bat("Bat", dungeon.getRandomPosition(2)) );             
        } 
        
        for (int i=1; i<=10; i++)
        {
            allMonsters.add( new Skeleton("Skeleton", dungeon.getRandomPosition(2)) ); 
        } 
        
        // Level 3   -------------------------------------------------------------------------------------------------------------
        
        for (int i=1; i<=10; i++)
        {
            allMonsters.add( new Ghost("Ghost", dungeon.getRandomPosition(3)) );
            allMonsters.add( new Skeleton("Skeleton",dungeon.getRandomPosition(3)) );
            allMonsters.add( new Spider("Spider", dungeon.getRandomPosition(3)) );            
            allMonsters.add( new Slime("Slime", dungeon.getRandomPosition(3)) );     
        }
        
        // Add to dungeon --------------------------------------------------------------------------------------------------------
        
        for (Monster m : allMonsters)
        {
            dungeon.addEntity(m);
        }
        
    }

    // Add some test items.
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
        
        dungeon.placeItem( new Sword("Sword of Doom", 50, 20), dungeon.getRandomPosition(1) );
        dungeon.placeItem( new Shield("Large Shield", 50), dungeon.getRandomPosition(1) );
        
        for (int i=1; i<=5; i++)
        {        
            dungeon.placeItem( new Sword("Short Sword", 10, 5), dungeon.getRandomPosition(1) );
            dungeon.placeItem( new Shield("Small Shield", 10), dungeon.getRandomPosition(1) );
        }
        
        // Level 2  -------------------------------------------------------------------------------------------------------------        
        
        for (int i=1; i<=10; i++)
        {        
            dungeon.placeItem( new Chest("Old Chest", new Sword("Long Sword", 20, 10)), dungeon.getRandomPosition(2));
            dungeon.placeItem( new Chest("Old Chest", new Shield("Shield", 20)), dungeon.getRandomPosition(2));
        }
        
        // Key to the Upstairs Room
        Cell endkeyloc = dungeon.getCell(new Position(24,6,2));
        MagicKey endkey = new MagicKey("Rusty Key", new Position(13,52,0), endkeyloc);
        endkeyloc.placeItem(endkey);
        
        // Level 3  -------------------------------------------------------------------------------------------------------------        

        // The powerful Teleportation Gem - one only, in a chest
        
        Position p = dungeon.getRandomPosition(3);
        Gem gem = new Gem(10);
        dungeon.placeItem( new Chest("Chest", gem), p);
        allRechargeItems.add(gem);        
        
        // All Levels -------------------------------------------------------------------------------------------------------------        
        
        // Chests all through dungeon containing gold
        for (int i=1; i<=30; i++)
        {
            p = dungeon.getRandomPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            
            int gold = JavaTools.generator.nextInt(100);            
            dungeon.placeItem( new Chest("Wooden Chest", gold), p);
        }
        
        // Chests all through dungeon containing potions
        for (int i=1; i<=10; i++)
        {
            int health = JavaTools.generator.nextInt(100);    
            Potion potion = new Potion(health);
            
            allRechargeItems.add(potion);
            
            p = dungeon.getRandomPosition();
            if (p.z == 0) p.z++;  // Not on starting level
            dungeon.placeItem( new Chest("Ornate Chest", potion ), p);
        }
        
        // Empty chests to keep life interesting
        for (int i=1; i<=20; i++)
        {
            p = dungeon.getRandomPosition();
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
                   m.respawn();
                   dungeon.addEntity(m);
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
