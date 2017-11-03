; -------------------------------------------------------------------------
; Rogue Screen Routines

; -------------------------------------------------------------------------
; Screen Constants

CHAR_BASE   = $4000
SCREEN_BASE = $4800
COLOR_BASE  = $D800

COMMS_CHAR  = SCREEN_BASE + $03BF
COMMS_COLOR = COLOR_BASE  + $03BF

GAME_ROWS   = 17
GAME_COLS   = 21

; -------------------------------------------------------------------------
; Screen Init

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
  
  ; Assume for now, don't have to bank out BASIC
  
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
  
  
  ; Draw the screen  
  ; TODO
  
  ; Indication of network activity
  lda #$FF
  sta COMMS_CHAR
  
  rts             
  
  
; -------------------------------------------------------------------------
; Copy screen data from UDP buffer to screen
copyscreen:  
  ldx #$00
  
copy:  
  ; 17 Rows on screen
  lda udp_inp_data+1,x 
  sta SCREEN_BASE+1+(40*2),x                 ; Shifted over one column for border  
  
  lda udp_inp_data+1+(GAME_COLS*1),x
  sta SCREEN_BASE+1+(40*3),x

  lda udp_inp_data+1+(GAME_COLS*2),x
  sta SCREEN_BASE+1+(40*4),x
 
  lda udp_inp_data+1+(GAME_COLS*3),x
  sta SCREEN_BASE+1+(40*5),x
 
  lda udp_inp_data+1+(GAME_COLS*4),x
  sta SCREEN_BASE+1+(40*6),x
 
  lda udp_inp_data+1+(GAME_COLS*5),x
  sta SCREEN_BASE+1+(40*7),x
 
  lda udp_inp_data+1+(GAME_COLS*6),x
  sta SCREEN_BASE+1+(40*8),x
 
  lda udp_inp_data+1+(GAME_COLS*7),x
  sta SCREEN_BASE+1+(40*9),x
 
  lda udp_inp_data+1+(GAME_COLS*8),x
  sta SCREEN_BASE+1+(40*10),x
 
  lda udp_inp_data+1+(GAME_COLS*9),x
  sta SCREEN_BASE+1+(40*11),x
 
  lda udp_inp_data+1+(GAME_COLS*10),x
  sta SCREEN_BASE+1+(40*12),x
 
  lda udp_inp_data+1+(GAME_COLS*11),x
  sta SCREEN_BASE+1+(40*13),x
 
  lda udp_inp_data+1+(GAME_COLS*12),x
  sta SCREEN_BASE+1+(40*14),x
 
  lda udp_inp_data+1+(GAME_COLS*13),x
  sta SCREEN_BASE+1+(40*15),x
 
  lda udp_inp_data+1+(GAME_COLS*14),x
  sta SCREEN_BASE+1+(40*16),x
 
  lda udp_inp_data+1+(GAME_COLS*15),x
  sta SCREEN_BASE+1+(40*17),x
 
  lda udp_inp_data+1+(GAME_COLS*16),x
  sta SCREEN_BASE+1+(40*18),x
 
  inx
  cpx #GAME_COLS
  bne copy
  
; -------------------------------------------------------------------------
; Fill in Color Data from Lookup Table

  BORDER $01

  ldy #$00
  
copy2:  
  ; 17 Rows on screen
  ldx SCREEN_BASE+1+(40*2),y 
  lda colortable,x                 
  sta COLOR_BASE+1+(40*2),y  

  ldx SCREEN_BASE+1+(40*3),y
  lda colortable,x   
  sta COLOR_BASE+1+(40*3),y

  ldx SCREEN_BASE+1+(40*4),y
  lda colortable,x  
  sta COLOR_BASE+1+(40*4),y
 
  ldx SCREEN_BASE+1+(40*5),y
  lda colortable,x  
  sta COLOR_BASE+1+(40*5),y
 
  ldx SCREEN_BASE+1+(40*6),y
  lda colortable,x
  sta COLOR_BASE+1+(40*6),y
 
  ldx SCREEN_BASE+1+(40*7),y
  lda colortable,x
  sta COLOR_BASE+1+(40*7),y
 
  ldx SCREEN_BASE+1+(40*8),y
  lda colortable,x
  sta COLOR_BASE+1+(40*8),y
 
  ldx SCREEN_BASE+1+(40*9),y
  lda colortable,x
  sta COLOR_BASE+1+(40*9),y
 
  ldx SCREEN_BASE+1+(40*10),y
  lda colortable,x
  sta COLOR_BASE+1+(40*10),y
 
  ldx SCREEN_BASE+1+(40*11),y
  lda colortable,x
  sta COLOR_BASE+1+(40*11),y
 
  ldx SCREEN_BASE+1+(40*12),y
  lda colortable,x
  sta COLOR_BASE+1+(40*12),y
 
  ldx SCREEN_BASE+1+(40*13),y
  lda colortable,x
  sta COLOR_BASE+1+(40*13),y
 
  ldx SCREEN_BASE+1+(40*14),y
  lda colortable,x
  sta COLOR_BASE+1+(40*14),y
 
  ldx SCREEN_BASE+1+(40*15),y
  lda colortable,x
  sta COLOR_BASE+1+(40*15),y
 
  ldx SCREEN_BASE+1+(40*16),y
  lda colortable,x
  sta COLOR_BASE+1+(40*16),y
 
  ldx SCREEN_BASE+1+(40*17),y
  lda colortable,x
  sta COLOR_BASE+1+(40*17),y
 
  ldx SCREEN_BASE+1+(40*18),y
  lda colortable,x
  sta COLOR_BASE+1+(40*18),y
 
  iny
  cpy #GAME_COLS
  beq copy_x
  jmp copy2


copy_x:  
  rts

;c64 c/g Constants 
CG_BLK = 144
CG_WHT = 5
CG_RED = 28
CG_CYN = 159
CG_PUR = 156
CG_GRN = 30
CG_BLU = 31
CG_YEL = 158
CG_BRN = 149
CG_ORG = 129
CG_PNK = 150
CG_GR1 = 151
CG_GR2 = 152
CG_LGN = 153
CG_LBL = 154
CG_GR3 = 155
CG_RVS = 18  ;revs-on
CG_NRM = 146 ;revs-off

CG_DCS = 8  ;disable shift+C=
CG_ECS = 9  ;enable shift+C=

CG_LCS = 14 ;switch to lowercase
CG_UCS = 142 ;switch to uppercase

  