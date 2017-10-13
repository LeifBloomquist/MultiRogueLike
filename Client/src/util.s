;-------------------------------------------------------------------------
; Vortex II Source Code
; Misc utilities

; -------------------------------------------------------------------------
; Wait 
waitforkey:
   
  ; Wait for key
  kernal_print WAITMESSAGE
    
gak0:
	jsr $FFE4  ;GETIN
	beq gak0   
  rts
  
  
WAITMESSAGE:
  .byte "press any key to continue."
  .byte 0


;======================================================================
;Input a string and store it in GOTINPUT, terminated with a null byte.
;x:a is a pointer to the allowed list of characters, null-terminated.
;max # of chars in y returns num of chars entered in y.
;======================================================================

GETIN = $ffe4

; Example usage
;FILTERED_TEXT
;  lda #<TEXT
;  ldx #>TEXT
;  ldy #38
  ;Drop through

; Main entry
FILTERED_INPUT:
  sty MAXCHARS
  sta CHECKALLOWED+1
  stx CHECKALLOWED+2

  ;Zero characters received.
  lda #$00
  sta INPUT_Y
  sta $CC       ;Flash cursor

;Wait for a character.
INPUT_GET:
  jsr GETIN
  beq INPUT_GET

  sta LASTCHAR

  cmp #$14               ;Delete
  beq DELETE

  cmp #$0d               ;Return
  beq INPUT_DONE

  ;Check the allowed list of characters.
  ldx #$00
CHECKALLOWED:
  lda $FFFF,x           ;Overwritten
  beq INPUT_GET         ;Reached end of list (0)

  cmp LASTCHAR
  beq INPUTOK           ;Match found

  ;Not end or match, keep checking
  inx
  jmp CHECKALLOWED

INPUTOK:
  ;End reached?
  lda INPUT_Y
  cmp MAXCHARS
  beq INPUT_GET  ;Yes, so don't allow character
  
  ; Store the character                        
  lda LASTCHAR          ;Get the char back
  ldy INPUT_Y
  sta GOTINPUT,y        ;Add it to string
  jsr $ffd2             ;Print it

  inc INPUT_Y           ;Next character  
  jmp INPUT_GET

INPUT_DONE:
   ldy INPUT_Y
   
   ; Must enter at least one character.
   bne INPUT_OK  
   jmp INPUT_GET

INPUT_OK:
   lda #$00
   sta GOTINPUT,y   ;Zero-terminate
   
   lda #$01
   sta $CC  ; Turn cursor off again   
   rts

; Delete last character.
DELETE:
  ;First, check if we're at the beginning.  If so, just exit.
  lda INPUT_Y
  bne DELETE_OK
  jmp INPUT_GET

  ;At least one character entered.
DELETE_OK:
  ;Move pointer back.
  dec INPUT_Y

  ;Store a zero over top of last character, just in case no other characters are entered.
  ldy INPUT_Y
  lda #$00
  sta GOTINPUT,y

  ;Print the delete char
  lda #$14
  jsr $ffd2

  ;Wait for next char
  jmp INPUT_GET


;=================================================
;Some example filters
;=================================================

IPADDRESS:
  .byte "1234567890.",0

ALPHANUMERIC:
  .byte "ABCDEFGHIJKLMNOPQRSTUVWXYZ"    ; Lowercase
  .byte 193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218 ; Uppercase
    
NUMBERS:
  .byte "1234567890"
  .byte 0

;=================================================
MAXCHARS:
  .byte $00

LASTCHAR:
  .byte $00

INPUT_Y:
  .byte $00

GOTINPUT:
  .res 40
  
; EOF!