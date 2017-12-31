package com.schemafactor.rogueserver.common;

// ANSI Codes    

public class EscapeSequences
{
    public static final int ESC = 27;
    
    // http://www.termsys.demon.co.uk/vtansi.htm
    public static final char   ANSI_ESC       = (char) 27;
    public static final String ANSI_HOME      = ANSI_ESC + "[H";
    public static final String ANSI_CLEAR     = ANSI_ESC + "[2J" + ANSI_HOME;

    // 91 = ']'
    
    public static final int[] ESCAPE_UP          = {ANSI_ESC, 91, 'A'};  
    public static final int[] ESCAPE_DOWN        = {ANSI_ESC, 91, 'B'};
    public static final int[] ESCAPE_RIGHT       = {ANSI_ESC, 91, 'C'};
    public static final int[] ESCAPE_LEFT        = {ANSI_ESC, 91, 'D'};
    
    public static final int[] ESCAPE_SHIFT_UP    = {ANSI_ESC, 91, 49, 59, 50, 'A'}; 
    public static final int[] ESCAPE_SHIFT_DOWN  = {ANSI_ESC, 91, 49, 59, 50, 'B'}; 
    public static final int[] ESCAPE_SHIFT_RIGHT = {ANSI_ESC, 91, 49, 59, 50, 'C'};   
    public static final int[] ESCAPE_SHIFT_LEFT  = {ANSI_ESC, 91, 49, 59, 50, 'D'};   
                                             
    public static final int[] ESCAPE_F1          = {ANSI_ESC, 91, 49, 49, 126};
    public static final int[] ESCAPE_F2          = {ANSI_ESC, 91, 49, 50, 126};      
}
