@cls
@echo off

path =..\..\Tools\cc65\bin\cl65;C:\Leif\GitHub\Ultimate64\Executable;%PATH%

set PRG=target\u-rogue6000test.prg

del %PRG%
..\..\Tools\cc65\bin\cl65 -D LOCAL_TEST -Oi -t c64 -C src\cfg\c64-nobasic.cfg src\lib\ultimate_lib_net.c src\u-rogueclient.c ..\C64\src\colordefs.s ..\C64\src\screen.s ..\C64\src\sound.s -o %PRG%

choice /c yn /n /m "Run on Ultimate64 Yes, No?"
if %ERRORLEVEL% == 2 exit /b

u64remote 192.168.7.64 reset
u64remote 192.168.7.64 load ..\common\font\rogue-font4000.prg
u64remote 192.168.7.64 load %PRG%
u64remote 192.168.7.64 type sys24576