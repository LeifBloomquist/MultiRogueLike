@cls
@del *.o
@del *.bin

@cc65\ca65 rogue.s
@cc65\ld65 rogue.o -C cfg/c64prg-vortex-8000.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib  -Ln rogue.lbl  -o rogue8000.bin

@dir *.bin
pause

@copy vortex8000.bin ..\..\Server\data\
@pushd ..\..\Server\data\
@call combine.cmd
@popd
