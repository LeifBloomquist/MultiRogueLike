; -------------------------------------------------------------------------
; Copy screen data from UDP buffer to screen

copyscreen:
  ldx #$00
  
copy:  
  ; 17 Rows on screen
  lda udp_inp_data+1,x 
  sta SCREEN_BASE+1+(40*1),x                 ; Shifted over one column for border  
  
  lda udp_inp_data+1+(GAME_COLS*1),x
  sta SCREEN_BASE+1+(40*2),x

  lda udp_inp_data+1+(GAME_COLS*2),x
  sta SCREEN_BASE+1+(40*3),x
 
  lda udp_inp_data+1+(GAME_COLS*3),x
  sta SCREEN_BASE+1+(40*4),x
 
  lda udp_inp_data+1+(GAME_COLS*4),x
  sta SCREEN_BASE+1+(40*5),x
 
  lda udp_inp_data+1+(GAME_COLS*5),x
  sta SCREEN_BASE+1+(40*6),x
 
  lda udp_inp_data+1+(GAME_COLS*6),x
  sta SCREEN_BASE+1+(40*7),x
 
  lda udp_inp_data+1+(GAME_COLS*7),x
  sta SCREEN_BASE+1+(40*8),x
 
  lda udp_inp_data+1+(GAME_COLS*8),x
  sta SCREEN_BASE+1+(40*9),x
 
  lda udp_inp_data+1+(GAME_COLS*9),x
  sta SCREEN_BASE+1+(40*10),x
 
  lda udp_inp_data+1+(GAME_COLS*10),x
  sta SCREEN_BASE+1+(40*11),x
 
  lda udp_inp_data+1+(GAME_COLS*11),x
  sta SCREEN_BASE+1+(40*12),x
 
  lda udp_inp_data+1+(GAME_COLS*12),x
  sta SCREEN_BASE+1+(40*13),x
 
  lda udp_inp_data+1+(GAME_COLS*13),x
  sta SCREEN_BASE+1+(40*14),x
 
  lda udp_inp_data+1+(GAME_COLS*14),x
  sta SCREEN_BASE+1+(40*15),x
 
  lda udp_inp_data+1+(GAME_COLS*15),x
  sta SCREEN_BASE+1+(40*16),x
 
  lda udp_inp_data+1+(GAME_COLS*16),x
  sta SCREEN_BASE+1+(40*17),x
 
  inx
  cpx #GAME_COLS
  bne copy

; -------------------------------------------------------------------------
; Copy server messages from UDP buffer to screen

copymessages:  
  ldx #$00
  
copym:
  lda udp_inp_data+358,x 
  sta SCREEN_BASE+(40*20),x 
  
  inx
  cpx #160   ; 40x4 messages
  bne copym

  ; Current Cell
  ldx udp_inp_data+518
  stx CELL_CHAR

  ; Held - Left
  ldx udp_inp_data+519
  stx LEFT_CHAR
  
  ; Held - Right
  ldx udp_inp_data+520
  stx RIGHT_CHAR
  
  ; Health
  lda udp_inp_data+521
  sta HEALTH_CHARS
  lda udp_inp_data+522
  sta HEALTH_CHARS+1
  lda udp_inp_data+523
  sta HEALTH_CHARS+2

  ; Sound effects
  lda udp_inp_data+524
  cmp soundcounter
  beq nosound
  
  sta soundcounter
  lda udp_inp_data+525
  jsr sound_play

  ; TODO, XP, gold?
nosound:
  
  rts