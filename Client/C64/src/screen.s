; -------------------------------------------------------------------------
; Rogue Screen Routines

.import default_screen

.export _screen_init

; -------------------------------------------------------------------------
; Screen Constants

.export CHAR_BASE    = $4000
.export SCREEN_BASE  = $4800
.export COLOR_BASE   = $D800

.export CELL_CHAR    = SCREEN_BASE + $006F
.export CELL_COLOR   = COLOR_BASE  + $006F

.export LEFT_CHAR    = SCREEN_BASE + $00BF
.export LEFT_COLOR   = COLOR_BASE  + $00BF

.export RIGHT_CHAR   = LEFT_CHAR   + 40
.export RIGHT_COLOR  = LEFT_COLOR  + 40

HEALTH_CHARS = RIGHT_CHAR  + 80

COMMS_CHAR  = SCREEN_BASE  + $03E7
COMMS_COLOR = COLOR_BASE   + $03E7

GAME_ROWS   = 17
GAME_COLS   = 21

COLOR_WHITE	= 1
COLOR_GREY1	= 11
COLOR_GREY2	= 12
COLOR_GREY3 = 15

; -------------------------------------------------------------------------
; Screen Init

_screen_init: 
screen_init: 
  ; Background Colors
  lda #$00
  sta $d020
  sta $d021  
  
  ; Extended background colors (not used)
  lda #$01
  sta $d022  
  
  lda #$0F
  sta $d023  
  
  ; Blank screen
  lda #00
  ldx #$00     
:
  sta SCREEN_BASE+$000,x
  sta SCREEN_BASE+$100,x
  sta SCREEN_BASE+$200,x
  sta SCREEN_BASE+$2E8,x
  inx
  bne :-	
  
  
; Explicitly paint screen color, for older kernals
  lda #$01 
  ldx #$00  
:
  sta COLOR_BASE+$000,x
  sta COLOR_BASE+$100,x
  sta COLOR_BASE+$200,x
  sta COLOR_BASE+$2E8,x
  inx
  bne :-    
    
  ; Grayscale gradient for messages - brighter=newest

  ldx #$00

gray: 
  lda #COLOR_GREY1
  sta COLOR_BASE+(40*20),x 
  
  lda #COLOR_GREY2
  sta COLOR_BASE+(40*21),x 
  
  lda #COLOR_GREY3
  sta COLOR_BASE+(40*22),x 
  
  lda #COLOR_WHITE
  sta COLOR_BASE+(40*23),x 
  
  inx
  cpx #40
  bne gray
  
  ; Assume for now, don't have to bank out BASIC

setupvic:  
  ; Set up VIC to Bank 1  ($4000-$7FFF)
  lda $DD00
  and #%11111100
  ora #%00000010 ; $DD00 = %xxxxxx10 -> bank1: $4000-$7fff
  sta $DD00

  ; Set screen to offset $0800 and character set to offset $0000
  ; $D018 = %0010xxxx -> screenmem is at $0800
  ; $D018 = %xxxx000x -> charmem   is at $0000 
  lda #%00100000
  sta $d018
  
  ; Draw the default screen  
drawscreen:

  ldx #$00     
:
  lda default_screen+$000,x
  sta SCREEN_BASE+$000,x
  lda default_screen+$100,x  
  sta SCREEN_BASE+$100,x
  lda default_screen+$200,x
  sta SCREEN_BASE+$200,x
  lda default_screen+$2E8,x
  sta SCREEN_BASE+$2E8,x
  inx
  bne :-	
  
  ; Indication of network activity
  lda #$FF
  sta COMMS_CHAR
  
  rts             

; EOF 