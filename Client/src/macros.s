
  .import kippercall  

.macro kernal_print address
  lda #<address
  ldy #>address
  jsr $ab1e   
.endmacro
 

.macro kippercall function_number
  ldy function_number
  jsr KPR_DISPATCH_VECTOR   
.endmacro

.macro BORDER color
  ;lda #color
  ;sta $D020   
.endmacro