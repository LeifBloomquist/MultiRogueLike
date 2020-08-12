@rem This batch file makes the full executable that can be placed on the TFTP Server

@cls
@del *.o
@del *.bin
@del ..\target\*.bin

@cc65\ca65 rogue.s
@cc65\ld65 rogue.o -C cfg/c64prg-rogue-6000.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -m rogue6000.map -vm -o rogue6000.bin
@copy /b /v loadaddress6000.bbb + rogue6000.bin ..\target\rogue6000-la.bin

@dir ..\target\*.bin

@exit /b

pause
@rem Combine with font, etc- Still in work!

@copy ..\target\*.bin ..\..\Server\data\
@pushd ..\..\Server\data\
@call combine.cmd
@popd
