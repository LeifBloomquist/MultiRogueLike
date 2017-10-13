#!/bin/bash

rm *.lock
/usr/bin/pkill -f gameloop.sh
/usr/bin/pkill -f vortexserver
/usr/bin/java -Xmx1024M -jar /home/leif/vortex/vortexserver.jar
