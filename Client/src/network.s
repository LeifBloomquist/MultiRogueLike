; -------------------------------------------------------------------------
; Roguelike Network Code

SEND_LENGTH   = 0017

; Packet Types - C64 to CLIENT
PACKET_ANNOUNCE       = 1
PACKET_CLIENT_UPDATE  = 2

; Packet Types - Server to C64
PACKET_ANNOUNCE_REPLY = 128
PACKET_SERVER_UPDATE  = 129


; -------------------------------------------------------------------------
; IP65 Imports
  
  .import udp_init
  .import udp_send
    .import udp_send_dest
    .import udp_send_dest_port
    .import udp_send_src_port
    .import udp_send_len
  
  .import udp_add_listener 
    .import udp_callback
    
  .import udp_inp
  .importzp udp_data
  
  udp_inp_data = udp_inp + udp_data
  
  .import copymem
    .importzp copy_src
    .importzp copy_dest  
  

; -------------------------------------------------------------------------
; Network Initialization

network_init_dhcp:  
  kernal_print NETWORKMESSAGE
  
  init_ip_via_dhcp   
  
  lda SERVER_IP+0
  sta cfg_tftp_server+0 
  lda SERVER_IP+1
  sta cfg_tftp_server+1
  lda SERVER_IP+2
  sta cfg_tftp_server+2
  lda SERVER_IP+3
  sta cfg_tftp_server+3
  
  jsr print_cr
  jsr print_ip_config
  jsr print_cr  
  rts
    
network_init_udp:
  jsr udp_init
  
  ; UDP Sends 
  lda SERVER_IP+0
  sta udp_send_dest+0 
  lda SERVER_IP+1
  sta udp_send_dest+1
  lda SERVER_IP+2
  sta udp_send_dest+2
  lda SERVER_IP+3
  sta udp_send_dest+3
  
  ldax #SERVER_PORT
  stax udp_send_dest_port
  
  ldax #SRC_PORT
  stax udp_send_src_port
  
  ldax #SEND_LENGTH
  stax udp_send_len
  
  ; UDP Receives
  ldax #gotpacket
  stax udp_callback
     
  ldax #LISTEN_PORT
  jsr udp_add_listener 
  rts
  

; -------------------------------------------------------------------------
; Send the Action packet
; A contains action
; X contains action parameter 1, if applicable
; Y contains action parameter 2, if applicable

sendaction:
  sta SENDBUFFER+2
  stx SENDBUFFER+3
  sty SENDBUFFER+4
  
  ; At the start of the game we send the Announce packet instead.
  lda gamepacketreceived
  beq sendannounce
  
  lda #PACKET_CLIENT_UPDATE
  sta SENDBUFFER+0      
 
  inc ACTIONCOUNTER
  lda ACTIONCOUNTER
  sta SENDBUFFER+1
    
  ldax #SENDBUFFER  
  jsr udp_send
  rts  

sendannounce:
  lda #PACKET_ANNOUNCE
  sta SENDBUFFER+0      
  
  ; Player Type (character)
  lda playerspritecolor
  sta SENDBUFFER+1
  
  ; Player name
  lda GOTINPUT+0
  sta SENDBUFFER+2
  
  lda GOTINPUT+1
  sta SENDBUFFER+3
  
  lda GOTINPUT+2
  sta SENDBUFFER+4
  
  lda GOTINPUT+3
  sta SENDBUFFER+5
  
  lda GOTINPUT+4
  sta SENDBUFFER+6
  
  lda GOTINPUT+5
  sta SENDBUFFER+7
  
  lda GOTINPUT+6
  sta SENDBUFFER+8
  
  lda GOTINPUT+7
  sta SENDBUFFER+9
  
  lda GOTINPUT+8
  sta SENDBUFFER+10
  
  lda GOTINPUT+9
  sta SENDBUFFER+11
  
  lda GOTINPUT+10
  sta SENDBUFFER+12
  
  lda GOTINPUT+11
  sta SENDBUFFER+13
  
  lda GOTINPUT+12
  sta SENDBUFFER+14
  
  lda GOTINPUT+13
  sta SENDBUFFER+15
  
  lda GOTINPUT+14
  sta SENDBUFFER+16
    
  ldax #SENDBUFFER  
  jsr udp_send
  rts  


; -------------------------------------------------------------------------
; Handle Received Packets (Dispatcher)

gotpacket:
  lda udp_inp_data+0   
  cmp #PACKET_SERVER_UPDATE
  beq gameupdate
  rts


;--------------------------------------------------------------------------
; Handle server updates

gameupdate:
  lda #$01
  sta gamepacketreceived  ; Flag that we got the first game packet
 
; -------------------------------------------------------------------------
; Copy screen data from UDP buffer to screen
copyscreen:  
  inc $d021
  ldx #$00

copy:  
  ; 17 Rows on screen
  lda udp_inp_data+1,x 
  sta SCREEN_BASE+1+(40*2),x                 ; Shifted over one column for border  
  
  lda udp_inp_data+1+(GAME_COLS*1),x
  sta SCREEN_BASE+1+(40*3),x

  lda udp_inp_data+1+(GAME_COLS*2),x
  sta SCREEN_BASE+1+(40*4),x
 
  lda udp_inp_data+1+(GAME_COLS*3),x
  sta SCREEN_BASE+1+(40*5),x
 
  lda udp_inp_data+1+(GAME_COLS*4),x
  sta SCREEN_BASE+1+(40*6),x
 
  lda udp_inp_data+1+(GAME_COLS*5),x
  sta SCREEN_BASE+1+(40*7),x
 
  lda udp_inp_data+1+(GAME_COLS*6),x
  sta SCREEN_BASE+1+(40*8),x
 
  lda udp_inp_data+1+(GAME_COLS*7),x
  sta SCREEN_BASE+1+(40*9),x
 
  lda udp_inp_data+1+(GAME_COLS*8),x
  sta SCREEN_BASE+1+(40*10),x
 
  lda udp_inp_data+1+(GAME_COLS*9),x
  sta SCREEN_BASE+1+(40*11),x
 
  lda udp_inp_data+1+(GAME_COLS*10),x
  sta SCREEN_BASE+1+(40*12),x
 
  lda udp_inp_data+1+(GAME_COLS*11),x
  sta SCREEN_BASE+1+(40*13),x
 
  lda udp_inp_data+1+(GAME_COLS*12),x
  sta SCREEN_BASE+1+(40*14),x
 
  lda udp_inp_data+1+(GAME_COLS*13),x
  sta SCREEN_BASE+1+(40*15),x
 
  lda udp_inp_data+1+(GAME_COLS*14),x
  sta SCREEN_BASE+1+(40*16),x
 
  lda udp_inp_data+1+(GAME_COLS*15),x
  sta SCREEN_BASE+1+(40*17),x
 
  lda udp_inp_data+1+(GAME_COLS*16),x
  sta SCREEN_BASE+1+(40*18),x
 
  inx
  cpx GAME_ROWS
  bne copy
  
  rts

; -------------------------------------------------------------------------
; Network Constants and Data  
  
SERVER_IP:
  ; .byte 208,79,218,201    ; Vortex VPS  
  .byte 192,168,7,105       ; Dev Laptop

  
SERVER_PORT = 3006
SRC_PORT    = 3000  ; In theory, having these match makes NAT work on some routers
LISTEN_PORT = 3000

SENDBUFFER:
  .res SEND_LENGTH
  
NETWORKMESSAGE:
  .byte 147, CG_LCS, CG_DCS, CG_LBL
  .byte "rOGUE dEMO nETWORK iNITIALIZATION",13, 13
  .byte CG_YEL, "fORWARD udp pORT 3000 TO YOUR c64", CG_LBL, 13,13
  .byte 0

SERVERMESSAGE:
  .byte "wAITING FOR SERVER..."
  .byte 0       

OKMESSAGE:
  .byte "ok",13
  .byte 0                      

FAILMESSAGE:
  .byte "...failed",13
  .byte 0                    
  
gamepacketreceived:
   .byte 0           

bittab:
   .byte $01,$02,$04,$08,$10,$20,$40,$80
