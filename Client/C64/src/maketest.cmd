@rem This batch file creates a local test executable with load address prepended, etc.   Assumes you are loading the font file manually beforehand!
@rem The resulting program can be started with SYS 24576

@cls
@del *.o
@del *.bin
@if not exist c:\rogue mkdir c:\rogue
@del c:\rogue\*.bin

@..\..\..\Tools\cc65\bin\ca65 rogue.s
@..\..\..\Tools\cc65\bin\ld65 rogue.o -C cfg/c64prg-rogue-6000.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -m rogue6000.map -vm -o rogue6000.bin
@copy /b /v loadaddress6000.bbb + rogue6000.bin c:\rogue\rogue6000-la.bin

@dir c:\rogue\*.bin

@exit /b