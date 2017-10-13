; -------------------------------------------------------------------------
; Vortex II Music Code

.define RASTER_MUSIC  50  

; -------------------------------------------------------------------------
; IRQ Initialization

music_init:

  ; Init Player
  
  lda #$00
  jsr $3000
  
  ; Interrupt setup    
  sei                           ; disable interrupts

  lda #$7f                      ; turn off the cia interrupts
  sta $dc0d

  lda $d01a                     ; enable raster irq
  ora #$01
  sta $d01a

  lda $d011                     ; clear high bit of raster line
  and #$7f
  sta $d011

  lda #RASTER_MUSIC             ; line number to go off at
  sta $d012                     ; low byte of raster line

  ldax #irqmusic                ; get address of target routine
  stax 788                      ; put into interrupt vector

  cli                           ; re-enable interrupts
  rts                           ; return to caller

; -------------------------------------------------------------------------
; Main IRQ Code (Top)

irqmusic:
  lda $d019   ; Clear irq
  sta $d019

  ; IRQ code starts here
  jsr $3003
   
  ; Exit the current interrupt - jump to regular handler so keyboard works.
  jmp $ea31

; EOF!