@rem batch file to pad+combine all Vortex data for TFTP download

trunc music.raw    4096
trunc chars.raw    2048
trunc screen.raw   1024
trunc Sprites.raw 13312
@rem leave vortex8000.bin as-is, since it's last

copy /b /v /y music.raw+chars.raw+screen.raw+Sprites.raw+vortex8000.bin tftpboot\vortexdata