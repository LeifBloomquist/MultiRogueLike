package com.schemafactor.rogueserver.common;

import java.time.Duration;

public class Constants 
{
    // Server version
    public static final double VERSION        = 0.001;
    
    // Game-specific constants
    public static final int TICK_TIME         = 50;     // milliseconds
    public static final int DUNGEON_SIZE      = 100;    // Per Side
    public static final int DUNGEON_DEPTH     = 10;     // Levels
    
    public static final long NETWORK_TIMEOUT  = 100;    // Seconds
    
    // Screen constants
    public static final int SCREEN_WIDTH      = 21;
    public static final int SCREEN_HEIGHT     = 17;
    public static final int SCREEN_SIZE       = SCREEN_WIDTH*SCREEN_HEIGHT;
    
    // Packet types
    public static final byte CLIENT_ANNOUNCE  = 1;
    public static final byte CLIENT_UPDATE    = 2;
    
    public static final byte PACKET_ANN_REPLY = (byte) 128;
    public static final byte PACKET_UPDATE    = (byte) 129;
    
    // Actions
    public static final byte ACTION_HEARTBEAT  = 0;
    public static final byte ACTION_MOVE       = 1;
    public static final byte ACTION_USE        = 2;
    public static final byte ACTION_DIG        = 3;
    public static final byte ACTION_ATTACK     = 4;
    public static final byte ACTION_EXAMINE    = 5;
    public static final byte ACTION_OPEN       = 6;
    public static final byte ACTION_CLOSE      = 7;
    public static final byte ACTION_CAST       = 8;
    public static final byte ACTION_PICKUP     = 9;
    public static final byte ACTION_DROP       = 10;
    
    // Directions
    public static final byte DIRECTION_NONE    = 0;
    public static final byte DIRECTION_NORTH   = 1;
    public static final byte DIRECTION_NE      = 2;
    public static final byte DIRECTION_EAST    = 3;
    public static final byte DIRECTION_SE      = 4;
    public static final byte DIRECTION_SOUTH   = 5;
    public static final byte DIRECTION_SW      = 6;
    public static final byte DIRECTION_WEST    = 7;
    public static final byte DIRECTION_NW      = 8;
    public static final byte DIRECTION_UP      = 9;
    public static final byte DIRECTION_DOWN    = 10;

    // Cell Types
    public static final byte CHAR_PLAYER_WIZARD  =  (byte) 200;
    public static final byte CHAR_MONSTER_SPIDER =  (byte) 101;    
    
    // C64 colors
    public static final byte COLOR_BLACK      = 0;
    public static final byte COLOR_WHITE      = 1;
    public static final byte COLOR_RED        = 2;
    public static final byte COLOR_CYAN       = 3;
    public static final byte COLOR_PURPLE     = 4;
    public static final byte COLOR_GREEN      = 5;
    public static final byte COLOR_BLUE       = 6;
    public static final byte COLOR_YELLOW     = 7;
    public static final byte COLOR_ORANGE     = 8;
    public static final byte COLOR_BROWN      = 9;
    public static final byte COLOR_LIGHTRED   = 10;
    public static final byte COLOR_GREY1      = 11;
    public static final byte COLOR_GREY2      = 12;
    public static final byte COLOR_LIGHTGREEN = 13;
    public static final byte COLOR_LIGHTBLUE  = 14;
    public static final byte COLOR_GREY3      = 15;
}
