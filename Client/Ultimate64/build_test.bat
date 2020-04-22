@cls
@echo off

rem path = C:\cc65\bin;%PATH%

del target\u-rogue6000.prg
cl65 -Oi -t c64 -C src\cfg\c64-nobasic.cfg src\lib\ultimate_lib_net.c src\u-rogueclient.c ..\C64\src\defaultscreen.s ..\C64\src\colorlookup.s ..\C64\src\screen.s ..\C64\src\sound.s -o target\u-rogue6000.prg
pause
u64remote 192.168.7.64 reset
u64remote 192.168.7.64 load C:\Leif\GitHub\MultiRogueLike\Client\common\font\rogue-font4000.prg
u64remote 192.168.7.64 load target\u-rogue6000.prg
u64remote 192.168.7.64 type sys24576