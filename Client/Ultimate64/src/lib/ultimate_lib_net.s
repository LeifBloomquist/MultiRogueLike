;
; File generated by cc65 v 2.18 - Git bf4b195
;
	.fopt		compiler,"cc65 v 2.18 - Git bf4b195"
	.setcpu		"6502"
	.smart		on
	.autoimport	on
	.case		on
	.debuginfo	off
	.importzp	sp, sreg, regsave, regbank
	.importzp	tmp1, tmp2, tmp3, tmp4, ptr1, ptr2, ptr3, ptr4
	.macpack	longbranch
	.import		_strlen
	.import		_memset
	.import		_malloc
	.import		_free
	.export		_uii_status
	.export		_uii_data
	.export		_uii_settarget
	.export		_uii_identify
	.export		_uii_getinterfacecount
	.export		_uii_getipaddress
	.export		_uii_tcpconnect
	.export		_uii_tcpclose
	.export		_uii_tcpsocketread
	.export		_uii_tcpsocketwrite
	.export		_uii_tcpsocketwritechar
	.export		_uii_tcpsocketwrite_ascii
	.export		_uii_tcplistenstart
	.export		_uii_tcplistenstop
	.export		_uii_tcpgetlistenstate
	.export		_uii_tcpgetlistensocket
	.export		_uii_logtext
	.export		_uii_logstatusreg
	.export		_uii_sendcommand
	.export		_uii_readdata
	.export		_uii_readstatus
	.export		_uii_accept
	.export		_uii_abort
	.export		_uii_isdataavailable
	.export		_uii_isstatusdataavailable
	.export		_uii_tcp_nextchar
	.export		_uii_tcp_nextline
	.export		_uii_tcp_nextline_ascii
	.export		_uii_tcp_emptybuffer
	.export		_uii_reset_uiidata
	.export		_uii_tcpsocketread_opt
	.export		_uii_tcpsocketread_opt_init
	.export		_temp_string_onechar
	.export		_uii_data_index
	.export		_uii_data_len
	.export		_uii_target
	.export		_read_cmd
	.export		_uii_echo
	.export		_uii_tcpsocketwrite_convert_parameter
	.export		_uii_tcp_nextline_convert_parameter

.segment	"DATA"

_cmddatareg:
	.word	$DF1D
_controlreg:
	.word	$DF1C
_statusreg:
	.word	$DF1C
_respdatareg:
	.word	$DF1E
_statusdatareg:
	.word	$DF1F
_uii_target:
	.byte	$03
_read_cmd:
	.byte	$00
	.byte	$10
	.byte	$00
	.byte	$F4
	.byte	$01

.segment	"RODATA"

S0001:
	.byte	$0D,$53,$45,$4E,$44,$49,$4E,$47,$20,$41,$42,$4F,$52,$54,$00

.segment	"BSS"

_uii_status:
	.res	256,$00
_uii_data:
	.res	1792,$00
_temp_string_onechar:
	.res	2,$00
_uii_data_index:
	.res	2,$00
_uii_data_len:
	.res	2,$00

; ---------------------------------------------------------------
; void __near__ uii_settarget (unsigned char id)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_settarget: near

.segment	"CODE"

	jsr     pusha
	ldy     #$00
	lda     (sp),y
	sta     _uii_target
	jmp     incsp1

.endproc

; ---------------------------------------------------------------
; void __near__ uii_identify (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_identify: near

.segment	"CODE"

	jsr     decsp2
	ldy     #$01
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$01
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$02
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	jmp     incsp2

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$01

.endproc

; ---------------------------------------------------------------
; void __near__ uii_getinterfacecount (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_getinterfacecount: near

.segment	"CODE"

	lda     _uii_target
	jsr     pusha
	jsr     decsp2
	ldy     #$01
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$02
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$02
	lda     (sp),y
	sta     _uii_target
	jmp     incsp3

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$02

.endproc

; ---------------------------------------------------------------
; void __near__ uii_getipaddress (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_getipaddress: near

.segment	"CODE"

	lda     _uii_target
	jsr     pusha
	jsr     decsp3
	ldy     #$02
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$03
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$03
	lda     (sp),y
	sta     _uii_target
	jmp     incsp4

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$05
	.byte	$00

.endproc

; ---------------------------------------------------------------
; unsigned char __near__ uii_tcpconnect (char *host, unsigned short port)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpconnect: near

.segment	"CODE"

	jsr     pushax
	lda     _uii_target
	jsr     pusha
	jsr     decsp4
	ldy     #$03
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	jsr     push0
	jsr     decsp2
	ldy     #$0C
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	clc
	adc     #$04
	bcc     L0010
	inx
	clc
L0010:	adc     #$01
	bcc     L0004
	inx
L0004:	jsr     _malloc
	jsr     stax0sp
	sta     ptr1
	stx     ptr1+1
	ldy     #$04
	lda     (sp),y
	ldy     #$00
	sta     (ptr1),y
	iny
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	ldy     #$05
	lda     (sp),y
	ldy     #$01
	sta     (ptr1),y
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	ldy     #$09
	lda     (sp),y
	ldy     #$02
	sta     (ptr1),y
	dey
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	ldy     #$0A
	lda     (sp),y
	ldy     #$03
	sta     (ptr1),y
	dey
	lda     #$00
	sta     (sp),y
	iny
	sta     (sp),y
L0007:	ldy     #$05
	jsr     pushwysp
	ldy     #$0E
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	jsr     tosicmp
	bcs     L0008
	ldy     #$03
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	clc
	adc     #$04
	bcc     L0011
	inx
	clc
L0011:	ldy     #$00
	adc     (sp),y
	sta     sreg
	txa
	iny
	adc     (sp),y
	sta     sreg+1
	ldy     #$0B
	lda     (sp),y
	clc
	ldy     #$02
	adc     (sp),y
	sta     ptr1
	ldy     #$0C
	lda     (sp),y
	ldy     #$03
	adc     (sp),y
	sta     ptr1+1
	ldy     #$00
	lda     (ptr1),y
	sta     (sreg),y
	ldy     #$03
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	clc
	adc     #$01
	bcc     L000B
	inx
L000B:	jsr     staxysp
	jmp     L0007
L0008:	jsr     pushw0sp
	ldy     #$0E
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	clc
	adc     #$04
	bcc     L000D
	inx
L000D:	jsr     tosaddax
	sta     ptr1
	stx     ptr1+1
	lda     #$00
	tay
	sta     (ptr1),y
	lda     #$03
	jsr     _uii_settarget
	jsr     pushw0sp
	ldy     #$0E
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	clc
	adc     #$04
	bcc     L0012
	inx
	clc
L0012:	adc     #$01
	bcc     L000F
	inx
L000F:	jsr     _uii_sendcommand
	ldy     #$01
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _free
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$08
	lda     (sp),y
	sta     _uii_target
	ldx     #$00
	txa
	sta     _uii_data_index
	sta     _uii_data_index+1
	sta     _uii_data_len
	sta     _uii_data_len+1
	lda     _uii_data
	ldy     #$0D
	jmp     addysp

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$07
	.byte	$00
	.byte	$00

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcpclose (unsigned char socketid)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpclose: near

.segment	"CODE"

	jsr     pusha
	lda     _uii_target
	jsr     pusha
	jsr     decsp3
	ldy     #$02
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	ldy     #$04
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$03
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$03
	lda     (sp),y
	sta     _uii_target
	jmp     incsp5

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$09
	.byte	$00

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcpsocketread (unsigned char socketid, unsigned short length)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketread: near

.segment	"CODE"

	jsr     pushax
	lda     _uii_target
	jsr     pusha
	jsr     decsp5
	ldy     #$04
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	ldy     #$00
	lda     (sp),y
	sta     (sp),y
	iny
	lda     (sp),y
	sta     (sp),y
	ldy     #$08
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	ldy     #$06
	lda     (sp),y
	ldy     #$03
	sta     (sp),y
	ldy     #$07
	lda     (sp),y
	ldy     #$04
	sta     (sp),y
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$05
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$05
	lda     (sp),y
	sta     _uii_target
	ldx     _uii_data+1
	lda     _uii_data
	ldy     #$09
	jmp     addysp

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$10
	.byte	$00
	.byte	$00
	.byte	$00

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcpsocketwrite (unsigned char socketid, char *data)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketwrite: near

.segment	"CODE"

	jsr     pushax
	jsr     decsp3
	ldy     #$05
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	ldy     #$04
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	ldy     #$00
	sta     (sp),y
	iny
	txa
	sta     (sp),y
	ldx     #$00
	txa
	jsr     _uii_tcpsocketwrite_convert_parameter
	jmp     incsp3

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcpsocketwritechar (unsigned char socketid, char one_char)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketwritechar: near

.segment	"CODE"

	jsr     pusha
	ldy     #$00
	lda     (sp),y
	sta     _temp_string_onechar
	sty     _temp_string_onechar+1
	iny
	lda     (sp),y
	jsr     pusha
	lda     #<(_temp_string_onechar)
	ldx     #>(_temp_string_onechar)
	jsr     _uii_tcpsocketwrite
	jmp     incsp2

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcpsocketwrite_ascii (unsigned char socketid, char *data)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketwrite_ascii: near

.segment	"CODE"

	jsr     pushax
	jsr     decsp3
	ldy     #$05
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	ldy     #$04
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	ldy     #$00
	sta     (sp),y
	iny
	txa
	sta     (sp),y
	ldx     #$00
	tya
	jsr     _uii_tcpsocketwrite_convert_parameter
	jmp     incsp3

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcplistenstart (unsigned short port)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcplistenstart: near

.segment	"CODE"

	jsr     pushax
	lda     _uii_target
	jsr     pusha
	jsr     decsp4
	ldy     #$03
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	ldy     #$05
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	ldy     #$06
	lda     (sp),y
	ldy     #$03
	sta     (sp),y
	tya
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$04
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$04
	lda     (sp),y
	sta     _uii_target
	ldx     _uii_data+1
	lda     _uii_data
	jmp     incsp7

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$12
	.byte	$00
	.byte	$00

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcplistenstop (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcplistenstop: near

.segment	"CODE"

	lda     _uii_target
	jsr     pusha
	jsr     decsp2
	ldy     #$01
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$02
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$02
	lda     (sp),y
	sta     _uii_target
	ldx     _uii_data+1
	lda     _uii_data
	jmp     incsp3

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$13

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcpgetlistenstate (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpgetlistenstate: near

.segment	"CODE"

	lda     _uii_target
	jsr     pusha
	jsr     decsp2
	ldy     #$01
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$02
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$02
	lda     (sp),y
	sta     _uii_target
	ldx     _uii_data+1
	lda     _uii_data
	jmp     incsp3

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$14

.endproc

; ---------------------------------------------------------------
; unsigned char __near__ uii_tcpgetlistensocket (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpgetlistensocket: near

.segment	"CODE"

	lda     _uii_target
	jsr     pusha
	jsr     decsp2
	ldy     #$01
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$03
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$02
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$02
	lda     (sp),y
	sta     _uii_target
	lda     _uii_data
	ldx     #$00
	jmp     incsp3

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$15

.endproc

; ---------------------------------------------------------------
; void __near__ uii_logtext (char *text)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_logtext: near

.segment	"CODE"

	jsr     pushax
	ldy     #$00
	tya
	sta     (sp),y
	iny
	sta     (sp),y
	jmp     incsp2

.endproc

; ---------------------------------------------------------------
; void __near__ uii_logstatusreg (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_logstatusreg: near

.segment	"CODE"

	rts

.endproc

; ---------------------------------------------------------------
; void __near__ uii_sendcommand (unsigned char *bytes, int count)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_sendcommand: near

.segment	"CODE"

	jsr     pushax
	jsr     push0
	jsr     push0
	ldy     #$07
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	lda     _uii_target
	ldy     #$00
	sta     (ptr1),y
	jmp     L0024
L0006:	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	and     #$20
	bne     L001F
	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$10
	beq     L0020
L001F:	tya
	jmp     L000D
L0020:	lda     #$01
L000D:	jsr     bnega
	bne     L0006
	jmp     L0010
L000E:	lda     _cmddatareg
	ldx     _cmddatareg+1
	jsr     pushax
	ldy     #$0B
	jsr     pushwysp
	ldy     #$07
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	sta     regsave
	stx     regsave+1
	clc
	adc     #$01
	bcc     L0011
	inx
L0011:	jsr     staxysp
	lda     regsave
	ldx     regsave+1
	jsr     tosaddax
	sta     ptr1
	stx     ptr1+1
	ldy     #$00
	lda     (ptr1),y
	jsr     staspidx
L0010:	ldy     #$03
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	ldy     #$04
	cmp     (sp),y
	txa
	iny
	sbc     (sp),y
	bvc     L001D
	eor     #$80
L001D:	bmi     L000E
	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	ora     #$01
	sta     (ptr1),y
	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$04
	cmp     #$04
	bne     L0015
	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	lda     (ptr1),y
	ora     #$08
	sta     (ptr1),y
	jmp     L0024
L0015:	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$20
	bne     L0023
	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$10
	cmp     #$10
	beq     L0015
L0023:	lda     #$01
	sta     (sp),y
	lda     #$00
	ldy     #$01
	sta     (sp),y
	dey
L0024:	lda     (sp),y
	iny
	ora     (sp),y
	jeq     L0006
	jmp     incsp8

.endproc

; ---------------------------------------------------------------
; unsigned int __near__ uii_readdata (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_readdata: near

.segment	"CODE"

	jsr     push0
	sta     _uii_data
	jmp     L0004
L0002:	ldy     #$01
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	sta     regsave
	stx     regsave+1
	clc
	adc     #$01
	bcc     L0006
	inx
L0006:	jsr     stax0sp
	lda     regsave
	clc
	adc     #<(_uii_data)
	sta     sreg
	lda     regsave+1
	adc     #>(_uii_data)
	sta     sreg+1
	lda     _respdatareg+1
	sta     ptr1+1
	lda     _respdatareg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	sta     (sreg),y
L0004:	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	and     #$80
	cmp     #$80
	beq     L0002
	lda     #<(_uii_data)
	clc
	adc     (sp),y
	sta     ptr1
	lda     #>(_uii_data)
	iny
	adc     (sp),y
	sta     ptr1+1
	lda     #$00
	dey
	sta     (ptr1),y
	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jmp     incsp2

.endproc

; ---------------------------------------------------------------
; int __near__ uii_readstatus (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_readstatus: near

.segment	"CODE"

	jsr     push0
	sta     _uii_status
	jmp     L0004
L0002:	ldy     #$01
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	sta     regsave
	stx     regsave+1
	clc
	adc     #$01
	bcc     L0005
	inx
L0005:	jsr     stax0sp
	lda     regsave
	clc
	adc     #<(_uii_status)
	sta     sreg
	lda     regsave+1
	adc     #>(_uii_status)
	sta     sreg+1
	lda     _statusdatareg+1
	sta     ptr1+1
	lda     _statusdatareg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	sta     (sreg),y
L0004:	jsr     _uii_isstatusdataavailable
	stx     tmp1
	ora     tmp1
	bne     L0002
	lda     #<(_uii_status)
	ldy     #$00
	clc
	adc     (sp),y
	sta     ptr1
	lda     #>(_uii_status)
	iny
	adc     (sp),y
	sta     ptr1+1
	lda     #$00
	dey
	sta     (ptr1),y
	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jmp     incsp2

.endproc

; ---------------------------------------------------------------
; void __near__ uii_accept (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_accept: near

.segment	"CODE"

	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	ora     #$02
	sta     (ptr1),y
	rts

.endproc

; ---------------------------------------------------------------
; void __near__ uii_abort (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_abort: near

.segment	"CODE"

	jsr     _uii_logstatusreg
	lda     #<(S0001)
	ldx     #>(S0001)
	jsr     _uii_logtext
	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	ora     #$04
	sta     (ptr1),y
	rts

.endproc

; ---------------------------------------------------------------
; int __near__ uii_isdataavailable (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_isdataavailable: near

.segment	"CODE"

	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	ldx     #$00
	and     #$80
	cmp     #$80
	bne     L0005
	lda     #$01
	rts
L0005:	txa
	rts

.endproc

; ---------------------------------------------------------------
; int __near__ uii_isstatusdataavailable (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_isstatusdataavailable: near

.segment	"CODE"

	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	ldx     #$00
	and     #$40
	cmp     #$40
	bne     L0005
	lda     #$01
	rts
L0005:	txa
	rts

.endproc

; ---------------------------------------------------------------
; char __near__ uii_tcp_nextchar (unsigned char socketid)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcp_nextchar: near

.segment	"CODE"

	jsr     pusha
	jsr     decsp1
	lda     _uii_data_index
	cmp     _uii_data_len
	lda     _uii_data_index+1
	sbc     _uii_data_len+1
	bvc     L000C
	eor     #$80
L000C:	bpl     L0002
	lda     _uii_data_index
	ldx     _uii_data_index+1
	clc
	adc     #$02
	bcc     L0003
	inx
L0003:	sta     ptr1
	txa
	clc
	adc     #>(_uii_data)
	sta     ptr1+1
	ldy     #<(_uii_data)
	lda     (ptr1),y
	ldy     #$00
	sta     (sp),y
	lda     _uii_data_index
	ldx     _uii_data_index+1
	clc
	adc     #$01
	bcc     L0010
	inx
	jmp     L0010
L0002:	ldy     #$01
	lda     (sp),y
	jsr     pusha
	ldx     #$03
	lda     #$7C
	jsr     _uii_tcpsocketread
	sta     _uii_data_len
	stx     _uii_data_len+1
	cpx     #$00
	bne     L0009
	cmp     #$00
	beq     L0001
L0009:	lda     _uii_data_len+1
	cmp     #$FF
	bne     L000E
	lda     _uii_data_len
	cmp     #$FF
	beq     L0002
L000E:	lda     _uii_data+2
	ldy     #$00
	sta     (sp),y
	ldx     #$00
	lda     #$01
L0010:	sta     _uii_data_index
	stx     _uii_data_index+1
	ldx     #$00
	lda     (sp),y
L0001:	jmp     incsp2

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcp_nextline (unsigned char socketid, char *result)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcp_nextline: near

.segment	"CODE"

	jsr     pushax
	jsr     decsp3
	ldy     #$05
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	ldy     #$04
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	ldy     #$00
	sta     (sp),y
	iny
	txa
	sta     (sp),y
	ldx     #$00
	txa
	jsr     _uii_tcp_nextline_convert_parameter
	jmp     incsp3

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcp_nextline_ascii (unsigned char socketid, char *result)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcp_nextline_ascii: near

.segment	"CODE"

	jsr     pushax
	jsr     decsp3
	ldy     #$05
	lda     (sp),y
	ldy     #$02
	sta     (sp),y
	ldy     #$04
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	ldy     #$00
	sta     (sp),y
	iny
	txa
	sta     (sp),y
	ldx     #$00
	tya
	jsr     _uii_tcp_nextline_convert_parameter
	jmp     incsp3

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcp_emptybuffer (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcp_emptybuffer: near

.segment	"CODE"

	lda     #$00
	sta     _uii_data_index
	sta     _uii_data_index+1
	rts

.endproc

; ---------------------------------------------------------------
; void __near__ uii_reset_uiidata (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_reset_uiidata: near

.segment	"CODE"

	lda     #$00
	sta     _uii_data_len
	sta     _uii_data_len+1
	sta     _uii_data_index
	sta     _uii_data_index+1
	lda     #<(_uii_data)
	ldx     #>(_uii_data)
	jsr     pushax
	ldx     #$07
	lda     #$00
	jsr     __bzero
	lda     #<(_uii_status)
	ldx     #>(_uii_status)
	jsr     pushax
	ldx     #$01
	lda     #$00
	jmp     __bzero

.endproc

; ---------------------------------------------------------------
; unsigned int __near__ uii_tcpsocketread_opt (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketread_opt: near

.segment	"CODE"

	jsr     push0
L0005:	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	and     #$20
	bne     L001C
	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$10
	beq     L001D
L001C:	tya
	jmp     L000C
L001D:	lda     #$01
L000C:	jsr     bnega
	bne     L0005
	lda     _cmddatareg+1
	sta     ptr1+1
	lda     _cmddatareg
	sta     ptr1
	lda     _read_cmd
	sta     (ptr1),y
	lda     _cmddatareg+1
	sta     ptr1+1
	lda     _cmddatareg
	sta     ptr1
	lda     _read_cmd+1
	sta     (ptr1),y
	lda     _cmddatareg+1
	sta     ptr1+1
	lda     _cmddatareg
	sta     ptr1
	lda     _read_cmd+2
	sta     (ptr1),y
	lda     _cmddatareg+1
	sta     ptr1+1
	lda     _cmddatareg
	sta     ptr1
	lda     _read_cmd+3
	sta     (ptr1),y
	lda     _cmddatareg+1
	sta     ptr1+1
	lda     _cmddatareg
	sta     ptr1
	lda     _read_cmd+4
	sta     (ptr1),y
	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	lda     (ptr1),y
	ora     #$01
	sta     (ptr1),y
	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$04
	beq     L000F
	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	lda     (ptr1),y
	ora     #$08
	sta     (ptr1),y
	jmp     L0005
L000F:	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$20
	bne     L0018
	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$10
	bne     L000F
	jmp     L0018
L0016:	ldy     #$01
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	sta     regsave
	stx     regsave+1
	clc
	adc     #$01
	bcc     L0019
	inx
L0019:	jsr     stax0sp
	lda     regsave
	clc
	adc     #<(_uii_data)
	sta     sreg
	lda     regsave+1
	adc     #>(_uii_data)
	sta     sreg+1
	lda     _respdatareg+1
	sta     ptr1+1
	lda     _respdatareg
	sta     ptr1
	ldy     #$00
	lda     (ptr1),y
	sta     (sreg),y
L0018:	lda     _statusreg+1
	sta     ptr1+1
	lda     _statusreg
	sta     ptr1
	lda     (ptr1),y
	and     #$80
	bne     L0016
	lda     _controlreg+1
	sta     ptr1+1
	lda     _controlreg
	sta     ptr1
	lda     (ptr1),y
	ora     #$02
	sta     (ptr1),y
	ldy     #$01
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	sec
	sbc     #$02
	bcs     L0001
	dex
L0001:	jmp     incsp2

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcpsocketread_opt_init (unsigned char socketid)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketread_opt_init: near

.segment	"CODE"

	jsr     pusha
	lda     #$03
	sta     _read_cmd
	ldy     #$00
	lda     (sp),y
	sta     _read_cmd+2
	lda     #$03
	jsr     _uii_settarget
	jmp     incsp1

.endproc

; ---------------------------------------------------------------
; void __near__ uii_echo (void)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_echo: near

.segment	"CODE"

	jsr     decsp2
	ldy     #$01
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	lda     #$01
	jsr     _uii_settarget
	lda     sp
	ldx     sp+1
	jsr     pushax
	ldx     #$00
	lda     #$02
	jsr     _uii_sendcommand
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	jmp     incsp2

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$F0

.endproc

; ---------------------------------------------------------------
; void __near__ uii_tcpsocketwrite_convert_parameter (unsigned char socketid, char *data, int ascii)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcpsocketwrite_convert_parameter: near

.segment	"CODE"

	jsr     pushax
	lda     _uii_target
	jsr     pusha
	jsr     decsp3
	ldy     #$02
L0002:	lda     M0001,y
	sta     (sp),y
	dey
	bpl     L0002
	jsr     push0
	jsr     decsp3
	ldy     #$0C
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	clc
	adc     #$03
	bcc     L0003
	inx
L0003:	jsr     _malloc
	ldy     #$01
	jsr     staxysp
	sta     ptr1
	stx     ptr1+1
	ldy     #$05
	lda     (sp),y
	ldy     #$00
	sta     (ptr1),y
	ldy     #$02
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	ldy     #$06
	lda     (sp),y
	ldy     #$01
	sta     (ptr1),y
	iny
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	ldy     #$0D
	lda     (sp),y
	ldy     #$02
	sta     (ptr1),y
	iny
	lda     #$00
	sta     (sp),y
	iny
	sta     (sp),y
L0006:	ldy     #$06
	jsr     pushwysp
	ldy     #$0E
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	jsr     tosicmp
	jcs     L0007
	ldy     #$0B
	lda     (sp),y
	ldy     #$03
	clc
	adc     (sp),y
	sta     ptr1
	ldy     #$0C
	lda     (sp),y
	ldy     #$04
	adc     (sp),y
	sta     ptr1+1
	ldy     #$00
	lda     (ptr1),y
	sta     (sp),y
	ldy     #$0A
	lda     (sp),y
	dey
	ora     (sp),y
	beq     L001B
	ldy     #$00
	lda     (sp),y
	cmp     #$61
	bcc     L0029
	cmp     #$7B
	bcc     L0032
L0029:	lda     (sp),y
	cmp     #$C1
	bcc     L0034
	cmp     #$DB
	bcs     L0034
L0032:	lda     (sp),y
	and     #$5F
	jmp     L0022
L0034:	lda     (sp),y
	cmp     #$41
	bcc     L0039
	cmp     #$5B
	bcs     L0039
	ora     #$20
	jmp     L0022
L0039:	lda     (sp),y
	cmp     #$0D
	bne     L001B
	lda     #$0A
L0022:	sta     (sp),y
L001B:	ldy     #$04
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	clc
	adc     #$03
	bcc     L0023
	inx
	clc
L0023:	ldy     #$01
	adc     (sp),y
	sta     ptr1
	txa
	iny
	adc     (sp),y
	sta     ptr1+1
	ldy     #$00
	lda     (sp),y
	sta     (ptr1),y
	ldy     #$04
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	clc
	adc     #$01
	bcc     L000A
	inx
L000A:	jsr     staxysp
	jmp     L0006
L0007:	ldy     #$04
	jsr     pushwysp
	ldy     #$0E
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	clc
	adc     #$03
	bcc     L0024
	inx
	clc
L0024:	adc     #$01
	bcc     L001E
	inx
L001E:	jsr     tosaddax
	sta     ptr1
	stx     ptr1+1
	lda     #$00
	tay
	sta     (ptr1),y
	lda     #$03
	jsr     _uii_settarget
	ldy     #$04
	jsr     pushwysp
	ldy     #$0E
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _strlen
	clc
	adc     #$03
	bcc     L001F
	inx
L001F:	jsr     _uii_sendcommand
	ldy     #$02
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	jsr     _free
	jsr     _uii_readdata
	jsr     _uii_readstatus
	jsr     _uii_accept
	ldy     #$08
	lda     (sp),y
	sta     _uii_target
	lda     #$00
	sta     _uii_data_index
	sta     _uii_data_index+1
	sta     _uii_data_len
	sta     _uii_data_len+1
	ldy     #$0E
	jmp     addysp

.segment	"RODATA"

M0001:
	.byte	$00
	.byte	$11
	.byte	$00

.endproc

; ---------------------------------------------------------------
; int __near__ uii_tcp_nextline_convert_parameter (unsigned char socketid, char *result, int swapCase)
; ---------------------------------------------------------------

.segment	"CODE"

.proc	_uii_tcp_nextline_convert_parameter: near

.segment	"CODE"

	jsr     pushax
	jsr     decsp2
	jsr     push0
	ldy     #$07
	lda     (sp),y
	sta     ptr1+1
	dey
	lda     (sp),y
	sta     ptr1
	lda     #$00
	jmp     L003C
L0002:	iny
L003D:	lda     (sp),y
	bne     L003A
	dey
	lda     (sp),y
	cmp     #$0D
	jeq     L0004
L003A:	ldy     #$05
	lda     (sp),y
	dey
	ora     (sp),y
	jeq     L001C
	dey
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$61
	txa
	sbc     #$00
	bvs     L000F
	eor     #$80
L000F:	bpl     L0027
	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$7B
	txa
	sbc     #$00
	bvc     L0011
	eor     #$80
L0011:	bmi     L0035
L0027:	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$C1
	txa
	sbc     #$00
	bvs     L0015
	eor     #$80
L0015:	bpl     L000E
	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$DB
	txa
	sbc     #$00
	bvc     L0017
	eor     #$80
L0017:	bpl     L000E
L0035:	lda     (sp),y
	ldx     #$00
	and     #$5F
	jmp     L002B
L000E:	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$41
	txa
	sbc     #$00
	bvs     L001D
	eor     #$80
L001D:	bpl     L001C
	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$5B
	txa
	sbc     #$00
	bvc     L001F
	eor     #$80
L001F:	bpl     L001C
	iny
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	ora     #$20
L002B:	jsr     staxysp
L001C:	ldy     #$09
	jsr     pushwysp
	ldy     #$03
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	sta     regsave
	stx     regsave+1
	clc
	adc     #$01
	bcc     L0022
	inx
L0022:	jsr     staxysp
	lda     regsave
	ldx     regsave+1
	jsr     tosaddax
	sta     ptr1
	stx     ptr1+1
	ldy     #$02
	lda     (sp),y
L003C:	ldy     #$00
	sta     (ptr1),y
L0004:	ldy     #$08
	lda     (sp),y
	jsr     _uii_tcp_nextchar
	ldy     #$02
	jsr     staxysp
	cpx     #$00
	bne     L003B
	cmp     #$00
	beq     L0029
L003B:	ldy     #$03
	lda     (sp),y
	jne     L003D
	dey
	lda     (sp),y
	cmp     #$0A
	jne     L0002
L0029:	ldy     #$06
	lda     (sp),y
	ldy     #$00
	clc
	adc     (sp),y
	sta     ptr1
	ldy     #$07
	lda     (sp),y
	ldy     #$01
	adc     (sp),y
	sta     ptr1+1
	ldx     #$00
	txa
	dey
	sta     (ptr1),y
	ldy     #$02
	lda     (sp),y
	iny
	ora     (sp),y
	bne     L0039
	ldy     #$01
	lda     (sp),y
	tax
	dey
	lda     (sp),y
	cmp     #$01
	txa
	sbc     #$00
	bvs     L0025
	eor     #$80
L0025:	asl     a
	ldx     #$00
	bcs     L0039
	txa
	jmp     L0001
L0039:	lda     #$01
L0001:	ldy     #$09
	jmp     addysp

.endproc

