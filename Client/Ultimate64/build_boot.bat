@cls
@echo off

..\..\Tools\cc65\bin\cl65 -Oi -t c64 src\lib\ultimate_lib_net.c src\u-rogueboot.c -o target\u64-rogueboot.prg
u64remote 192.168.7.64 run target\u64-rogueboot.prg