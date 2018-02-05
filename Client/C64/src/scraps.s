
old .cfg entries

    RAM4000: start = $4000, size = $0800, type=rw, file = %O, fill = yes;
    RAM4C00: start = $4C00, size = $2C00, type=rw, file = %O, fill = yes;
    RAM6000: start = $6000, size = $1800, type=rw;
    DISCARD: start = $77FF, size = $10; 



        BSS4K: load = RAM4000, type = bss, optional=yes;
    DATA6K:   load = RAM, run = RAM6000, type = rw, define = yes, optional=yes;
     EXEHDR: load = DISCARD, type = ro, optional=yes;









; Copy screen data from UDP buffer to screen

copyscreen:
  BORDER #$07

  ; Screen Data
  
  ; Copy to the screen that is not currently being shown
;  lda whichscreen
;  beq screen1copy      ; if 0, copy to screen 1
  
;screen0copy:
  ldax #$8000
;  jmp docopy
  
;screen1copy:
;  ldax #$8400
  
  ; Do the copy 
;docopy:
  stax copy_dest  
  ldax #udp_inp_data+4
  stax copy_src 
  ldax #100 ; #1000   ; Decimal
  
  
  