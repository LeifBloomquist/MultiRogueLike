;------------------------------------------------------------------------
; Rogue Source Code

; -------------------------------------------------------------------------
; Includes

  .include "ip65/inc/common.i"
  .include "ip65/inc/commonprint.i"
  .include "ip65/inc/net.i"
  
  .include "macros.s"
  
.ifndef KPR_API_VERSION_NUMBER
  .define EQU     =
  .include "ip65/inc/kipper_constants.i"
.endif

; -------------------------------------------------------------------------
; Imports
  
  .import  __CODE_LOAD__
  .import  __CODE_SIZE__
  .import  __RODATA_SIZE__
  .import  __DATA_SIZE__
  
	.segment "CLIENTCODE"    ; Program start

; -------------------------------------------------------------------------
; Program entry point

init:
  lda #$00
  sta $d020
  sta $d021
  
  ; Turn off BASIC ROM
  ;lda #$36
  ;sta $01
  

  ; jsr music_init
  ; jsr station  
  jsr login

  lda #$02
  sta $d020
  lda #$00
  sta $d021
  
  jsr network_init_dhcp  
  jsr network_init_udp  
    
  jsr irq_init   ; Needed for network, kills music

  jsr sound_init
 
  ; Wait for the first server packet
  kernal_print SERVERMESSAGE
  
:
  jsr sendannounce
  jsr WAITONE               ; Wait one second so not flooding network
  lda gamepacketreceived
  beq :-
  
  kernal_print OKMESSAGE
    
  jsr screen_init
  
  jsr KEYREPEAT
  
; -------------------------------------------------------------------------
; Main Loop - Idle.

loop:
  jmp loop


; -------------------------------------------------------------------------
; More Includes  

  .include "actions.s"
  .include "joystick.s"
  .include "keyboard.s"
  .include "colordefs.s"
  .include "screen.s"
  .include "irq.s" 
  .include "network.s" 
  .include "music.s"
  .include "sound.s"
  .include "login.s"  
  .include "util.s"
  .include "input.s"
  .include "copyscreen.s"
  
; -------------------------------------------------------------------------
; Binary data is loaded from server via TFTP - no includes needed
  
; EOF!