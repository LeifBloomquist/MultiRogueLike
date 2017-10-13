#!/bin/bash

pkill -f vortexserver
java -Xmx1024M -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=y -jar ./vortexserver.jar
