@rem batch file to pad+combine all Rogue data for TFTP download

@copy ..\..\Client\C64\src\rogue6000.bin .

trunc rogue-font.raw 2048
trunc empty.raw      6144
@rem leave rogue6000.bin as-is, since it's last

copy /b /v /y rogue-font.raw+empty.raw+rogue6000.bin tftpboot\roguedata