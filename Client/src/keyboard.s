; -------------------------------------------------------------------------
; Rogue keyboard code

;Flag that the keyboard may be used for input
KEYOK:
   .byte $01

; ---------------------------------------------------------------------
; Keyboard handler.

READKEYBOARD:  
  lda KEYOK   
  bne KEYSTART
:
  rts
  
  // http://sta.c64.org/cbm64pet.html
  
KEYSTART:
   jsr $FFE4    
   beq :-    ; No key pressed, return

   cmp #87		;w - north
   beq north
   
   cmp #65		;a - west
   beq west
   
   cmp #68		;d - east
   beq east
		
   cmp #88		;x - south
   beq south
   
   cmp #83		;s - south (duplicate control)
   beq south
   
   cmp #43      ;+ Pick up
   beq pickup
   
   cmp #45      ;- Drop
   beq drop
   
   ; No match
   rts

north:		
   lda #ACTION_MOVE
   ldx #DIRECTION_NORTH
   jmp sendaction
		
south:	
   lda #ACTION_MOVE
   ldx #DIRECTION_SOUTH
   jmp sendaction	
   
west:	
   lda #ACTION_MOVE
   ldx #DIRECTION_WEST
   jmp sendaction

east:	
   lda #ACTION_MOVE
   ldx #DIRECTION_EAST
   jmp sendaction

pickup:
   lda #ACTION_PICKUP
   jmp sendaction
   
drop:
   lda #ACTION_DROP
   jmp sendaction