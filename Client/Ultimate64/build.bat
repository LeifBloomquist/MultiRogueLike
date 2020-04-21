@cls
@echo off

rem path = C:\cc65\bin;%PATH%

cl65 -Oi -t c64 -C src\cfg\c64-nobasic.cfg src\lib\ultimate_lib_net.c src\u-rogueclient.c ..\C64\src\defaultscreen.s ..\C64\src\colorlookup.s ..\C64\src\screen.s ..\C64\src\sound.s -o target\u-rogueclient.prg
u64remote 192.168.7.64 load c:\xlar54\playground\rogue-font4000.prg
u64remote 192.168.7.64 load target\u-rogueclient.prg