#!/bin/bash

echo Purging logs...
rm out.txt

echo Killing old server loop...
/usr/bin/pkill -f gameloop.sh

echo Killing old server instance...
/usr/bin/pkill -f vortexserver

echo Starting game loop script
./gameloop.sh &
