; -------------------------------------------------------------------------
; Rogue Joystick code and vars

;Flag that the joystick may be used for input
JOYOK:
   .byte $01

;This holds the joystick button state
JOYBUTTON:
  .byte $00
  
; ---------------------------------------------------------------------
; Joystick handler.

READJOYSTICK:  
  lda JOYOK   
  bne JOYSTART
  rts
  
JOYSTART:

   ; Determine direction with a lookup table
   lda $dc00  ; Port 2
   and #$0F
   tax
   lda spritedirections,x
   cmp #$ff ; leave as-is
   beq JOY_x
   
   sta MOVE_DIRECTION
   lda #ACTION_MOVE
   sta PLAYER_ACTION 
   
; ---------------------------------------------------------------------
JOY_x:
  rts
   
; Lookup table to map joystick input to a direction.  
; $FF=leave as before (centered or impossible)
spritedirections:  
  .byte $ff,$ff,$ff,$ff
  .byte $ff,$03,$01,$02
  .byte $ff,$05,$07,$06
  .byte $ff,$04,$00,$ff

; EOF!