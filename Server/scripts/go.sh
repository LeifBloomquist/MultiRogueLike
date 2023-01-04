#!/bin/bash

OUTFILE="/home/leif/rogue/out.txt"

cd /home/leif/rogue/
rm -f /home/leif/rogue/*.log
rm -f /home/leif/rogue/rogueserver.lock

while true
do
    /usr/bin/java -Xms1024m -Xmx1024m -jar /home/leif/rogue/rogueserver.jar mini.ini >>"$OUTFILE" 2>&1
    echo "Server 'rogue' crashed with exit code $?.  Respawning..." >>"$OUTFILE" 2>&1
    sleep 1
done
