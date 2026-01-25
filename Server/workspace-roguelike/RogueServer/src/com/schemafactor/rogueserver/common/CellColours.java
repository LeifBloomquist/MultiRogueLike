package com.schemafactor.rogueserver.common;

import java.awt.Color;

public class CellColours 
{
    // For the online map
    public static Color getCellColour(int charcode)
    {        
    	byte cbmcolor = COLOUR_TABLE[charcode];
    	
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

    // This mirrors the C64 client color lookup table 
    private static final byte[] COLOUR_TABLE = new byte[] 
    {                         
       Constants.COLOR_BLACK    , // 0    Empty Space
       Constants.COLOR_GREY2    , // 1    Brick Wall  
       Constants.COLOR_GREY1    , // 2    Dirt   
       Constants.COLOR_GREY2    , // 3    Closed Door   
       Constants.COLOR_GREY2    , // 4    Broken Wall   
       Constants.COLOR_WHITE    , // 5    Alternate Stairs Down   
       Constants.COLOR_WHITE    , // 6    Closed Door Big
       Constants.COLOR_WHITE    , // 7    Open Door
       Constants.COLOR_GREY3    , // 8    Stairs Down  
       Constants.COLOR_GREY3    , // 9    Stairs Up
       Constants.COLOR_GREY1    , // 10   Debris   
       Constants.COLOR_GREY2    , // 11   Squares   
       Constants.COLOR_GREY2    , // 12   Big Wall   
       Constants.COLOR_GREY2    , // 13   Open Door?   
       Constants.COLOR_GREY2    , // 14   Secret Door?  
       Constants.COLOR_RED      , // 15   Lava   
       Constants.COLOR_BLUE     , // 16   Water   
       Constants.COLOR_WHITE    , // 17   ?  
       Constants.COLOR_LIGHTBLUE, // 18   Portal
       Constants.COLOR_WHITE    , // 19   
       Constants.COLOR_WHITE    , // 20   
       Constants.COLOR_WHITE    , // 21   
       Constants.COLOR_WHITE    , // 22   
       Constants.COLOR_WHITE    , // 23   
       Constants.COLOR_WHITE    , // 24   
       Constants.COLOR_WHITE    , // 25   
       Constants.COLOR_WHITE    , // 26   
       Constants.COLOR_WHITE    , // 27   
       Constants.COLOR_WHITE    , // 28   
       Constants.COLOR_WHITE    , // 29   
       Constants.COLOR_WHITE    , // 30   
       Constants.COLOR_WHITE    , // 31   
       Constants.COLOR_WHITE    , // 32    Punctuation...   
       Constants.COLOR_WHITE    , // 33    
       Constants.COLOR_WHITE    , // 34    
       Constants.COLOR_WHITE    , // 35    
       Constants.COLOR_WHITE    , // 36    
       Constants.COLOR_WHITE    , // 37    
       Constants.COLOR_WHITE    , // 38    
       Constants.COLOR_WHITE    , // 39    
       Constants.COLOR_WHITE    , // 40    
       Constants.COLOR_WHITE    , // 41    
       Constants.COLOR_WHITE    , // 42    
       Constants.COLOR_WHITE    , // 43    
       Constants.COLOR_WHITE    , // 44    
       Constants.COLOR_WHITE    , // 45    
       Constants.COLOR_WHITE    , // 46    
       Constants.COLOR_WHITE    , // 47    
       Constants.COLOR_WHITE    , // 48    
       Constants.COLOR_WHITE    , // 49    
       Constants.COLOR_WHITE    , // 50    
       Constants.COLOR_WHITE    , // 51    
       Constants.COLOR_WHITE    , // 52    
       Constants.COLOR_WHITE    , // 53    
       Constants.COLOR_WHITE    , // 54    
       Constants.COLOR_WHITE    , // 55    
       Constants.COLOR_WHITE    , // 56    
       Constants.COLOR_WHITE    , // 57    
       Constants.COLOR_WHITE    , // 58    
       Constants.COLOR_WHITE    , // 59    
       Constants.COLOR_WHITE    , // 60    
       Constants.COLOR_WHITE    , // 61    
       Constants.COLOR_WHITE    , // 62    
       Constants.COLOR_WHITE    , // 63    
       Constants.COLOR_WHITE    , // 64    ASCII Letters...     
       Constants.COLOR_WHITE    , // 65     
       Constants.COLOR_WHITE    , // 66     
       Constants.COLOR_WHITE    , // 67     
       Constants.COLOR_WHITE    , // 68     
       Constants.COLOR_WHITE    , // 69     
       Constants.COLOR_WHITE    , // 70     
       Constants.COLOR_WHITE    , // 71     
       Constants.COLOR_WHITE    , // 72     
       Constants.COLOR_WHITE    , // 73     
       Constants.COLOR_WHITE    , // 74     
       Constants.COLOR_WHITE    , // 75     
       Constants.COLOR_WHITE    , // 76     
       Constants.COLOR_WHITE    , // 77     
       Constants.COLOR_WHITE    , // 78     
       Constants.COLOR_WHITE    , // 79     
       Constants.COLOR_WHITE    , // 80     
       Constants.COLOR_WHITE    , // 81     
       Constants.COLOR_WHITE    , // 82     
       Constants.COLOR_WHITE    , // 83     
       Constants.COLOR_WHITE    , // 84     
       Constants.COLOR_WHITE    , // 85     
       Constants.COLOR_WHITE    , // 86     
       Constants.COLOR_WHITE    , // 87     
       Constants.COLOR_WHITE    , // 88     
       Constants.COLOR_WHITE    , // 89     
       Constants.COLOR_WHITE    , // 90     
       Constants.COLOR_WHITE    , // 91     
       Constants.COLOR_WHITE    , // 92     
       Constants.COLOR_WHITE    , // 93     
       Constants.COLOR_WHITE    , // 94     
       Constants.COLOR_WHITE    , // 95     
       Constants.COLOR_RED      , // 96   Spider
       Constants.COLOR_WHITE    , // 97   Skeleton
       Constants.COLOR_BROWN    , // 98   Bat  
       Constants.COLOR_LIGHTRED , // 99   Demon
       Constants.COLOR_CYAN     , // 100  Spectre
       Constants.COLOR_WHITE    , // 101  Ghost
       Constants.COLOR_GREEN    , // 102  Frog Thing
       Constants.COLOR_GREEN    , // 103  Slime
       Constants.COLOR_LIGHTGREEN,// 104 Zombie
       Constants.COLOR_RED      , // 105  Daemon
       Constants.COLOR_WHITE    , // 106     
       Constants.COLOR_WHITE    , // 107     
       Constants.COLOR_WHITE    , // 108     
       Constants.COLOR_WHITE    , // 109     
       Constants.COLOR_WHITE    , // 110     
       Constants.COLOR_WHITE    , // 111     
       Constants.COLOR_WHITE    , // 112     
       Constants.COLOR_WHITE    , // 113     
       Constants.COLOR_WHITE    , // 114     
       Constants.COLOR_WHITE    , // 115     
       Constants.COLOR_WHITE    , // 116     
       Constants.COLOR_WHITE    , // 117     
       Constants.COLOR_WHITE    , // 118     
       Constants.COLOR_WHITE    , // 119     
       Constants.COLOR_WHITE    , // 120     
       Constants.COLOR_WHITE    , // 121     
       Constants.COLOR_WHITE    , // 122     
       Constants.COLOR_WHITE    , // 123     
       Constants.COLOR_WHITE    , // 124     
       Constants.COLOR_WHITE    , // 125     
       Constants.COLOR_WHITE    , // 126     
       Constants.COLOR_WHITE    , // 127     
       Constants.COLOR_WHITE    , // 128     Generic Player
       Constants.COLOR_WHITE    , // 129     Mage
       Constants.COLOR_WHITE    , // 130     Fighter
       Constants.COLOR_WHITE    , // 131     Fighter 2
       Constants.COLOR_WHITE    , // 132     
       Constants.COLOR_WHITE    , // 133     
       Constants.COLOR_WHITE    , // 134     
       Constants.COLOR_WHITE    , // 135     
       Constants.COLOR_WHITE    , // 136     
       Constants.COLOR_WHITE    , // 137     
       Constants.COLOR_WHITE    , // 138     
       Constants.COLOR_WHITE    , // 139     
       Constants.COLOR_WHITE    , // 140     
       Constants.COLOR_WHITE    , // 141     
       Constants.COLOR_WHITE    , // 142     
       Constants.COLOR_WHITE    , // 143     
       Constants.COLOR_WHITE    , // 144     
       Constants.COLOR_WHITE    , // 145     
       Constants.COLOR_WHITE    , // 146     
       Constants.COLOR_WHITE    , // 147     
       Constants.COLOR_WHITE    , // 148     
       Constants.COLOR_WHITE    , // 149     
       Constants.COLOR_WHITE    , // 150     
       Constants.COLOR_WHITE    , // 151     
       Constants.COLOR_WHITE    , // 152     
       Constants.COLOR_WHITE    , // 153     
       Constants.COLOR_WHITE    , // 154     
       Constants.COLOR_WHITE    , // 155     
       Constants.COLOR_WHITE    , // 156     
       Constants.COLOR_WHITE    , // 157     
       Constants.COLOR_WHITE    , // 158     
       Constants.COLOR_WHITE    , // 159     
       Constants.COLOR_WHITE    , // 160     
       Constants.COLOR_WHITE    , // 161     
       Constants.COLOR_WHITE    , // 162     
       Constants.COLOR_WHITE    , // 163     
       Constants.COLOR_WHITE    , // 164     
       Constants.COLOR_WHITE    , // 165     
       Constants.COLOR_WHITE    , // 166     
       Constants.COLOR_WHITE    , // 167     
       Constants.COLOR_WHITE    , // 168     
       Constants.COLOR_WHITE    , // 169     
       Constants.COLOR_WHITE    , // 170     
       Constants.COLOR_WHITE    , // 171     
       Constants.COLOR_WHITE    , // 172     
       Constants.COLOR_WHITE    , // 173  // Dash -   
       Constants.COLOR_WHITE    , // 174     
       Constants.COLOR_WHITE    , // 175     
       Constants.COLOR_WHITE    , // 176     
       Constants.COLOR_WHITE    , // 177     
       Constants.COLOR_WHITE    , // 178     
       Constants.COLOR_WHITE    , // 179     
       Constants.COLOR_WHITE    , // 180     
       Constants.COLOR_WHITE    , // 181     
       Constants.COLOR_WHITE    , // 182     
       Constants.COLOR_WHITE    , // 183     
       Constants.COLOR_WHITE    , // 184     
       Constants.COLOR_WHITE    , // 185     
       Constants.COLOR_WHITE    , // 186     
       Constants.COLOR_WHITE    , // 187     
       Constants.COLOR_WHITE    , // 188     
       Constants.COLOR_WHITE    , // 189     
       Constants.COLOR_WHITE    , // 190     
       Constants.COLOR_WHITE    , // 191     
       Constants.COLOR_LIGHTBLUE, // 192   Sword     
       Constants.COLOR_GREY3    , // 193   Shield  
       Constants.COLOR_ORANGE   , // 194   Bow  
       Constants.COLOR_ORANGE   , // 195   Arrow  
       Constants.COLOR_PURPLE   , // 196   Potion  
       Constants.COLOR_BLUE     , // 197   Gem  
       Constants.COLOR_WHITE    , // 198   Note  
       Constants.COLOR_BROWN    , // 199   Chest  (Closed)
       Constants.COLOR_WHITE    , // 200   Crook  
       Constants.COLOR_YELLOW   , // 201   Gold  
       Constants.COLOR_WHITE    , // 202   ?     
       Constants.COLOR_BROWN    , // 203   Rope  
       Constants.COLOR_YELLOW   , // 204   Key  
       Constants.COLOR_BROWN    , // 205   Sign     
       Constants.COLOR_WHITE    , // 206     
       Constants.COLOR_WHITE    , // 207     
       Constants.COLOR_WHITE    , // 208     
       Constants.COLOR_BROWN    , // 209   Chest (Open but empty)     
       Constants.COLOR_GREY1    , // 210   Ring  
       Constants.COLOR_WHITE    , // 211     
       Constants.COLOR_WHITE    , // 212     
       Constants.COLOR_WHITE    , // 213     
       Constants.COLOR_WHITE    , // 214     
       Constants.COLOR_WHITE    , // 215     
       Constants.COLOR_WHITE    , // 216     
       Constants.COLOR_WHITE    , // 217     
       Constants.COLOR_WHITE    , // 218     
       Constants.COLOR_WHITE    , // 219     
       Constants.COLOR_WHITE    , // 220     
       Constants.COLOR_WHITE    , // 221     
       Constants.COLOR_WHITE    , // 222     
       Constants.COLOR_WHITE    , // 223     
       Constants.COLOR_WHITE    , // 224     
       Constants.COLOR_WHITE    , // 225     
       Constants.COLOR_WHITE    , // 226     
       Constants.COLOR_WHITE    , // 227     
       Constants.COLOR_WHITE    , // 228     
       Constants.COLOR_WHITE    , // 229     
       Constants.COLOR_WHITE    , // 230     
       Constants.COLOR_WHITE    , // 231     
       Constants.COLOR_WHITE    , // 232     
       Constants.COLOR_WHITE    , // 233     
       Constants.COLOR_WHITE    , // 234     
       Constants.COLOR_WHITE    , // 235     
       Constants.COLOR_WHITE    , // 236     
       Constants.COLOR_WHITE    , // 237     
       Constants.COLOR_WHITE    , // 238     
       Constants.COLOR_WHITE    , // 239     
       Constants.COLOR_WHITE    , // 240     
       Constants.COLOR_WHITE    , // 241     
       Constants.COLOR_WHITE    , // 242     
       Constants.COLOR_WHITE    , // 243     
       Constants.COLOR_WHITE    , // 244     
       Constants.COLOR_WHITE    , // 245     
       Constants.COLOR_WHITE    , // 246     
       Constants.COLOR_WHITE    , // 247     
       Constants.COLOR_WHITE    , // 248     
       Constants.COLOR_WHITE    , // 249     
       Constants.COLOR_WHITE    , // 250     
       Constants.COLOR_WHITE    , // 251     
       Constants.COLOR_WHITE    , // 252     
       Constants.COLOR_WHITE    , // 253     
       Constants.COLOR_BLACK    , // 254     // New - barrier cell for monsters
       Constants.COLOR_WHITE    , // 255     
  };
}
