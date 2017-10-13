; -------------------------------------------------------------------------
; Vortex II Sprites

PLAYER_XPOS = 172 ;24+(320/2)-(24/2)
PLAYER_YPOS = 140 ;50+(200/2)-(21/2)

; -------------------------------------------------------------------------
; Sprite Initialization

sprites_init:

  ; Move all sprites to 0,0
  lda #$00
  sta $d000
  sta $d001
  sta $d002
  sta $d003
  sta $d004
  sta $d005
  sta $d006
  sta $d007
  sta $d008
  sta $d009
  sta $d00a
  sta $d00b
  sta $d00c
  sta $d00d
  sta $d00e
  sta $d00f
  sta $d010  ; MSB
  
  ; Normal size
  sta $d017  ; YXPAND
  sta $d01d  ; XXPAND

  ; Put all sprites in front of foreground.
  sta $d01b  
  
  ; Turn on all sprites (we move to 0,0 to hide them)
  lda #$ff
  sta $d015
    
  ; All sprites are multicolor.
  sta $d01c
  
  ; Define multicolors  
  lda #01  ; White
  sta $d025  
  
  lda #11  ; Dark grey
  sta $d026
  
  
  ; Place Sprite #0 at the center of the screen (Player)  
  ldx #PLAYER_XPOS
  ldy #PLAYER_YPOS
  stx $d000
  sty $d001
  
  ; Set default color (Player may override in future)
  lda playerspritecolor
  sta $d027
  
  ; Set initial sprite 
  lda #(SPRITE_BASE+SPRITE_PLAYER) ;  Ship facing up 
  sta SPRITE_POINTERS+0  
  
  rts 

; -------------------------------------------------------------------------
; Sprite Constants

SPRITE_BASE = $30  ; pointing to $4C00  
SPRITE_POINTERS = SCREEN_BASE+$03F8

SPRITE_PLAYER = 48   ; Decimal!  ref. SpritePad.

playerspritecolor:
  .byte $06


; EOF!