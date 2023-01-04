#!/bin/bash

OUTFILE="/home/leif/rogue/ultimate64/out.txt"

cd /home/leif/rogue/ultimate64/
#rm -f "$OUTFILE"

while true
do
   /usr/bin/java -jar /home/leif/rogue/ultimate64/u64fileserver.jar 3064 roguedata-u64.bin >> "$OUTFILE"
   sleep 1
done
