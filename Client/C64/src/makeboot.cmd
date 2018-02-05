@cls
@del *.o
@del *.prg

@cc65\ca65 rogueboot.s
@cc65\ld65 rogueboot.o -C cfg/c64prg-vortex-boot.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -o rogueboot.prg

@dir rogueboot.prg
@copy rogueboot.prg p:\rogue\