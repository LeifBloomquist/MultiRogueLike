; -------------------------------------------------------------------------
; Rogue keyboard code

;Flag that the keyboard may be used for input
KEYOK:
   .byte $01

; ---------------------------------------------------------------------
; Keyboard handler.

KEYREPEAT:
  lda #$FF   ; All keys repeat
  sta $028A   
  rts

READKEYBOARD:  
  lda KEYOK   
  bne KEYSTART
:
  rts
    
KEYSTART:
   jsr $FFE4    
   beq :-    ; No key pressed, return with no action

   jmp sendaction