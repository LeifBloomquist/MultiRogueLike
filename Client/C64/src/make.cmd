@rem This batch file makes the full executable that can be placed on the TFTP Server

@cls
@del *.o
@del *.bin
@del ..\target\*.bin

@..\..\..\Tools\cc65\bin\ca65 rogue.s
@..\..\..\Tools\cc65\bin\ld65 rogue.o -C cfg/c64prg-rogue-6000.cfg ip65/ip65/ip65.lib  ip65/drivers/c64rrnet.lib -m rogue6000.map -vm -o rogue6000.bin
@copy /b /v loadaddress6000.bbb + rogue6000.bin ..\target\rogue6000-la.bin

@dir /b ..\target\*.bin

@rem Pad+combine all Rogue data for download.

@..\..\..\Tools\trunc ..\..\common\rogue-font.raw 2048
@..\..\..\Tools\trunc ..\..\common\empty.raw      6144

@copy /b /v /y "..\..\common\rogue-font.raw"+"..\..\common\empty.raw"+rogue6000.bin ..\target\tftpboot\roguedata
