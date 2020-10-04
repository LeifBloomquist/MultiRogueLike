; -------------------------------------------------------------------------
; Rogue Login Code
; -------------------------------------------------------------------------

  .import cfg_mac_default
  .export _login

STATIONLENGTH=1

station:  
  kernal_print TITLEMESSAGE  
  kernal_print TITLEMESSAGE2 
  kernal_print MOTD  
  kernal_print STATIONPROMPT
  
  ; Get station
  ldax #NUMBERS
  ldy #STATIONLENGTH
  jsr FILTERED_INPUT  
  ;Station is now in GOTINPUT  
  
  lda GOTINPUT
  sta cfg_mac_default+5    ; Last byte of MAC address
  
  rts    

; -------------------------------------------------------------------------

NAMELENGTH=15

_login:
login:
  kernal_print LOGINPROMPT
  
  ; Get name
  ldax #ALPHANUMERIC
  ldy #NAMELENGTH
  jsr FILTERED_INPUT  
  ;Name is now in GOTINPUT  
  
  rts  


; -------------------------------------------------------------------------


TITLEMESSAGE:
  .byte 147, CG_LCS, CG_DCS, CG_RED
  .byte "rOGUE vERSION 0.007", 13, 13
  .byte CG_LBL, "cONCEPT+gAME cODE: ", CG_WHT, "lEIF bLOOMQUIST", 13, 13
  .byte CG_LBL, "nETWORKING cODE:   ", CG_WHT, "jONNO dOWNES", 13
  .byte CG_LBL, "                   ", CG_WHT, "pER oLOFSSON", 13, 13
  .byte CG_LBL, "cONTRIBUTORS:      ", CG_WHT, "rOBIN hARBRON", 13
  .byte CG_LBL, "                   ", CG_WHT, "QZEROW", 13, 13
  .byte 0   
   
TITLEMESSAGE2:
  .byte CG_LBL, "cONTROLS:          ", CG_YEL, "jOYSTICK IN pORT 2", 13
  .byte CG_LBL, "                   ", CG_YEL, "pRESS f1 FOR kEYS", 13, 13
  .byte 0

MOTD: 
  .byte CG_GRN, "lan vERSION FOR ",CG_LGN, "rOGUELIKE cELEBRATION!", 13
  .byte 13
  .byte 0
  
STATIONPROMPT: 
  .byte CG_RED, "lan sTATION#: ", CG_YEL
  .byte 0
  
LOGINPROMPT:  
  .byte " ", 13, 13, CG_LGN, "lOGIN: ", CG_WHT
  .byte 0

; EOF!