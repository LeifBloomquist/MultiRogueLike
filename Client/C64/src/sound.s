; -------------------------------------------------------------------------
; Rogue Sound Code

.export _sound_init
.export _sound_play

SOUND_NONE         = 0
SOUND_PLAYER_STEP  = 1
SOUND_BLOCKED      = 2
SOUND_ATTACK       = 3
SOUND_MISS         = 4
SOUND_ATTACKED     = 5
SOUND_MONSTER_STEP = 6

;------------------------------------------------------------------------------
; Setup - clear sound chip and set maximum volume!

_sound_init:
sound_init:
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

;------------------------------------------------------------------------------
; Sound Effects Dispatcher - C entry point
; Defined with fastcall, so param should be in A?  
; Ref https://github.com/cc65/wiki/wiki/Parameter-passing-and-calling-conventions

_sound_play:

;------------------------------------------------------------------------------
; Sound Effects Dispatcher - sound to play in A
; Check the SOUND_ constants in Constants.java

sound_play:
   cmp #SOUND_NONE 
   beq sound_x
   
   cmp #SOUND_PLAYER_STEP 
   beq sound_step

   cmp #SOUND_BLOCKED 
   beq sound_blocked
   
   cmp #SOUND_ATTACK 
   beq sound_attack

   cmp #SOUND_ATTACKED 
   beq sound_attacked
   
sound_x:
   rts

;------------------------------------------------------------------------------
; Player Steps - Use Voice 1

; TODO - these are for voice 2, fix
sound_step:
  lda #$00
  sta $d40c  
  lda #$02
  sta $d40d
  lda #$30    ; Pitch 
  sta $d408
  lda #$00
  sta $d407
  lda #$81
  sta $d40b
  lda #$80
  sta $d40b
  rts
  


; TODO - these are for voice 2, fix
sound_blocked:
  lda #$00
  sta $d40c  
  lda #$02
  sta $d40d
  lda #$03    ; Pitch 
  sta $d408
  lda #$00
  sta $d407
  lda #$81
  sta $d40b
  lda #$80
  sta $d40b
  rts
  
;------------------------------------------------------------------------------
sound_attack: 
  lda #$00    ; 0 Attack, 0 Decay
  sta $d40c  
  lda #$03    ; 0 Sustain, ?? release 
  sta $d40d
  lda #$10    ; Pitch (High)
  sta $d408
  lda #$00    ; Pitch (Low)
  sta $d407
  lda #$81    ; Trigger
  sta $d40b
  lda #$80    ; Release
  sta $d40b
  rts

;------------------------------------------------------------------------------
sound_attacked: 
  lda #$00    ; 0 Attack, 0 Decay
  sta $d40c  
  lda #$03    ; 0 Sustain, ?? release 
  sta $d40d
  lda #$05    ; Pitch (High)
  sta $d408
  lda #$00    ; Pitch (Low)
  sta $d407
  lda #$81    ; Trigger
  sta $d40b
  lda #$80    ; Release
  sta $d40b
  rts


; EOF!