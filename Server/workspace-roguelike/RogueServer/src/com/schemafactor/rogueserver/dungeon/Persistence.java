package com.schemafactor.rogueserver.dungeon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.schemafactor.rogueserver.common.JavaTools;

public class Persistence
{
    private static String serializeFileName = "rogue-dungeon-persist.ser";
    
    public static boolean Serialize(Dungeon d)
    { 
        try 
        {
            FileOutputStream fileOut = new FileOutputStream(serializeFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(d);
            out.close();
            fileOut.close();
            JavaTools.printlnTime("Serialized data is saved in " + serializeFileName);
            return true; 
        } 
        catch (IOException i) 
        {
            i.printStackTrace();
            return false;
        }
    }

    public static Dungeon deSerialize()
    {
        Dungeon d = null;
        
        try 
        {
           FileInputStream fileIn = new FileInputStream(serializeFileName);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           d = (Dungeon) in.readObject();
           in.close();
           fileIn.close();
           return d;           
        } 
        catch (IOException i) 
        {
           i.printStackTrace();
           return null;
        }
        catch (ClassNotFoundException c) 
        {
           JavaTools.printlnTime("Dungeon class not found!");
           c.printStackTrace();
           return null;
        }
    }
}