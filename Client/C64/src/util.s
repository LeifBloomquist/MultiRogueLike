;-------------------------------------------------------------------------
; Rogue Source Code
; Misc utilities

; ==============================================================
; One second delay - thanks groepaz
; ==============================================================

ONESECOND:
    ldx #60   ; - NTSC   use #50 for PAL
lp:
    lda #$f8
lp2:
    cmp $d012    ; reached the line
    bne lp2
lp3:
    cmp $d012    ; past the line
    beq lp3
    
    ; Count down
    dex
    bne lp

ONESECOND_x:   
    rts

; ==============================================================
; One second delay that can be interrupted by a packet
; ==============================================================

WAITONE:
    ldx #60  ; - NTSC   use #50 for PAL
alp:
    lda #$f8
alp2:
    cmp $d012    ; reached the line
    bne alp2
alp3:
    cmp $d012    ; past the line
    beq alp3
    
    ; Early exit if a packet is received
    lda gamepacketreceived
    bne WAITONE_x           ; exit if flag = 1
    
    ; Count down
    dex
    bne alp

WAITONE_x:    
    rts
    
; -------------------------------------------------------------------------
; Wait 
waitforkey:
   
  ; Wait for key
  ; kernal_print WAITMESSAGE
    
gak0:
	jsr $FFE4  ;GETIN
	beq gak0   
  rts
  
  
WAITMESSAGE:
  .byte "press any key to continue."
  .byte 0

; EOF