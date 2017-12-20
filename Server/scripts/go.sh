rm rogueserver.lock
rm out.txt
/usr/bin/pkill -f vortexserver
java -Xms2048m -Xmx2048m -jar rogueserver.jar >>out.txt 2>&1