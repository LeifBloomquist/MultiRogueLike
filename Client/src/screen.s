; -------------------------------------------------------------------------
; Rogue Screen

; -------------------------------------------------------------------------
; Screen Initialization

GAME_ROWS=17
GAME_COLS=21

screen_init: 
  ; Background Colors
  lda #$00
  sta $d020
  sta $d021  
  
  ; Extended background colors
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
  lda #$0A   ; Remember, multicolor character mode 
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
  
  jsr finexy
  
  rts             


; -------------------------------------------------------------------------
; Set Fine X and Fine Y  (Also sets 38 Columns and Extended color mode as a by-product)

finexy:
 ; Fine X
  lda $d016
  and #%11100000
  ora #%00010000  ; Multi
  ora finex
  sta $d016

  ; Fine Y
  lda $d011
  and #%11110000
  ora finey
  sta $d011
  
  rts

finex:
  .byte $00

finey:
  .byte $00    

; -------------------------------------------------------------------------
; Screen Constants

CHAR_BASE   = $4000
SCREEN_BASE = $4800
COLOR_BASE  = $D800

;c64 c/g 
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
CG_RVS = 18 ;revs-on
CG_NRM = 146 ;revs-off

CG_DCS = 8  ;disable shift+C=
CG_ECS = 9  ;enable shift+C=

CG_LCS = 14 ;switch to lowercase
CG_UCS = 142 ;switch to uppercase

  