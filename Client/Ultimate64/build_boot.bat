@cls
@echo off

@rem path = C:\cc65\bin;%PATH%

cl65 -Oi -t c64 src\lib\ultimate_lib_net.c src\u-rogueboot.c -o target\u-rogueboot.prg
u64remote 192.168.7.64 load target\u-rogueboot.prg