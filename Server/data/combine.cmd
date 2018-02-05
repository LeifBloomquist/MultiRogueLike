@rem batch file to pad+combine all Rogue data for TFTP download

@copy ..\..\Client\C64\src\rogue8000.bin .

trunc rogue-font.raw 2048
trunc empty.raw      14336
@rem leave rogue8000.bin as-is, since it's last

copy /b /v /y rogue-font.raw+empty.raw+rogue8000.bin tftpboot\roguedata