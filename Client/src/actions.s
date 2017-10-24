
.define ACTION_NONE    0
.define ACTION_MOVE    1
.define ACTION_USE     2
.define ACTION_DIG     3
.define ACTION_ATTACK  4
.define ACTION_EXAMINE 5
.define ACTION_OPEN    6
.define ACTION_CLOSE   7
.define ACTION_CAST    8
.define ACTION_PICKUP  9
.define ACTION_DROP    10

.define DIRECTION_NONE   0
.define DIRECTION_NORTH  1
.define DIRECTION_NE     2
.define DIRECTION_EAST   3
.define DIRECTION_SE     4
.define DIRECTION_SOUTH  5
.define DIRECTION_SW     6
.define DIRECTION_WEST   7
.define DIRECTION_NW     8
.define DIRECTION_UP     9
.define DIRECTION_DOWN   10

ACTIONCOUNTER:
   .byte 0  
   
PLAYER_ACTION:
   .byte 0  

PLAYER_ACTION_PARAM1:
   .byte 0 
   
PLAYER_ACTION_PARAM2:
   .byte 0 
