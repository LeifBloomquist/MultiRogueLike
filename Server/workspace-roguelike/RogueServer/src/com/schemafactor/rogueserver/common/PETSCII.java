package com.schemafactor.rogueserver.common;

import java.nio.charset.StandardCharsets;

import com.schemafactor.rogueserver.dungeon.Dungeon;

public class PETSCII
{  
    private static int INVALID = 168;  // Invalid entry, used for no match (change it here)
       
    // Return an ASCII equivalent of a "PETSCII" or game graphic code
    private static int ascii_lookup(int petscii)
    {
        switch (petscii)
        {
            case Constants.CHAR_EMPTY:
                return 32;
           
            case Constants.CHAR_BRICKWALL:
                return 178;
           
            case Constants.CHAR_DIRT:
                return 176;
           
            case Constants.CHAR_DOOR_CLOSED:
                return INVALID;
                
            case Constants.CHAR_STAIRS_DOWN:
                return 118;
           
            case Constants.CHAR_STAIRS_UP:
                return 94;
           
            case Constants.CHAR_DEBRIS:
                return 249;
           
            case Constants.CHAR_MONSTER_SPIDER:
                return 210;
           
            case Constants.CHAR_MONSTER_SKELETON:
                return 237;
           
            case Constants.CHAR_MONSTER_BAT:
                return 126;
           
            case Constants.CHAR_MONSTER_DEMON:
                return 232;
           
            case Constants.CHAR_MONSTER_SPECTRE:
                return 233;
           
            case Constants.CHAR_MONSTER_GHOST:
                return 225;
           
            case Constants.CHAR_MONSTER_FROG:
                return 234;
           
            case Constants.CHAR_MONSTER_SLIME:
                return 239;
           
            case Constants.CHAR_MONSTER_ZOMBIE:
                return 232;
           
            case Constants.CHAR_MONSTER_DAEMON:
                return 228;
           
            case Constants.CHAR_PLAYER_GENERIC:
                return 64;
           
            case Constants.CHAR_PLAYER_MAGE:
                return 33;
           
            case Constants.CHAR_ITEM_SWORD:
                return 197;
           
            case Constants.CHAR_ITEM_SHIELD:
                return 180;
           
            case Constants.CHAR_ITEM_BOW:
                return 68;
           
            case Constants.CHAR_ITEM_ARROW:
                return 124;
           
            case Constants.CHAR_ITEM_POTION:
                return 147;
           
            case Constants.CHAR_ITEM_GEM:
                return 42;
           
            case Constants.CHAR_ITEM_NOTE:
                return 240;
           
            case Constants.CHAR_ITEM_CHEST:
                return 220;
           
            case Constants.CHAR_ITEM_CROOK:
                return 159;
           
            case Constants.CHAR_ITEM_GOLD:
                return 236;
           
            case Constants.CHAR_ITEM_ROPE:
                return 128;
           
            case Constants.CHAR_ITEM_KEY:
                return 183;
                
            case Constants.CHAR_ITEM_SIGN:
                return 80;
               
            
            default:
                return INVALID;
        }   
    }
    
    public static String toASCII(byte[] petscii)
    { 
        String out = "";
        
        for (byte b : petscii)
        {
             byte a = toASCII(b);
             
             out += new String(new byte[] {a}, StandardCharsets.US_ASCII);
        }
        return out;
    }
          
    public static byte toASCII(byte b)
    {
         int i = (int)(b & 0xFF);   
         
         // Numbers
         if ((i>=48) && (i<=57))
         {
             return b;
         }
         
         // Lowercase
         if ((i>=65) && (i<=90))
         {
             return (byte) (b+32);
         }
         
         // Uppercase
         if ((i>=193) && (i<=218))
         {
             return (byte) (b-128);
         }
         
         // Not strictly an ASCII conversion, but returns equivalent functional keystrokes.         
         switch (i)
         {         
             // Control characters.  
             case 29:  // Cursor right
                 return (byte)'d'; 
                 
             case 157:  // Cursor left
                 return (byte)'a';
                 
             case 145:  // Cursor up
                 return (byte)'w';
             
             case 17:  // Cursor down
                 return (byte)'x';

             case 133:  // F1
                 return (byte)'h';
                 
             // Other chars
                 
             case 42:  // *
                 return (byte)'*';
                 
             case '!':  // *
                 return (byte)'!';

             case ',':  // *
                 return (byte)',';

             case '.':  // *
                 return (byte)'.';        
         }
         
         // Everything else becomes a null
         return 0;       
    }
    
    public static String toExtendedASCII(byte[] petscii_charcodes)
    { 
        char[] out = new char[petscii_charcodes.length];
        
        for (int i=0; i < petscii_charcodes.length; i++)
        {
            out[i] = (char) getExtendedASCII(petscii_charcodes[i]);             
        }
        
        String outs = new String(out);
        
        return outs;
    }
    
    public static char toExtendedASCII(int ascii_code)
    { 
       return (char) ExtendedAscii.getAscii(ascii_code);             
    } 
    
    public static int ASCIILookup(int petscii)
    { 
        return ascii_lookup(petscii);
    }     
    
    public static char getExtendedASCII(int petscii_charcode)
    {    
        int lookup = PETSCII.ASCIILookup(petscii_charcode);
        char converted = PETSCII.toExtendedASCII(lookup);
        return converted;
    }
}
