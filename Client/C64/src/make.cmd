@cls
@del *.o
@del *.bin
@del p:\rogue\*.bin

@cc65\ca65 rogue.s
@cc65\ld65 rogue.o -C cfg/c64prg-vortex-8000.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -o rogue8000.bin
copy /b /v loadaddress8000.bbb + rogue8000.bin p:\rogue\rogue8000-la.bin

@dir p:\rogue\*.bin

@exit /b

pause

@copy vortex8000.bin ..\..\Server\data\
@pushd ..\..\Server\data\
@call combine.cmd
@popd
