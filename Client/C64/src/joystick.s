; -------------------------------------------------------------------------
; Rogue Joystick code and vars

;Flag that the joystick may be used for input
JOYOK:
   .byte $01
  
; ---------------------------------------------------------------------
; Joystick handler.

READJOYSTICK:  
  lda JOYOK   
  bne JOYSTART
  rts
  
JOYSTART:
   ; Determine direction with a lookup table
   lda $dc00  ; Port 2
   and #$1F
   tax
   lda joydirections,x
   cmp #$ff ; leave as-is
   beq JOY_x
   
   jmp sendaction
   
; ---------------------------------------------------------------------
JOY_x:
  rts
   
; Lookup table to map joystick input to a keystroke.  
; $FF=leave as before (centered or impossible)
joydirections:  
  .byte $FF  ;  0     
  .byte $FF  ;  1
  .byte $FF  ;  2
  .byte $FF  ;  3
  .byte $FF  ;  4
  .byte $C3  ;  5 = Down+Right+Button  (Attack SouthEast)
  .byte $C5  ;  6 = Up+Right+Button  (Attack NorthEast)
  .byte $C4  ;  7 = Right+Button  (Attack East) 
  .byte $FF  ;  8
  .byte $DA  ;  9 = Down+Left+Button  (Attack SouthWest)
  .byte $D1  ; 10 = Up+Left+Button  (Attack NorthWest)
  .byte $C1  ; 11 = Left+Button  (Attack West)
  .byte $FF  ; 12
  .byte $D8  ; 13 = Down+Button  (Attack South)
  .byte $D7  ; 14 = Up+Button  (Attack North)
  .byte '*'  ; 15 = Centered+Button  (Use)
  .byte $FF  ; 16
  .byte $FF  ; 17
  .byte $FF  ; 18
  .byte $FF  ; 19
  .byte $FF  ; 20
  .byte 'C'  ; 21 = Down+Right
  .byte 'E'  ; 22 = Up+Right
  .byte 'D'  ; 23 = Right
  .byte $FF  ; 24
  .byte 'Z'  ; 25 = Down+Left
  .byte 'Q'  ; 26 = Up+Left
  .byte 'A'  ; 27 = Left
  .byte $FF  ; 28
  .byte 'X'  ; 29 = Down
  .byte 'W'  ; 30 = Up
  .byte $FF  ; 31 = Centered+No Button
  
; EOF!