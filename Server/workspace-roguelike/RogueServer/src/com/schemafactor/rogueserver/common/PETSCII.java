package com.schemafactor.rogueserver.common;

import java.nio.charset.StandardCharsets;

public class PETSCII
{  
    private static int INV = 129;  // Invalid entry, used for no match (change it here)
       
    private final static int[] ascii_lookup = 
        {
           32,  // 0    Empty Space
           178, // 1    Brick Wall
           176, // 2    Dirt    
           INV, // 3       
           INV, // 4       
           INV, // 5       
           INV, // 6       
           INV, // 7       
           INV, // 8       
           INV, // 9       
           INV, // 10      
           INV, // 11      
           INV, // 12      
           INV, // 13      
           INV, // 14      
           INV, // 15      
           INV, // 16      
           INV, // 17      
           INV, // 18      
           INV, // 19      
           INV, // 20      
           INV, // 21      
           INV, // 22      
           INV, // 23      
           INV, // 24      
           INV, // 25      
           INV, // 26      
           INV, // 27      
           INV, // 28      
           INV, // 29      
           INV, // 30      
           INV, // 31      
           INV, // 32      
           INV, // 33      
           INV, // 34   Thick B
           INV, // 35      
           INV, // 36      
           INV, // 37      
           INV, // 38      
           INV, // 39      
           INV, // 40      
           INV, // 41      
           INV, // 42      
           INV, // 43      
           INV, // 44      
           INV, // 45      
           INV, // 46      
           INV, // 47      
           INV, // 48      
           INV, // 49      
           INV, // 50      
           INV, // 51      
           INV, // 52      
           INV, // 53      
           INV, // 54      
           INV, // 55      
           INV, // 56      
           INV, // 57      
           INV, // 58      
           INV, // 59      
           INV, // 60      
           INV, // 61      
           INV, // 62      
           INV, // 63      
           INV, // 64      
           INV, // 65      
           INV, // 66      
           INV, // 67      
           INV, // 68      
           INV, // 69      
           INV, // 70      
           INV, // 71      
           INV, // 72      
           INV, // 73      
           INV, // 74  Sword    
           INV, // 75      
           INV, // 76      
           INV, // 77      
           INV, // 78      
           INV, // 79      
           INV, // 80      
           INV, // 81      
           INV, // 82      
           INV, // 83      
           INV, // 84      
           INV,      // 85      
           INV,      // 86      
           INV,      // 87      
           INV,      // 88      
           INV,      // 89      
           INV,      // 90      
           INV,      // 91      
           INV,      // 92      
           INV,      // 93      
           INV,      // 94      
           INV,      // 95      
           INV,      // 96      Sp
           INV,      // 97      Sk
           INV,      // 98      Ba
           INV,      // 99      
           INV,      // 100     
           INV,      // 101     
           INV,      // 102     
           INV,      // 103      Sli
           INV,      // ; 104    Zom
           INV,      // 105     
           INV,      // 106     
           INV,      // 107     
           INV,      // 108     
           INV,      // 109     
           INV,      // 110     
           INV,      // 111     
           INV,      // 112     
           INV,      // 113     
           INV,      // 114     
           INV,      // 115     
           INV,      // 116     
           INV,      // 117     
           INV,      // 118     
           INV,      // 119     
           INV,      // 120     
           INV,      // 121     
           INV,      // 122     
           INV,      // 123     
           INV,      // 124     
           INV,      // 125     
           INV,      // 126     
           INV,      // 127     Ge
           INV,      // 128     
           INV,      // 129     
           INV,      // 130     
           INV,      // 131     
           INV,      // 132     
           INV,      // 133     
           INV,      // 134     
           INV,      // 135     
           INV,      // 136     
           INV,      // 137     
           INV,      // 138     
           INV,      // 139     
           INV,      // 140     
           INV,      // 141     
           INV,      // 142     
           INV,      // 143     
           INV,      // 144     
           INV,      // 145     
           INV,      // 146     
           INV,      // 147     
           INV,      // 148     
           INV,      // 149     
           INV,      // 150     
           INV,      // 151     
           INV,      // 152     
           INV,      // 153     
           INV,      // 154     
           INV,      // 155     
           INV,      // 156     
           INV,      // 157     
           INV,      // 158     
           INV,      // 159     
           INV,      // 160     
           INV,      // 161     
           INV,      // 162     
           INV,      // 163     
           INV,      // 164     
           INV,      // 165     
           INV,      // 166     
           INV,      // 167     
           INV,      // 168     
           INV,      // 169     
           INV,      // 170     
           INV,      // 171     
           INV,      // 172     
           INV,      // 173     
           INV,      // 174     
           INV,      // 175     
           INV,      // 176     
           INV,      // 177     
           INV,     // 178     
           INV,     // 179     
           INV,     // 180     
           INV,     // 181     
           INV,     // 182     
           INV,     // 183     
           INV,     // 184     
           INV,     // 185     
           INV,     // 186     
           INV,     // 187     
           INV,     // 188     
           INV,     // 189     
           INV,     // 190     
           INV,     // 191     
           INV,     // 192     
           INV,     // 193     
           INV,     // 194     
           INV,     // 195     
           INV,     // 196     
           INV,     // 197     
           INV,     // 198     
           INV,     // 199     
           INV,     // 200     
           INV,     // 201     
           INV,     // 202     
           INV,     // 203     
           INV,     // 204     
           INV,     // 205     
           INV,     // 206     
           INV,     // 207     
           INV,     // 208     
           INV,     // 209     
           INV,     // 210     
           INV,     // 211     
           INV,     // 212     
           INV,     // 213     
           INV,     // 214     
           INV,     // 215     
           INV,     // 216     
           INV,     // 217     
           INV,     // 218     
           INV,     // 219     
           INV,     // 220     
           INV,     // 221     
           INV,     // 222     
           INV,     // 223     
           INV,     // 224     
           INV,     // 225     
           INV,     // 226     
           INV,     // 227     
           INV,     // 228     
           INV,     // 229     
           INV,     // 230     
           INV,     // 231     
           INV,     // 232     
           INV,     // 233     
           INV,     // 234     
           INV,     // 235     
           INV,     // 236     
           INV,     // 237     
           INV,     // 238     
           INV,     // 239     
           INV,     // 240     
           INV,     // 241     
           INV,     // 242     
           INV,     // 243     
           INV,     // 244     
           INV,     // 245     
           INV,     // 246     
           INV,     // 247     
           INV,     // 248     
           INV,     // 249     
           INV,     // 250     
           INV,     // 251     
           INV,     // 252     
           INV,     // 253     
           INV,     // 254     
           INV     // 255     
        };
    
    public static String toASCII(byte[] petscii)
    { 
        String out = "";
        
        for (byte b : petscii)
        {
             int i = (int)(b & 0xFF);             
             
             // Numbers
             if ((i>=48) && (i<=57))
             {
                 out += new String(new byte[] {b}, StandardCharsets.US_ASCII); // Pass-Thru
             }
             
             // Lowercase
             if ((i>=65) && (i<=90))
             {
                 out += new String(new byte[] {(byte) (b+32)}, StandardCharsets.US_ASCII);  // Add 32
             }
             
             // Uppercase
             if ((i>=193) && (i<=218))
             {
                 out += new String(new byte[] {(byte) (b-128)}, StandardCharsets.US_ASCII);  // Subtract 128
             } 
             
             // Ignore anything else
        }
        
        return out;
    }
    
    public static String toExtendedASCII(byte[] petscii)
    { 
        byte[] out = new byte[petscii.length];
        
        for (int i=0; i < petscii.length; i++)
        {
            out[i] = (byte) ascii_lookup[ExtendedAscii.getAscii(petscii[i])];             
        }
        
        String outs = new String(out);
        
        return outs;
    }
    
    public static char toExtendedASCII(byte petscii)
    { 
       return (char) ascii_lookup[ExtendedAscii.getAscii(petscii)];             
    }    
}
