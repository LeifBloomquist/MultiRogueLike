package com.schemafactor.rogueserver.common;

public class EscapeSequences
{
    public static final int ESC = 27;

    // 91 = ']'
    
    public static final int[] ESCAPE_UP          = {27, 91, 'A'};  
    public static final int[] ESCAPE_DOWN        = {27, 91, 'B'};
    public static final int[] ESCAPE_RIGHT       = {27, 91, 'C'};
    public static final int[] ESCAPE_LEFT        = {27, 91, 'D'};
    
    public static final int[] ESCAPE_SHIFT_UP    = {27, 91, 49, 59, 50, 'A'}; 
    public static final int[] ESCAPE_SHIFT_DOWN  = {27, 91, 49, 59, 50, 'B'}; 
    public static final int[] ESCAPE_SHIFT_RIGHT = {27, 91, 49, 59, 50, 'C'};   
    public static final int[] ESCAPE_SHIFT_LEFT  = {27, 91, 49, 59, 50, 'D'};   

    public static final int[] ESCAPE_F1          = {27, 91, 49, 49, 126};
    public static final int[] ESCAPE_F2          = {27, 91, 49, 50, 126};      
}