; -------------------------------------------------------------------------
; Vortex II Sound Code


;------------------------------------------------------------------------------
; Setup - clear sound chip and set maximum volume!

sound_setup:
  ldx #$00
  txa
:
  sta $d400,x
  inx
  cpx #$19
  bne :-
  
  lda #$0f
  sta $d418
  rts

  
; EOF!