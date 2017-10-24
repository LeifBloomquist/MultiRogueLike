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
  
KEYSTART:
   jsr $FFE4    
   beq :-    ; No key pressed, return

   cmp #87		;W - up
   beq up
		
   cmp #83		;s - down
   beq down

   cmp #65		;a - left
   beq left

   cmp #68		;d - right
   beq right
   
   ; No match
   rts

up:		
   lda #ACTION_MOVE
   ldx #DIRECTION_NORTH
   jmp sendaction
		
down:	
   lda #ACTION_MOVE
   ldx #DIRECTION_SOUTH
   jmp sendaction	
   
left:	
   lda #ACTION_MOVE
   ldx #DIRECTION_WEST
   jmp sendaction

right:	
   lda #ACTION_MOVE
   ldx #DIRECTION_EAST
   jmp sendaction
