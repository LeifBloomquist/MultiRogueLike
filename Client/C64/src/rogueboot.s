;------------------------------------------------------------------------
; Vortex II Bootloader Source Code

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
  
	.segment "STARTUP"    ;this is what gets put at the start of the file on the C64

; -------------------------------------------------------------------------
; Load address and BASIC stub

	.word basicstub		; load address

basicstub:
	.word @nextline
	.word 2014
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
  lda #12     ; decimal 1.2             
  sta $cFFF

  jmp $8000


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
  
  ldax #$3000
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
  .byte 208,79,218,201    ; Vortex VPS  

;c64 c/g 
CG_BLK = 144
CG_WHT = 5
CG_RED = 28
CG_CYN = 159
CG_PUR = 156
CG_GRN = 30
CG_BLU = 31
CG_YEL = 158
CG_BRN = 149
CG_ORG = 129
CG_PNK = 150
CG_GR1 = 151
CG_GR2 = 152
CG_LGN = 153
CG_LBL = 154
CG_GR3 = 155
CG_RVS = 18 ;revs-on
CG_NRM = 146 ;revs-off

CG_DCS = 8  ;disable shift+C=
CG_ECS = 9  ;enable shift+C=

CG_LCS = 14 ;switch to lowercase
CG_UCS = 142 ;switch to uppercase
 
   
NETWORKMESSAGE:
  .byte 147, CG_LCS, CG_DCS, CG_GR3
  .byte "rOGUE nETWORK bOOTLOADER 1.0",13
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