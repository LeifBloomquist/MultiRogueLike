; -------------------------------------------------------------------------
; Rogue IRQ Code

.define IRQ_TOP    50

; -------------------------------------------------------------------------
; IRQ Initialization

irq_init:
  sei                           ; disable interrupts

  lda #$7f                      ; turn off the cia interrupts
  sta $dc0d

  lda $d01a                     ; enable raster irq
  ora #$01
  sta $d01a

  lda $d011                     ; clear high bit of raster line
  and #$7f
  sta $d011

  lda #IRQ_TOP                  ; line number to go off at
  sta $d012                     ; low byte of raster line

  ldax #irqtop                  ; get address of target routine
  stax 788                      ; put into interrupt vector

  cli                           ; re-enable interrupts
  rts                           ; return to caller

; -------------------------------------------------------------------------
; Main IRQ Code (Top)

irqtop:
  lda $d019   ; Clear irq
  sta $d019

  ; IRQ code starts here
  inc frametype  
  lda frametype
  
  cmp #$01
  beq irq_update
  
  cmp #$02
  beq irq_process
  
  cmp #$03
  beq irq_screen

irq_reset:
  lda #$00
  sta frametype
  
irq_x:
  BORDER $00
     
  ; Exit the current interrupt.  
  jmp $EA31
  
  ; Old method, that does not scan the keyboard
  ;pla
  ;tay                           
  ;pla                           
  ;tax                          
  ;pla                        
  ;rti

; -------------------------------------------------------------------------
; Routines called within the IRQ.

irq_update:
  BORDER $03
  
  ; Read joystick for player actions
  jsr READJOYSTICK
  
  ; Read keyboard for player actions
  jsr READKEYBOARD
  
  ; Any pending action?  If not, skip.
  lda PLAYER_ACTION
  beq :+
  
  jsr sendaction
  
  lda #$00
  sta PLAYER_ACTION   ; Action has been sent
:
  jmp irq_x

; Background network processing
irq_process:
  BORDER $02
  jsr ip65_process 
  jmp irq_x
   
; Copy screen, if received
irq_screen:
  BORDER $07
  lda screenreceived
  beq :+

  jsr copyscreen
  jsr docolorlookup
  lda #$00
  sta screenreceived
  
:  
  jmp irq_reset    ; For last frame type only

; -------------------------------------------------------------------------
; Misc flags used by the IRQ
  
frametype:
  .byte $00     ; Used to round-robin through frame types

screenreceived:
  .byte $00     ; Set to 1 when a full screen is received, so irq can process.
    
; EOF!