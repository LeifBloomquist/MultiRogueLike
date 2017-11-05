; -------------------------------------------------------------------------
; Rogue Default Screen

; Common characters
W = $E0

default_screen:
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 0
   .byte W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,0,$AD,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 1
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 2
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,$89,0,$93,$85,$85,$BA,0,$AD,0,0,0,0,0,0,0,0    ; Line 3
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 4
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,$8C,$85,$86,$94,$BA,0,0,$AD,0,0,0,0,0,0,0,0    ; Line 5
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,$92,$89,$87,$88,$94,$BA,0,$AD,0,0,0,0,0,0,0,0    ; Line 6
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 7
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 8
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 9
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 10
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 11
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 12
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 13
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 14
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 15
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 16
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 17   
   .byte W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 18
   .byte W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 19
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 20
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 21
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 22
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0    ; Line 23
  ;  .byte "12345abcdeABCDE!@#$%12345abcdeABCDE!@#$%"
   .byte 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255  ; Line 24
