#!/bin/bash

while true
do

  echo Removing lock file...
  rm -f /home/leif/vortex/vortexserver.lock

  echo Starting Vortex Server \(logging to out.txt\)...
  /usr/bin/java -Xmx1024M -jar /home/leif/vortex/vortexserver.jar >>out.txt 2>&1
  sleep 1s

done
