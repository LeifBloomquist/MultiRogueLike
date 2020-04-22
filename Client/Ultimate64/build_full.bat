@cls
@echo off

@rem path = C:\cc65\bin;%PATH%

del target\u-rogue6000.prg
cl65 -Oi -t c64 -C src\cfg\c64-nobasic.cfg src\lib\ultimate_lib_net.c src\u-rogueclient.c ..\C64\src\defaultscreen.s ..\C64\src\colorlookup.s ..\C64\src\screen.s ..\C64\src\sound.s -o target\u-rogue6000.prg
pause

@rem Pad+combine all Rogue data for download.  Note that empty.raw is 2 bytes less than it should be, to "hide" the load address from roguedata-u64.bin and align start with $6000

..\common\trunc ..\common\rogue-font.raw 2048
..\common\trunc ..\common\empty.raw      6142

copy /b /v /y "..\common\rogue-font.raw"+"..\common\empty.raw"+"target\u-rogue6000.prg" "target\roguedata-u64.bin"