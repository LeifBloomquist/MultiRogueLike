; -------------------------------------------------------------------------
; Rogue Default Screen

; Common characters
W = $E0

default_screen:
   .byte W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,0,$AD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,"I SEE:",0,$AD,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,"LEFT:",0,0,$AD,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,"RIGHT:",0,$AD,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,"HEALTH:",0,$AD,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ;.byte "12345abcdeABCDE!@#$%12345abcdeABCDE!@#$%"
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255