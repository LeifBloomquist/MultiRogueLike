@cls
@del *.o
@del *.prg

..\..\..\Tools\cc65\bin\ca65 rogueboot.s
..\..\..\Tools\cc65\bin\ld65 rogueboot.o -C cfg/c64prg-vortex-boot.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -o rboottemp.prg

exo\exomizer.exe sfx basic rboottemp.prg -o rogueboot.prg

@dir rogueboot.prg
@rem @copy rogueboot.prg p:\rogue\    