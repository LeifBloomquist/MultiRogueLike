@cls
@echo off

path = C:\cc65\bin;%PATH%

del target\u-rogue6000.prg
..\..\Tools\cc65\bin\cl65 -Oi -t c64 -C src\cfg\c64-nobasic.cfg src\lib\ultimate_lib_net.c src\u-rogueclient.c ..\C64\src\defaultscreen.s ..\C64\src\colorlookup.s ..\C64\src\screen.s ..\C64\src\sound.s -o target\u-rogue6000.prg

choice /c yn /n /m "Run on Ultimate64 Yes, No?"
if %ERRORLEVEL% == 2 exit /b

u64remote 192.168.7.64 reset
u64remote 192.168.7.64 load ..\common\font\rogue-font4000.prg
u64remote 192.168.7.64 load target\u-rogue6000.prg
u64remote 192.168.7.64 type sys24576