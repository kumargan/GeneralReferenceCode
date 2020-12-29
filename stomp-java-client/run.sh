#!bin bash

echo "stopping old server"
file="process.pid"
		if [ -f "$file" ]
		then
       		kill `cat process.pid`
		fi
	cp /dev/null process.pid

rm -rf target
mvn package > build.logs

if [ $? -eq 0 ]; then
	echo "packaging successful "
	echo "**********************************************************************************"

	if [ "$debug" = 'debug' ]; then
		echo "starting server in debug mode on port 8000"
		java -Xmx2000M -Xms256M -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n \
       -jar target/indexer-0.0.1-SNAPSHOT.jar --server.port=8085 1>>build.logs 2>>build.logs &
	else
		echo "starting server "
		java -Xmx2000M -Xms256M -jar target/stomp-java-client-1.0.jar 1>>build.logs 2>>build.logs &
	fi
	echo $! >> process.pid
else
	echo "packaging failed, check build.logs"
fi