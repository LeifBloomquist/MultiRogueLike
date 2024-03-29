;------------------------------------------------------------------------
; Rogue 64 Bootloader Source Code

; -------------------------------------------------------------------------
; Includes

  .include "ip65/inc/common.i"
  .include "ip65/inc/commonprint.i"
  .include "ip65/inc/net.i"
  
  .include "macros.s"
  .include "colordefs.s"
  
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
  
	.segment "STARTUP"    ;this is what gets put at the start of the file on the C64

; -------------------------------------------------------------------------
; Load address and BASIC stub

	.word basicstub		; load address

basicstub:
	.word @nextline
	.word 2018
	.byte $9e
	.byte <(((init / 1000) .mod 10) + $30)
	.byte <(((init / 100 ) .mod 10) + $30)
	.byte <(((init / 10  ) .mod 10) + $30)
	.byte <(((init       ) .mod 10) + $30)
	.byte 0
@nextline:
	.word 0

.code

init:
  lda #$00
  sta $d020
  sta $d021

  jsr network_init_dhcp  
  jsr tftpget
   
    
; -------------------------------------------------------------------------
; Main Entry point - Jump into the downloaded code.

main: 
  ; Save bootloader version if needed
  lda #13     ; decimal 1.3
  sta $CFFF

  jmp $6000


; -------------------------------------------------------------------------
; Network code - trimmed to absolute minimum


; -------------------------------------------------------------------------
; IP65 Imports

  .import tftp_download
    .import tftp_load_address
    .importzp tftp_filename
    .import tftp_ip 
    
  .import tftp_clear_callbacks 
  .import tftp_set_callback_vector
  .import copy_tftp_block_to_ram
  
  .import cfg_tftp_server

; -------------------------------------------------------------------------
; Network Initialization

network_init_dhcp:
  
  kernal_print NETWORKMESSAGE
  
  init_ip_via_dhcp   
  
  lda TFTP_SERVER_IP+0
  sta cfg_tftp_server+0 
  sta tftp_ip+0 
  lda TFTP_SERVER_IP+1
  sta cfg_tftp_server+1
  sta tftp_ip+1
  lda TFTP_SERVER_IP+2
  sta cfg_tftp_server+2
  sta tftp_ip+2
  lda TFTP_SERVER_IP+3
  sta cfg_tftp_server+3
  sta tftp_ip+3
  
  jsr print_cr
  jsr print_ip_config
  jsr print_cr  
  rts
    
; -------------------------------------------------------------------------
; TFTP Get

tftpget:
  kernal_print DOWNLOADMESSAGE

  ldax #tftpname
  stax tftp_filename
  
  ldax #$4000
  stax tftp_load_address
  
  jsr tftp_clear_callbacks    
  ldax #tftpprogress
  jsr tftp_set_callback_vector
  
  jsr tftp_download  
  bcs tftperror

tftpok: 
  kernal_print OKMESSAGE 
  rts


tftperror:
  kernal_print FAILMESSAGE
:
  lda #$07
  sta $d020
  lda #$02
  sta $d020
  jmp :-

tftpprogress:
  stax saveax     ; Save pointer to block of data 
  lda #'.'        ; Call kernal routine
  jsr $FFD2
  ldax saveax     ; Retrieve pointer
  jmp copy_tftp_block_to_ram    ; Call into default handler (ends with rts)    

saveax: 
  .byte 0,0  

; -------------------------------------------------------------------------
; Network Constants and Data  
  
TFTP_SERVER_IP:
  .byte 45,114,227,35    ; Rogue VPS  

   
NETWORKMESSAGE:
  .byte 147, CG_LCS, CG_DCS, CG_GR3
  .byte "rOGUE nETWORK bOOTLOADER 1.3",13
  .byte "fORWARD udp pORT 3000 TO YOUR c64",13,13
  .byte CG_LBL
  .byte 0

DOWNLOADMESSAGE:
  .byte CG_GR3, "dOWNLOADING GAME DATA", CG_GRN
  .byte 0

OKMESSAGE:
  .byte CG_LGN, "ok",13
  .byte 0                      

FAILMESSAGE:
  .byte CG_RED, "...failed",13
  .byte 0                    
  
tftpname:
  .byte "roguedata", 0

; EOF!