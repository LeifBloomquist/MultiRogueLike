; -------------------------------------------------------------------------
; Rogue Login Code

NAMELENGTH=15

login:  
  kernal_print TITLEMESSAGE  
  kernal_print MOTD
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
  .byte "rOGUE vERSION 0.003", 13, 13
  .byte CG_LBL, "cONCEPT+gAME cODE: ", CG_WHT, "lEIF bLOOMQUIST", 13, 13
  .byte CG_LBL, "nETWORKING cODE:   ", CG_WHT, "jONNO dOWNES", 13
  .byte CG_LBL, "                   ", CG_WHT, "pER oLOFSSON", 13, 13
  .byte CG_LBL, "cONTRIBUTORS:      ", CG_WHT, "rOBIN hARBRON", 13
  .byte CG_LBL, "                   ", CG_WHT, "Q0W/aTLANTIS", 13, 13
;  .byte CG_LBL, "pLAYTESTERS:       ", CG_WHT, "", 13, 13
  .byte 13
  .byte 0   

MOTD: 
  .byte CG_GRN, "mESSAGE OF THE dAY:", 13, 13     
  .byte CG_LGN, "  tHANKS FOR TRYING THE DEMO!", 13
  .byte 13
  .byte 0
  
LOGINPROMPT:  
  .byte CG_RED, "lOGIN: ", CG_PNK
  .byte 0
  
; EOF!